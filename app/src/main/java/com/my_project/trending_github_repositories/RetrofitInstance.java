package com.my_project.trending_github_repositories;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String url = "https://private-anon-884e955f75-githubtrendingapi.apiary-mock.com/";

    public static Retrofit getRetrofit() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static void setRetrofit(Retrofit retrofit) {
        RetrofitInstance.retrofit = retrofit;
    }
}
