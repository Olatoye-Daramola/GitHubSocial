package io.olatoye.githubsocial.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document
public class User extends GithubUser {

    @MongoId
    private String id;

    @JsonIgnore
    private String githubToken;

    private List<GithubUser> followers;
    private List<GithubUser> following;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGithubToken() {
        return githubToken;
    }

    public void setGithubToken(String githubToken) {
        this.githubToken = githubToken;
    }

    public List<GithubUser> getFollowers() {
        return followers;
    }

    public void setFollowers(List<GithubUser> followers) {
        this.followers = followers;
    }

    public List<GithubUser> getFollowing() {
        return following;
    }

    public void setFollowing(List<GithubUser> following) {
        this.following = following;
    }
}
