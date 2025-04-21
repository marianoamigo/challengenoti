package com.challenge.mentoria.errors;

public class ErrorService extends Exception{
    //se crea esta clase para diferenciar errores propios de bugs y errores propios del sistema
    public ErrorService(String message) {
        super(message);
    }
}
