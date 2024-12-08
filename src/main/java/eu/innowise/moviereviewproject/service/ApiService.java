package eu.innowise.moviereviewproject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.innowise.moviereviewproject.dto.MovieDTO;
import eu.innowise.moviereviewproject.mapper.MovieMapper;
import eu.innowise.moviereviewproject.model.Genre;
import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.model.MovieType;
import eu.innowise.moviereviewproject.model.Person;
import eu.innowise.moviereviewproject.repository.GenreRepository;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static eu.innowise.moviereviewproject.utils.Constants.API_KEY;
import static eu.innowise.moviereviewproject.utils.Constants.API_URL;

@Slf4j
public class ApiService {

    private static final int MOVIES_PER_PAGE = 12;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private final MovieRepository movieRepository;
    private final PersonRepository personRepository;
    private final GenreRepository genreRepository;
    private final MovieMapper movieMapper;

    public ApiService(HttpClient httpClient, ObjectMapper objectMapper, MovieRepository movieRepository, PersonRepository personRepository, GenreRepository genreRepository) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.movieRepository = movieRepository;
        this.personRepository = personRepository;
        this.genreRepository = genreRepository;
        this.movieMapper = Mappers.getMapper(MovieMapper.class);
    }

    public List<MovieDTO> fetchMoviesFromApi(int page, int typeNumber) throws Exception {
        String url = buildMoviesUrl(page, typeNumber);
        return fetchMoviesFromApi(url);
    }

    public List<MovieDTO> fetchMoviesFromSearchFromApi(int page, String query) throws Exception {
        String url = buildSearchUrl(page, query);
        return fetchMoviesFromApi(url);
    }

    private List<MovieDTO> fetchMoviesFromApi(String url) throws Exception {
        HttpResponse<String> response = sendApiRequest(url);

        if (response.statusCode() != 200) {
            log.error("API error: status code {}", response.statusCode());
            throw new Exception("API Error: Status Code " + response.statusCode());
        }

        return parseMoviesFromResponse(response.body());
    }

    private HttpResponse<String> sendApiRequest(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("X-API-KEY", API_KEY)
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private List<MovieDTO> parseMoviesFromResponse(String responseBody) throws Exception {
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode moviesNode = rootNode.get("docs");
        List<Movie> movies = new ArrayList<>();

        if (moviesNode != null && moviesNode.isArray()) {
            for (JsonNode movieNode : moviesNode) {
                Movie movie = mapToMovie(movieNode);

                if (!movieRepository.existsByExternalId(movie.getExternalId())) {
                    movieRepository.save(movie);
                }

                movies.add(movie);
            }
        }
        return movies.stream().map(movieMapper::toSummaryDTO).toList();
    }

    private String buildMoviesUrl(int page, int typeNumber) {

        StringBuilder urlBuilder = new StringBuilder(API_URL)
                .append("?page=").append(page)
                .append("&limit=").append(MOVIES_PER_PAGE)
                .append("&selectFields=id&selectFields=name&selectFields=description&selectFields=year&selectFields=poster&selectFields=typeNumber&selectFields=genres&selectFields=persons")
                .append("&notNullFields=id&notNullFields=name&notNullFields=description&notNullFields=year&notNullFields=poster.url");

        if (typeNumber > 0) {
            urlBuilder.append("&typeNumber=").append(typeNumber);
        }

        return urlBuilder.toString();
    }

    private String buildSearchUrl(int page, String query) {
        return API_URL +
                "/search?page=" + page +
                "&limit=" + MOVIES_PER_PAGE +
                "&query=" + query;

    }

    private String buildMovieUrl(Long externalId) {
        return API_URL + "/" + externalId;

    }

    private Movie mapToMovie(JsonNode movieNode) {
        if (movieNode == null) {
            throw new RuntimeException("Movie node is null");
        }

        Movie movie = new Movie();
        movie.setExternalId(movieNode.get("id") != null ? movieNode.get("id").asLong() : 0L);
        movie.setTitle(movieNode.get("name") != null ? movieNode.get("name").asText() : "");
        movie.setPosterUrl(movieNode.get("poster") != null && movieNode.get("poster").get("url") != null ? movieNode.get("poster").get("url").asText() : "");
        movie.setReleaseYear(movieNode.get("year") != null ? movieNode.get("year").asInt() : 0);
        movie.setDescription(movieNode.get("description") != null ? movieNode.get("description").asText() : "");
        movie.setMovieType(movieNode.get("typeNumber") != null ? MovieType.fromTypeNumber(movieNode.get("typeNumber").asInt()) : null);
        movie.setGenres(mapToGenres(movieNode));

        JsonNode personsNode = movieNode.get("persons");
        if (personsNode != null && personsNode.isArray()) {
            movie.setPersons(mapToPersons(personsNode));
        } else {
            try {
                movie.setPersons(fetchPersonsFromApi(movie.getExternalId()));
            } catch (Exception e) {
                log.error("Error fetching persons for movie: {}", movie.getExternalId(), e);
                movie.setPersons(new HashSet<>());
            }
        }

        return movie;
    }


    private Set<Person> fetchPersonsFromApi(Long movieExternalId) throws Exception {
        String url = buildMovieUrl(movieExternalId);
        HttpResponse<String> response = sendApiRequest(url);

        if (response.statusCode() != 200) {
            log.error("API error while fetching persons: status code {}", response.statusCode());
            throw new Exception("API Error: Status Code " + response.statusCode());
        }

        JsonNode rootNode = objectMapper.readTree(response.body());
        JsonNode personsNode = rootNode.get("persons");

        return mapToPersons(personsNode);
    }

    private Set<Genre> mapToGenres(JsonNode movieNode) {
        Set<Genre> genres = new HashSet<>();
        JsonNode genresNode = movieNode.get("genres");

        if (genresNode != null && genresNode.isArray()) {
            for (JsonNode genreNode : genresNode) {
                String genreName = genreNode.get("name").asText();
                Genre genre = new Genre();
                genre.setName(genreName);
                genres.add(genreRepository.findByName(genreName).orElseGet(() -> genreRepository.save(genre)));
            }
        }
        return genres;
    }

    private Set<Person> mapToPersons(JsonNode personsNode) {
        Set<Person> persons = new HashSet<>();

        if (personsNode == null || !personsNode.isArray() || personsNode.isEmpty()) {
            return persons;
        }

        for (JsonNode personNode : personsNode) {
            Person person = mapToPerson(personNode);
            Person existingPerson = personRepository.findByExternalId(person.getExternalId())
                    .orElseGet(() -> personRepository.save(person));
            persons.add(existingPerson);
        }
        return persons;
    }

    private Person mapToPerson(JsonNode personNode) {
        Person person = new Person();
        person.setExternalId(personNode.get("id").asLong());
        person.setPhotoUrl(personNode.get("photo").asText());
        person.setName(personNode.get("name").asText());
        person.setEnName(personNode.get("enName").asText());
        person.setProfession(personNode.get("profession").asText());
        return person;
    }
}