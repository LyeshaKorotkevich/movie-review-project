package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.dto.RegistrationDTO;
import eu.innowise.moviereviewproject.exceptions.DtoValidationException;
import eu.innowise.moviereviewproject.exceptions.user.UserAlreadyExistsException;
import eu.innowise.moviereviewproject.exceptions.user.UserNotFoundException;
import eu.innowise.moviereviewproject.mapper.UserMapper;
import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.model.UserRole;
import eu.innowise.moviereviewproject.repository.UserRepository;
import eu.innowise.moviereviewproject.validator.DtoValidator;
import org.mapstruct.factory.Mappers;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = Mappers.getMapper(UserMapper.class);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getMovieById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }

    public void registerUser(RegistrationDTO dto) throws DtoValidationException {
        DtoValidator.validate(dto);

        User user = userMapper.toUser(dto);
        user.setPassword(hashPassword(dto.password()));
        user.setUserRole(UserRole.USER);

        saveUser(user);
    }

    public void saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (UserAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during user registration", e);
        }

    }

    public void updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException("Cannot update user. User with ID " + user.getId() + " not found");
        }
        userRepository.update(user);
    }


    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Cannot delete user. User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    private String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }
}
