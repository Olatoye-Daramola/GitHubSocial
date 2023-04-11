package io.olatoye.githubsocial.utils;

import org.springframework.beans.factory.annotation.Value;

public class Constants {

    @Value("${github_url}")
    private static String githubUrl;

    public static String getFollowersUrl(String username) {
        return githubUrl + username + "/followers";
    }

    public static String getFollowingUrl(String username) {
        return githubUrl + username + "/following";
    }

    public static String getUserDetailsUrl(String username) {
        return githubUrl + username;
    }

    public static String getUserEmailUrl(String username) {
        return githubUrl + username + "/public_emails";
    }
}
