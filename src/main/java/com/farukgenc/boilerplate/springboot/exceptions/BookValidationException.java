package com.farukgenc.boilerplate.springboot.exceptions;
import java.util.List;

public class BookValidationException extends RuntimeException {
    private final List<String> errors;

    public BookValidationException(List<String> errors) {
        super("Book validation failed");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}

