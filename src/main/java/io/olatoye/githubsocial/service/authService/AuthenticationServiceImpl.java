package io.olatoye.githubsocial.service.authService;

import io.olatoye.githubsocial.domain.AuthUser;
import io.olatoye.githubsocial.domain.User;
import io.olatoye.githubsocial.domain.dto.ResponseSchema;
import io.olatoye.githubsocial.domain.dto.auth.AuthenticationRequest;
import io.olatoye.githubsocial.domain.dto.auth.RegistrationRequest;
import io.olatoye.githubsocial.exception.GithubSocialBadRequestException;
import io.olatoye.githubsocial.exception.GithubSocialInternalServerException;
import io.olatoye.githubsocial.exception.GithubSocialNotFoundException;
import io.olatoye.githubsocial.repository.AuthUserRepository;
import io.olatoye.githubsocial.repository.UserRepository;
import io.olatoye.githubsocial.security.jwt.JwtService;
import io.olatoye.githubsocial.utils.Constants;
import io.olatoye.githubsocial.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthUserRepository authUserRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Constants constants;


    @Override
    @Transactional
    public ResponseSchema register(RegistrationRequest request) {

        try {
            boolean isValidToken = isValidGithubToken(request.getGithubToken(), request.getGithubUsername());
            if (!isValidToken) throw new GithubSocialBadRequestException("GITHUB TOKEN NOT VALID");

            log.info("\nGOT HERE --> REGISTER METHOD\n");

            var user = AuthUser.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .username(request.getEmail())
                    .email(request.getEmail())
                    .githubToken(passwordEncoder.encode(request.getGithubToken()))
                    .githubUsername(request.getGithubUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            authUserRepository.findByEmail(request.getEmail())
                    .ifPresent(foundUser -> {
                        throw new GithubSocialBadRequestException("This user already exists");
                    });
            var savedUser = authUserRepository.save(user);

            String token = jwtService.generateToken(savedUser);
            if (!token.isEmpty() && !token.isBlank())
                saveUserDetails(request.getGithubToken(), request.getGithubUsername());
            else throw new GithubSocialInternalServerException("Unable to generate token");

            return ResponseSchema.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("success")
                    .data(token)
                    .build();
        } catch (GithubSocialBadRequestException e) {
            log.info("\nFAILED TO REGISTER USER BECAUSE: {}\n", Arrays.stream(e.getStackTrace()).toList());
            throw new GithubSocialInternalServerException("\nREGISTRATION FAILED BECAUSE -> \n" + e.getMessage());
        }
    }

    @Override
    public ResponseSchema authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = authUserRepository.findByEmail(request.getEmail())
                .stream().findFirst()
                .orElseThrow(() -> new GithubSocialNotFoundException("User not registered"));
        String token = jwtService.generateToken(user);
        if (token.isEmpty() && token.isBlank())
            throw new GithubSocialInternalServerException("Unable to generate token");

        return ResponseSchema.builder()
                .statusCode(HttpStatus.OK.value())
                .message("success")
                .data(token)
                .build();
    }


    // -------------------------------- HELPER METHODS --------------------------------
    private boolean isValidGithubToken(String githubToken, String username) {
        try {
            log.info("\nGOT HERE --> TOKEN VALIDITY METHOD --> URL: {}\n", constants.getUserDetailsUrl.apply(username));
            ResponseEntity<String> response = Utils.callApi(githubToken, constants.getUserDetailsUrl.apply(username));
            return !Objects.requireNonNull(response.getBody()).isBlank() || !response.getBody().isEmpty();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError())
                return false;
            throw new GithubSocialInternalServerException(e.getMessage());
        } catch (Exception e) {
            log.info("\nAPI CALL FAILED BECAUSE: {}\n", Arrays.stream(e.getStackTrace()).toList());
            throw new GithubSocialInternalServerException(e.getMessage());
        }
    }

    private void saveUserDetails(String githubToken, String username) {
        try {
            ResponseEntity<String> response = Utils.callApi(githubToken, constants.getUserDetailsUrl.apply(username));
            String responseBody = Objects.requireNonNull(response.getBody());
            User user = (User) Utils.parseJsonToUser(responseBody);
            user.setGithubToken(passwordEncoder.encode(githubToken));
            userRepository.save(user);
        } catch (HttpClientErrorException e) {
            log.info("\nFAILED TO SAVE USER BECAUSE: {}\n", Arrays.stream(e.getStackTrace()).toList());
            if (e.getStatusCode().is4xxClientError())
                throw new GithubSocialInternalServerException(e.getMessage());
        } catch (Exception e) {
            log.info("\nFAILED TO SAVE USER BECAUSE: {}\n", Arrays.stream(e.getStackTrace()).toList());
            throw new GithubSocialInternalServerException(e.getMessage());
        }
    }


}