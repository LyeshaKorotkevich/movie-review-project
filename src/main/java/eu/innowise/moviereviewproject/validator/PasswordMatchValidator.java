package eu.innowise.moviereviewproject.validator;

import eu.innowise.moviereviewproject.dto.RegistrationDTO;
import eu.innowise.moviereviewproject.validator.annotation.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RegistrationDTO> {

    @Override
    public boolean isValid(RegistrationDTO dto, ConstraintValidatorContext context) {
        if (dto.password().equals(dto.confirmPassword())) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пароли не совпадают")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
    }
}
