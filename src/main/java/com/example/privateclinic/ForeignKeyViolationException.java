package com.example.privateclinic;

public class ForeignKeyViolationException extends Exception{
    public ForeignKeyViolationException(String message) {
        super(message);
    }
}
