package io.olatoye.githubsocial.utils;

import org.springframework.beans.factory.annotation.Value;

import java.util.function.Function;

public class Constants {

    @Value("${github_url}")
    private static String githubUrl;


    public static Function<String, String> getFollowersUrl = username -> githubUrl + username + "/followers";

    public static Function<String, String> getFollowingUrl = username -> githubUrl + username + "/following";

    public static Function<String, String> getUserDetailsUrl = username -> githubUrl + username;
}
