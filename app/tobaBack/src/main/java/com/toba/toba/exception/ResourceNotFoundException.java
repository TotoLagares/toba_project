package com.toba.toba.exception;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String entityName, Long id) {
		super("%s no encontrado con id: %d".formatted(entityName, id));
	}
}
