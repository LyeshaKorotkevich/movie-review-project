package eu.innowise.moviereviewproject.dto;

import eu.innowise.moviereviewproject.validator.PasswordMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatch
public record RegistrationDTO(

        @NotBlank(message = "Имя пользователя не может быть пустым.")
        @Size(min = 3, max = 255, message = "Имя пользователя должно содержать минимум 3 символа и максимум 255.")
        String username,

        @NotBlank(message = "Email не может быть пустым.")
        @Email(message = "Пожалуйста, введите корректный email.")
        String email,

        @NotBlank(message = "Пароль не может быть пустым.")
        @Size(min = 8, message = "Пароль должен содержать минимум 8 символов.")
        String password,

        @NotBlank(message = "Подтверждение пароля не может быть пустым.")
        String confirmPassword
) {
}
