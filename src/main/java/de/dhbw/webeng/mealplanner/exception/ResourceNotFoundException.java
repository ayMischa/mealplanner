package de.dhbw.webeng.mealplanner.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " mit id " + id + " nicht gefunden");
    }
}