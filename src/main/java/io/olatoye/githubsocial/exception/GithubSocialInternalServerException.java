package io.olatoye.githubsocial.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(INTERNAL_SERVER_ERROR)
public class GithubSocialInternalServerException extends RuntimeException {

    public GithubSocialInternalServerException(String message) {
        super(message);
    }
}
