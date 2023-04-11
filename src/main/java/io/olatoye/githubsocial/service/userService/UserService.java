package io.olatoye.githubsocial.service.userService;

import io.olatoye.githubsocial.domain.dto.ResponseSchema;

import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<ResponseSchema> pseudoLogin(String githubToken);

    CompletableFuture<ResponseSchema> getUserDetails();

    CompletableFuture<ResponseSchema> getFollowers();

    CompletableFuture<ResponseSchema> getFollowing();
}
