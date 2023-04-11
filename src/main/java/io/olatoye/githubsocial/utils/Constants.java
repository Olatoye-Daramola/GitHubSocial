package io.olatoye.githubsocial.utils;

import org.springframework.beans.factory.annotation.Value;

public class Constants {

    @Value("${github_url}")
    private String githubUrl;

    public String getFollowersUrl(String username) {
        return githubUrl + username + "/followers";
    }

    public String getFollowingUrl(String username) {
        return githubUrl + username + "/following";
    }

    public String getUserDetailsUrl(String username) {
        return githubUrl + username;
    }

    public String getUserEmailUrl(String username) {
        return githubUrl + username + "/public_emails";
    }
}
