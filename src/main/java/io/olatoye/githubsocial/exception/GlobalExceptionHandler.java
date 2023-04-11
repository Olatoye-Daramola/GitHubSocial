package io.olatoye.githubsocial.exception;

import io.olatoye.githubsocial.domain.dto.ResponseSchema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ GithubSocialInternalServerException.class })
    public ResponseEntity<ResponseSchema> internalServerError(GithubSocialInternalServerException ex) {
        ResponseSchema response = ResponseSchema.builder()
                .statusCode(500)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ GithubSocialNotFoundException.class })
    public ResponseEntity<ResponseSchema> notFound(GithubSocialNotFoundException ex) {
        ResponseSchema response = ResponseSchema.builder()
                .statusCode(404)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, NOT_FOUND);
    }
}
