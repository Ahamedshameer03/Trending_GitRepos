package com.example.trending_github_repositories;

import java.util.ArrayList;

public class RepoModel {
    String author, name, description, language, languageColor, avatar, username, url;
    int stars, forks, currentPeriodStars;

    ArrayList<BuiltBy> builtBy = new ArrayList<BuiltBy>();

    public RepoModel(String author,
                     String name,
                     String description,
                     String language,
                     String languageColor,
                     String avatar,
                     String username,
                     String url,
                     int stars,
                     int forks,
                     int currentPeriodStars,
                     ArrayList<BuiltBy> builtBy) {
        this.author = author;
        this.name = name;
        this.description = description;
        this.language = language;
        this.languageColor = languageColor;
        this.avatar = avatar;
        this.username = username;
        this.url = url;
        this.stars = stars;
        this.forks = forks;
        this.currentPeriodStars = currentPeriodStars;

        // BuiltBy
        this.builtBy = builtBy;
        /*this.builtBy = new BuiltBy[builtBy.length];
        for(int i=0;i< builtBy.length;i++){
            this.builtBy[i] = builtBy[i];
        }*/
    }

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

    public ArrayList<BuiltBy> getBuiltBy() {
        return builtBy;
    }

    public void setBuiltBy(ArrayList<BuiltBy> builtBy) {
        this.builtBy = builtBy;
    }


    public RepoModel() {
        author = "author";
        name = "name";
        description = "description";
        language = "language";
        languageColor = "languageColor";
        avatar = "imageUrl";
        username = "username";
        url = "url";
        stars = 100;
        forks = 200;
        currentPeriodStars = 150;

        // builtBy
        BuiltBy builtBy = new BuiltBy("href", "avatar", "username");
        this.builtBy.add(builtBy);
        /*builtBy = new BuiltBy[1];
        builtBy[0].avatar = "avatar";
        builtBy[0].href = "href";
        builtBy[0].username = "username";*/
    }


}
