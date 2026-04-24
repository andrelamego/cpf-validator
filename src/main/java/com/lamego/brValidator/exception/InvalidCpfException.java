package com.lamego.brValidator.exception;

public class InvalidCpfException extends RuntimeException {
    public InvalidCpfException(String message) {
        super(message);
    }
}
