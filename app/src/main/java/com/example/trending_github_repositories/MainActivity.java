package com.example.trending_github_repositories;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView author, name, description, language, stars, languageColor, imageUrl, username;

    ImageView avatar;

    RecyclerView recyclerView;
    ReposAdapter adapter;
    RepoApi repoApi;

    SwipeRefreshLayout swipeRefreshLayout;
    List<RepoModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Inside", "Main");

        init();


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
                int size = response.body().size();
                if(size > 0){
                    Toast.makeText(MainActivity.this, "List is not Empty ", Toast.LENGTH_LONG).show();
                    data = response.body();
                    adapter = new ReposAdapter(MainActivity.this, data);
                    recyclerView.setAdapter(adapter);
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
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private  void init(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.searchAction);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter = new ReposAdapter(MainActivity.this, data);
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}