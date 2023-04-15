package io.olatoye.githubsocial.utils;

import io.olatoye.githubsocial.domain.GithubUser;
import io.olatoye.githubsocial.domain.User;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;

public class Utils {

    public static ResponseEntity<String> callApi(String githubToken, String baseUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + githubToken);
        headers.add("Accept", "application/vnd.github+json");
        headers.add("X-GitHub-Api-Version", "2022-11-28");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        RestTemplate template = new RestTemplate();
        return template.exchange(baseUrl, HttpMethod.GET, entity, String.class);
    }

    public static GithubUser parseJsonToUser(String jsonString) {
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
