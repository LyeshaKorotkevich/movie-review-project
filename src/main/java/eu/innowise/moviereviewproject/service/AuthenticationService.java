package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.dto.response.UserResponse;
import eu.innowise.moviereviewproject.dto.request.LoginRequest;
import eu.innowise.moviereviewproject.exceptions.user.InvalidPasswordException;
import eu.innowise.moviereviewproject.exceptions.user.UserNotFoundException;
import eu.innowise.moviereviewproject.mapper.UserMapper;
import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.repository.UserRepository;
import eu.innowise.moviereviewproject.validator.DtoValidator;
import org.mapstruct.factory.Mappers;
import org.mindrot.jbcrypt.BCrypt;

public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = Mappers.getMapper(UserMapper.class);
    }

    public UserResponse authenticate(LoginRequest loginRequest) throws InvalidPasswordException {
        DtoValidator.validate(loginRequest);

        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UserNotFoundException("User with username " + loginRequest.username() + " not found"));

        if (!BCrypt.checkpw(loginRequest.password(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        return userMapper.toSummaryResponse(user);
    }
}
