package eu.innowise.moviereviewproject.validator;

import eu.innowise.moviereviewproject.dto.request.RegistrationRequest;
import eu.innowise.moviereviewproject.validator.annotation.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RegistrationRequest> {

    @Override
    public boolean isValid(RegistrationRequest dto, ConstraintValidatorContext context) {
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
