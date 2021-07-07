package com.my_project.trending_github_repositories;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RepoApi {

    @GET("repositories")
    Call<List<RepoModel>> getrepositories();
}
