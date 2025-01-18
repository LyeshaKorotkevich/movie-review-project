package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.dto.request.LoginRequest;
import eu.innowise.moviereviewproject.dto.response.UserResponse;
import eu.innowise.moviereviewproject.exceptions.EntityNotFoundException;
import eu.innowise.moviereviewproject.exceptions.user.InvalidPasswordException;
import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.repository.impl.UserRepositoryImpl;
import eu.innowise.moviereviewproject.validator.DtoValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.google.inject.matcher.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepositoryImpl userRepository;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {

        try (MockedStatic<UserRepositoryImpl> mockStaticRepo = mockStatic(UserRepositoryImpl.class)) {
            mockStaticRepo
                    .when(UserRepositoryImpl::getInstance)
                    .thenReturn(userRepository);
            authenticationService = AuthenticationService.getInstance();
        }
    }

    @Test
    void testAuthenticate_Success() {
        // given
        String username = "testUser";
        String password = "password";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        try (MockedStatic<DtoValidator> mockedValidator = Mockito.mockStatic(DtoValidator.class)) {
            mockedValidator.when(() -> DtoValidator.validate(any()))
                    .thenAnswer(invocation -> null);

            LoginRequest request = new LoginRequest(username, password);

            // when
            UserResponse response = authenticationService.authenticate(request);

            // then
            assertNotNull(response);
            assertEquals("testUser", response.username());
        }
    }

    @Test
    void testAuthenticate_shouldThrowEntityNotFound() {
        // given
        String username = "someone";

        LoginRequest request = new LoginRequest(username, "password");

        // when & then
        assertThrows(
                EntityNotFoundException.class,
                () -> authenticationService.authenticate(request)
        );

    }

    @Test
    void testAuthenticate_InvalidPassword() {
        // given
        String username = "testUser";
        String hashedPassword = BCrypt.hashpw("correctPassword", BCrypt.gensalt());

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);

        LoginRequest request = new LoginRequest(username, "wrongPassword");

        // when & then
        assertThrows(
                InvalidPasswordException.class,
                () -> authenticationService.authenticate(request)
        );
    }
}
