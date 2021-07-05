package com.example.trending_github_repositories;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.trending_github_repositories.utility.Network_Listener;

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

    Network_Listener networkListener = new Network_Listener();

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onRefresh() {
                if(!filtering){
                    init();
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }else
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void init(){
        Log.d("Inside", "INIT");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

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

    }

    boolean filtering = false;
    SearchView searchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.searchAction);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filtering = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filtering = true;
                adapter.getFilter().filter(s);

                return false;
            }

        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFocusChange(View view, boolean b) {
                init();
                filtering = false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkListener);
        super.onStop();
    }
}