package io.olatoye.githubsocial.domain;

public class GithubUser {
    private long githubUserId;
    private String username;
    private String email;
    private String avatar;
    private String profile;
    private String repository;

    public GithubUser() {
    }

    public long getGithubUserId() {
        return githubUserId;
    }

    public void setGithubUserId(long githubUserId) {
        this.githubUserId = githubUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }
}
