package io.olatoye.githubsocial.service.userService;

import io.olatoye.githubsocial.domain.dto.ResponseSchema;
import io.olatoye.githubsocial.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
