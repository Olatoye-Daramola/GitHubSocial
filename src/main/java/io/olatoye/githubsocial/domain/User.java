package io.olatoye.githubsocial.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document
public class User extends GithubUser {

    private String githubToken;
    private List<GithubUser> followers;
    private List<GithubUser> following;

    public User() {
        super();
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
