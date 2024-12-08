package eu.innowise.moviereviewproject.mapper;

import eu.innowise.moviereviewproject.dto.RegistrationDTO;
import eu.innowise.moviereviewproject.dto.UserDTO;
import eu.innowise.moviereviewproject.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "watchlistMovies", ignore = true)
    User toUser(RegistrationDTO dto);

    //UserDTO toDetailedDTO(User user);

    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "watchlistMovies", ignore = true)
    UserDTO toSummaryDTO(User user);
}
