package eu.innowise.moviereviewproject.model;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public enum MovieType {

    MOVIE(1),
    TV_SHOW(2),
    CARTOON(3),
    ANIME(4),
    ANIMATED_TV_SHOW(5);

    private static final Map<Integer, MovieType> TYPE_MAP = Map.of(
            1, MOVIE,
            2, TV_SHOW,
            3, CARTOON,
            4, ANIME,
            5, ANIMATED_TV_SHOW
    );

    private final int typeNumber;

    public static Optional<MovieType> fromTypeNumber(int typeNumber) {
        return Optional.ofNullable(TYPE_MAP.get(typeNumber));
    }
}
