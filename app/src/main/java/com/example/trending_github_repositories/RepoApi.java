package com.example.trending_github_repositories;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RepoApi {

    @GET("repositories")
    Call<List<RepoModel>> getrepositories();
}
