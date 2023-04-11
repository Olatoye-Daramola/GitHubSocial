package io.olatoye.githubsocial.service.userService;

import io.olatoye.githubsocial.domain.dto.ResponseSchema;
import io.olatoye.githubsocial.domain.dto.ResponseSchema;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    private final UserService userService;

    public UserServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CompletableFuture<ResponseSchema> pseudoLogin(String githubToken) {
        return null;
    }

    @Override
    public CompletableFuture<ResponseSchema> getUserDetails() {
        return null;
    }

    @Override
    public CompletableFuture<ResponseSchema> getFollowers() {
        return null;
    }

    @Override
    public CompletableFuture<ResponseSchema> getFollowing() {
        return null;
    }
}
