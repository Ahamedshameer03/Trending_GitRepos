package com.example.trending_github_repositories;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String url = "https://ghapi.huchen.dev/";
    TextView author, name, description, language, stars, languageColor, imageUrl, username;

    ImageView avatar;
    RepoApi repoApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* avatar = (ImageView) findViewById(R.id.avatar);

        author = (TextView) findViewById(R.id.author);
        name = (TextView) findViewById(R.id.name);
        description = (TextView) findViewById(R.id.description);
        language = (TextView) findViewById(R.id.language);
        stars = (TextView) findViewById(R.id.stars);


        author.setText("");
        name.setText("");
        description.setText("");
        language.setText("");
        stars.setText("");
        languageColor.setText("");
        imageUrl.setText("");
        username.setText("");*/

        repoApi = RetrofitInstance.getRetrofit().create(RepoApi.class);

        repoApi.getrepositories().enqueue(new Callback<List<RepoModel>>() {
            @Override
            public void onResponse(Call<List<RepoModel>> call, Response<List<RepoModel>> response) {
                if(response.body().size() > 0){
                    Toast.makeText(MainActivity.this, "List is not Empty", Toast.LENGTH_LONG).show();
                    List<RepoModel> data = response.body();
                    int size = data.size();
                    if(size > 0)
                        for(int i=0;i<size;i++){
                            author.append(data.get(i).getAuthor());
                            imageUrl.append(data.get(i).getImageUrl());
                            name.append(data.get(i).getName());
                            description.append(data.get(i).getDescription());
                            language.append(data.get(i).getLanguage());
                            languageColor.append(data.get(i).getLanguageColor());
                            stars.append(""+data.get(i).getStars());

                        }
                }
                else{
                    Toast.makeText(MainActivity.this, "List is Empty", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<RepoModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "FAILED  "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("Retrofit",retrofit.toString());
        RepoApi api = retrofit.create(RepoApi.class);

        Call<List<RepoModel>> call = api.getmodels();
        call.enqueue(new Callback<List<RepoModel>>() {
            @Override
            public void onResponse(Call<List<RepoModel>> call, Response<List<RepoModel>> response) {
                List<RepoModel> data = response.body();
                int size = data.size();
                Log.d("Size", " "+size);
                String json = gson.toJson(data.get(0));
                Log.d("JSON-Data", json);
                if(size > 0)
                    for(int i=0;i<size;i++){
                        json = gson.toJson(data.get(i));
                        Log.d("JSON-Data", json);
                        author.append(data.get(i).getAuthor());
                        imageUrl.append(data.get(i).getImageUrl());
                        name.append(data.get(i).getName());
                        description.append(data.get(i).getDescription());
                        language.append(data.get(i).getLanguage());
                        languageColor.append(data.get(i).getLanguageColor());
                        stars.append(""+data.get(i).getStars());

                    }
            }

            @Override
            public void onFailure(Call<List<RepoModel>> call, Throwable t) {
                Log.d("Failed", "final");
            }
        });*/
    }
}