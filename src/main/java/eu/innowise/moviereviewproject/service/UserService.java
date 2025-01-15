package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.dto.request.RegistrationRequest;
import eu.innowise.moviereviewproject.dto.response.UserResponse;
import eu.innowise.moviereviewproject.exceptions.DtoValidationException;
import eu.innowise.moviereviewproject.exceptions.EntityAlreadyExistsException;
import eu.innowise.moviereviewproject.exceptions.EntityNotFoundException;
import eu.innowise.moviereviewproject.mapper.UserMapper;
import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.model.enums.UserRole;
import eu.innowise.moviereviewproject.repository.UserRepository;
import eu.innowise.moviereviewproject.repository.impl.UserRepositoryImpl;
import eu.innowise.moviereviewproject.validator.DtoValidator;
import org.mapstruct.factory.Mappers;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.Constants.USER_NOT_FOUND_BY_ID;
import static eu.innowise.moviereviewproject.utils.Constants.USER_NOT_FOUND_BY_USERNAME;

public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = Mappers.getMapper(UserMapper.class);
    }

    private static class SingletonHelper {
        private static final UserService INSTANCE = new UserService(
                UserRepositoryImpl.getInstance()
        );
    }

    public static UserService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponse getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toSummaryResponse)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, id)));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_USERNAME, username)));
    }

    public void registerUser(RegistrationRequest dto) throws DtoValidationException {
        DtoValidator.validate(dto);

        User user = userMapper.toUser(dto);
        user.setPassword(hashPassword(dto.password()));
        user.setUserRole(UserRole.USER);

        saveUser(user);
    }

    public void saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during user registration", e);
        }

    }

    public void updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, user.getId()));
        }
        userRepository.update(user);
    }


    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, id));
        }
        userRepository.deleteById(id);
    }

    private String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }
}
