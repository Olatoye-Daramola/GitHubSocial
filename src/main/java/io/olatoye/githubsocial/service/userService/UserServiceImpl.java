package io.olatoye.githubsocial.service.userService;

import io.olatoye.githubsocial.domain.AuthUser;
import io.olatoye.githubsocial.domain.GithubUser;
import io.olatoye.githubsocial.domain.User;
import io.olatoye.githubsocial.domain.dto.ResponseSchema;
import io.olatoye.githubsocial.exception.GithubSocialInternalServerException;
import io.olatoye.githubsocial.exception.GithubSocialNotFoundException;
import io.olatoye.githubsocial.repository.AuthUserRepository;
import io.olatoye.githubsocial.repository.UserRepository;
import io.olatoye.githubsocial.utils.Constants;
import io.olatoye.githubsocial.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Async
    @Override
    public CompletableFuture<ResponseSchema> getAuthUserDetails() {
        try {
            return CompletableFuture.supplyAsync(() -> {
                AuthUser authUser = getLoggedInUser();
                User user = userRepository.findByEmail(authUser.email)
                        .orElseThrow(() -> new GithubSocialNotFoundException("Unable to find this user"));
                return ResponseSchema.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("success")
                        .data(user)
                        .build();
            });
        } catch (Exception e) {
            throw new GithubSocialInternalServerException(
                    String.format("\nUNABLE TO GET USER DETAILS BECAUSE: %s\n", e.getMessage())
            );
        }
    }

    @Override
    public CompletableFuture<ResponseSchema> getUserDetails(String username) {
        try {
            return CompletableFuture.supplyAsync(() -> {
                AuthUser authUser = getLoggedInUser();
                User user = userRepository.findByEmail(authUser.email)
                        .orElseThrow(() -> new GithubSocialNotFoundException("Unable to find this user"));

                ResponseEntity<String> response =
                        Utils.callApi(authUser.getGithubToken(), Constants.getUserDetailsUrl.apply(username));
                String responseBody = Objects.requireNonNull(response.getBody());
                GithubUser githubUser = Utils.parseJsonToUser(responseBody);
                return ResponseSchema.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("success")
                        .data(githubUser)
                        .build();
            });
        } catch (Exception e) {
            throw new GithubSocialInternalServerException(
                    String.format("\nUNABLE TO GET USER BECAUSE: %s\n", e.getMessage())
            );
        }
    }

    @Async
    @Override
    public CompletableFuture<ResponseSchema> getFollowers() {
        try {
            return CompletableFuture.supplyAsync(() -> getFollowersOrFollowing("followers"));
        } catch (Exception e) {
            throw new GithubSocialInternalServerException(
                    String.format("\nUNABLE TO GET USER FOLLOWERS BECAUSE: %s\n", e.getMessage())
            );
        }
    }

    @Override
    public CompletableFuture<ResponseSchema> getFollowing() {
        try {
            return CompletableFuture.supplyAsync(() -> getFollowersOrFollowing("following"));
        } catch (Exception e) {
            throw new GithubSocialInternalServerException(
                    String.format("\nUNABLE TO GET USER FOLLOWINGS BECAUSE: %s\n", e.getMessage())
            );
        }
    }


    //-------------------------------- HELPER METHODS --------------------------------
    private AuthUser getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (AuthUser) auth.getPrincipal();
    }

    private List<GithubUser> parseJsonToGithubUserList(String jsonString) {

        return new JSONArray(jsonString).toList().stream()
                .map(jsonObject -> Utils.parseJsonToUser(jsonObject.toString()))
                .toList();
    }

    private ResponseSchema getFollowersOrFollowing(String typeOfUsersToGet) {
        AuthUser authUser = getLoggedInUser();
        User user = userRepository.findByEmail(authUser.email)
                .orElseThrow(() -> new GithubSocialNotFoundException("Unable to find this user"));

        byte[] decodedTokenArray = Base64.getDecoder().decode(authUser.getGithubToken());
        String githubToken = new String(decodedTokenArray, StandardCharsets.UTF_8);

        String url;
        if (Objects.equals(typeOfUsersToGet, "followers")) url = Constants.getFollowersUrl.apply(user.getUsername());
        else  url = Constants.getFollowingUrl.apply(user.getUsername());

        ResponseEntity<String> response = Utils.callApi(githubToken, url);

        List<GithubUser> githubFollowers = parseJsonToGithubUserList(response.getBody());
        if (!githubFollowers.isEmpty())
            githubFollowers
                    .forEach(githubUser -> {
                        user.getFollowers().add(githubUser);
                    });
        userRepository.save(user);
        return ResponseSchema.builder()
                .statusCode(HttpStatus.OK.value())
                .message("success")
                .data(user.getFollowers())
                .build();
    }
}
