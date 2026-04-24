package io.github.andrelamego.brValidator.cnpj;

public class InvalidCnpjException extends RuntimeException {
    public InvalidCnpjException(String message) {
        super(message);
    }
}
