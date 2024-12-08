package eu.innowise.moviereviewproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @NotBlank(message = "Имя пользователя не может быть пустым.")
        @Size(min = 3, max = 255, message = "Имя пользователя должно содержать минимум 3 символа и максимум 255.")
        String username,

        @NotBlank(message = "Пароль не может быть пустым.")
        @Size(min = 8, message = "Пароль должен содержать минимум 8 символов.")
        String password
) {
}
