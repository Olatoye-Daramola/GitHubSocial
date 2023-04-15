package io.olatoye.githubsocial.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component
public class Constants {

    @Value("${github_url}")
    private String githubUrl;


    public Function<String, String> getFollowersUrl = username -> githubUrl + username + "/followers";

    public Function<String, String> getFollowingUrl = username -> githubUrl + username + "/following";

    public Function<String, String> getUserDetailsUrl = username -> githubUrl + username;
}
