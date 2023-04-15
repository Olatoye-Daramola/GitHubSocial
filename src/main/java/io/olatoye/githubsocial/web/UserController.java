package io.olatoye.githubsocial.web;

import io.olatoye.githubsocial.domain.dto.ResponseSchema;
import io.olatoye.githubsocial.service.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/github-social/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<ResponseSchema> getLoggedInUserDetails() {
        ResponseSchema response = userService.getAuthUserDetails().join();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ResponseSchema> getUserDetails(@PathVariable("username") String username) {
        ResponseSchema response = userService.getUserDetails(username).join();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/followers")
    public ResponseEntity<ResponseSchema> getFollowers() {
        ResponseSchema response = userService.getFollowers().join();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/following")
    public ResponseEntity<ResponseSchema> getFollowing() {
        ResponseSchema response = userService.getFollowing().join();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
