package io.olatoye.githubsocial.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(BAD_REQUEST)
public class GithubSocialBadRequestException extends RuntimeException {

    public GithubSocialBadRequestException(String message) {
        super(message);
    }
}
