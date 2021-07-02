package com.example.trending_github_repositories;

import java.util.ArrayList;
import java.util.List;

public class RepoModel {
    String author, name, description, language, languageColor, imageUrl, username, url;
    int stars, forks, currentPeriodStars;
    //List<BuildBy> buildBy;

    /*private class BuildBy {
        public BuildBy(String href, String avatar, String username) {
            this.href = href;
            this.avatar = avatar;
            this.username = username;
        }

        String href, avatar, username;
    }*/

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageColor() {
        return languageColor;
    }

    public void setLanguageColor(String languageColor) {
        this.languageColor = languageColor;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getCurrentPeriodStars() {
        return currentPeriodStars;
    }

    public void setCurrentPeriodStars(int currentPeriodStars) {
        this.currentPeriodStars = currentPeriodStars;
    }

    /*public List<BuildBy> getBuildBy() {
        return buildBy;
    }

    public void setBuildBy(BuildBy buildBy) {
        this.buildBy.add(buildBy);
    }*/

    public RepoModel(String author, String name, String description, String language, String languageColor, String imageUrl, String username, String url, int stars, int forks, int currentPeriodStars) {
        this.author = author;
        this.name = name;
        this.description = description;
        this.language = language;
        this.languageColor = languageColor;
        this.imageUrl = imageUrl;
        this.username = username;
        this.url = url;
        this.stars = stars;
        this.forks = forks;
        this.currentPeriodStars = currentPeriodStars;
        //this.buildBy.add(buildBy);
    }
    public RepoModel() {
        author = "author";
        name = "name";
        description = "description";
        language = "language";
        languageColor = "languageColor";
        imageUrl = "imageUrl";
        username = "username";
        url = "url";
        stars = 100;
        forks = 200;
        currentPeriodStars = 150;
        //buildBy.add(new BuildBy("buildByHref","buildByAvatar", "buildByUsername"));

    }

}
