package io.olatoye.githubsocial.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class GithubSocialNotFoundException extends RuntimeException {

    public GithubSocialNotFoundException(String message) {
        super(message);
    }
}
