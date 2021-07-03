package com.example.trending_github_repositories;

public class BuiltBy {
    String href, avatar, username;

    public BuiltBy(String href, String avatar, String username) {
        this.href = href;
        this.avatar = avatar;
        this.username = username;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
