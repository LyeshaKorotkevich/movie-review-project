package eu.innowise.moviereviewproject.mapper;

import eu.innowise.moviereviewproject.dto.request.RegistrationRequest;
import eu.innowise.moviereviewproject.dto.response.UserResponse;
import eu.innowise.moviereviewproject.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    User toUser(RegistrationRequest dto);

    //UserResponse toDetailedResponse(User user);

    UserResponse toSummaryResponse(User user);
}
