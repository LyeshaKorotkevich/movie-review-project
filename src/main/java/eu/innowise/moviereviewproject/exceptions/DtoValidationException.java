package eu.innowise.moviereviewproject.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class DtoValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public DtoValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }
}
