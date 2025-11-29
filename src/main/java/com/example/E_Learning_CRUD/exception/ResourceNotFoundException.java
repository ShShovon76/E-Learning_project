package com.example.E_Learning_CRUD.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " not found with id = " + id);
    }
    public ResourceNotFoundException(String message) { super(message); }
}
