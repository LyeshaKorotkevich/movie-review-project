package eu.innowise.moviereviewproject.validator.annotation;

import eu.innowise.moviereviewproject.validator.PasswordMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordMatch {

    String message() default "Пароли не совпадают.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}