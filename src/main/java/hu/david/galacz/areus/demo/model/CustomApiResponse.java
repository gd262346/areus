package hu.david.galacz.areus.demo.model;

import org.springframework.http.HttpStatus;

public class CustomApiResponse extends RuntimeException{

    private final String message;
    private final HttpStatus httpStatus;

    public CustomApiResponse(HttpStatus httpStatus, String message) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
