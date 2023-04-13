package io.olatoye.githubsocial.domain.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String githubToken;
    private String githubUsername;
}
