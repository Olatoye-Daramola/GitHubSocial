package io.olatoye.githubsocial.service.authService;

import io.olatoye.githubsocial.domain.AuthUser;
import io.olatoye.githubsocial.domain.User;
import io.olatoye.githubsocial.domain.dto.auth.AuthenticationRequest;
import io.olatoye.githubsocial.domain.dto.auth.RegistrationRequest;
import io.olatoye.githubsocial.exception.GithubSocialBadRequestException;
import io.olatoye.githubsocial.exception.GithubSocialInternalServerException;
import io.olatoye.githubsocial.exception.GithubSocialNotFoundException;
import io.olatoye.githubsocial.repository.AuthUserRepository;
import io.olatoye.githubsocial.repository.UserRepository;
import io.olatoye.githubsocial.security.jwt.JwtService;
import io.olatoye.githubsocial.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthUserRepository authUserRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public String register(RegistrationRequest request) {

        try {
            boolean isValidToken = isValidGithubToken(request.getGithubToken(), request.getGithubUsername());
            if (!isValidToken) throw new GithubSocialBadRequestException("GITHUB TOKEN NOT VALID");

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

            return token;
        } catch (GithubSocialBadRequestException e) {
            throw new GithubSocialInternalServerException("\nREGISTRATION FAILED BECAUSE -> \n" + e.getMessage());
        }
    }

    @Override
    public String authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = authUserRepository.findByEmail(request.getEmail())
                .stream().findFirst()
                .orElseThrow(() -> new GithubSocialNotFoundException("User not registered"));
        return jwtService.generateToken(user);
    }


    // -------------------------------- HELPER METHODS --------------------------------
    private boolean isValidGithubToken(String githubToken, String username) {
        try {
            ResponseEntity<String> response = callApi(githubToken, username);
            return !Objects.requireNonNull(response.getBody()).isBlank() || !response.getBody().isEmpty();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError())
                return false;
            throw new GithubSocialInternalServerException(e.getMessage());
        } catch (Exception e) {
            throw new GithubSocialInternalServerException(e.getMessage());
        }
    }

    private void saveUserDetails(String githubToken, String username) {
        try {
            ResponseEntity<String> response = callApi(githubToken, username);
            String responseBody = Objects.requireNonNull(response.getBody());
            User user = parseJsonToUser(responseBody);
            userRepository.save(user);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError())
                throw new GithubSocialInternalServerException(e.getMessage());
        } catch (Exception e) {
            throw new GithubSocialInternalServerException(e.getMessage());
        }
    }

    private ResponseEntity<String> callApi(String githubToken, String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        String baseUrl = Constants.getUserDetailsUrl(username);

        RestTemplate template = new RestTemplate();
        return template.exchange(baseUrl, HttpMethod.GET, entity, String.class);
    }

    private User parseJsonToUser(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);

        User user = new User();
        user.setGithubUserId(jsonObject.getInt("id"));
        user.setUsername(jsonObject.getString("login"));
        user.setEmail(jsonObject.getString("email"));
        user.setAvatar(jsonObject.getString("avatar_url"));
        user.setProfile(jsonObject.getString("html_url"));
        user.setRepository(jsonObject.getString("repos_url"));
        user.setNumberOfFollowers(jsonObject.getInt("followers"));
        user.setNumberOfFollowing(jsonObject.getInt("following"));

        return user;
    }
}