package me.elaamiri.customerservice.Exceptions;

public class CustomerNotFountException extends RuntimeException{
    public CustomerNotFountException(String message) {
        super(message);
    }
}
