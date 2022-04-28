package com.game.kalah.advice;

import com.game.kalah.constants.ErrorMessage;
import com.game.kalah.dto.ApiError;
import com.game.kalah.exception.CustomServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomExceptionResponseHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CustomExceptionResponseHandler.class);

    @ExceptionHandler({CustomServiceException.class})
    public ResponseEntity<Object> handleCustomServiceException(CustomServiceException ex) {
        LOG.info("Invalid Move - {}, {}", ex.getError().getCode(), ex.getError().getMessage());
        return new ResponseEntity<>(ex.getError(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        ex.printStackTrace();
        LOG.info("Unexpected error - {}", ex.getStackTrace());
        return new ResponseEntity<>(new ApiError(ErrorMessage.UNHANDLED_EXCEPTION),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
