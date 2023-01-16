package it.florence.assignment.user_management.exceptions;

public class CSVParsingException extends RuntimeException{

    public CSVParsingException() {
        super();
    }

    public CSVParsingException(String message) {
        super(message);
    }
}
