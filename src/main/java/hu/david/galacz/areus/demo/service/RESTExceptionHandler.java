package hu.david.galacz.areus.demo.service;

import hu.david.galacz.areus.demo.model.CustomApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RESTExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RESTExceptionHandler.class);

    @ExceptionHandler(CustomApiResponse.class)
    public ResponseEntity<?> handleCustomException(CustomApiResponse customApiResponseAsException) {

        LOG.error(customApiResponseAsException.getMessage());

        return ResponseEntity
                .status(customApiResponseAsException.getHttpStatus())
                .body(customApiResponseAsException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomApiResponse> handleGlobalException(Exception exception) {

        LOG.error(exception.getMessage(), exception);

        CustomApiResponse customApiResponseAsException = new CustomApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(customApiResponseAsException);
    }

}
