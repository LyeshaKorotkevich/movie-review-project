package eu.innowise.moviereviewproject.validator;

import eu.innowise.moviereviewproject.exceptions.DtoValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DtoValidator {

    public static <T> void validate(T object) throws DtoValidationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<T> violation : violations) {
                log.debug(violation.getMessage());
                String field = violation.getPropertyPath().toString();
                errors.put(field, errors.getOrDefault(field, "") + violation.getMessage());
            }
            log.debug(errors.toString());
            throw new DtoValidationException(errors);
        }
    }
}
