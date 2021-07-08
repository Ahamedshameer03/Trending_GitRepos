package com.my_project.trending_github_repositories;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.my_project.trending_github_repositories.utility.Network_Listener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CloudActivity extends AppCompatActivity  implements  ReposAdapter.onclickListener{

    RecyclerView recyclerView;
    ReposAdapter adapter;
    RepoApi repoApi;

    ShimmerFrameLayout shimmerFrameLayout;

    SwipeRefreshLayout swipeRefreshLayout;
    List<RepoModel> data;

    Network_Listener networkListener = new Network_Listener();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Trending_Github_Repositories);
        getAPIData();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cloud);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Repositories");
        actionBar.setSubtitle("Cloud");

        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmerView);
        Log.d("Inside", "Main");
        init();


        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onRefresh() {
                if(recyclerView.getVisibility() == View.GONE &&
                        ConnectivityManager.CONNECTIVITY_ACTION == ConnectivityManager.EXTRA_NO_CONNECTIVITY){
                    swipeRefreshLayout.setRefreshing(false);
                }
                else{
                    if(!filtering){
                        init();
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }else
                        swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void getAPIData() {
        repoApi = RetrofitInstance.getRetrofit().create(RepoApi.class);

        repoApi.getrepositories().enqueue(new Callback<List<RepoModel>>() {
            @Override
            public void onResponse(Call<List<RepoModel>> call, Response<List<RepoModel>> response) {
                int size = response.body().size();
                if(size > 0){
                    Toast.makeText(CloudActivity.this, "List is not Empty ", Toast.LENGTH_LONG).show();
                    data = response.body();
                    setAdapter();

                }
                else{
                    Toast.makeText(CloudActivity.this, "List is Empty", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<RepoModel>> call, Throwable t) {
                Toast.makeText(CloudActivity.this, "FAILED  "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Add to Database
    ContentValues values = new ContentValues();
    public void onAddContent(List<RepoModel> data) {

        for(int i=0;i<data.size();i++){
            values.put(RepoProvider.NAME_R, data.get(i).name);
            values.put(RepoProvider.AUTHOR_R, data.get(i).author);
            values.put(RepoProvider.AVATAR_R, data.get(i).avatar);
            values.put(RepoProvider.DESCRIPTION_R, data.get(i).description);
            values.put(RepoProvider.LANGUAGE_R, data.get(i).language);
            values.put(RepoProvider.LANGUAGECOLR_R, data.get(i).languageColor);
            values.put(RepoProvider.STARS_R, data.get(i).stars);
            values.put(RepoProvider.FORKS_R, data.get(i).forks);
            values.put(RepoProvider.CUR_PER_STARS_R, data.get(i).currentPeriodStars);
            values.put(RepoProvider.URL_R, data.get(i).url);
            values.put(RepoProvider.USERNAME_R, data.get(i).username);
            values.put(RepoProvider.BUILTBY_R, String.valueOf(data.get(i).builtBy));

            Uri uri = getContentResolver().insert(RepoProvider.CONTENT_URI_URL, values);
        }

    }
    // Get from Database
    public void onGetContent(View view) {

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void init(){

        Log.d("Inside", "INIT");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(CloudActivity.this));
    }

    private void setAdapter() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.hideShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);

        //recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        Log.d("DATA_S", ""+data.size());
        recyclerView.setVisibility(View.VISIBLE);
        adapter = new ReposAdapter(CloudActivity.this, data, this);
        recyclerView.setAdapter(adapter);
        onAddContent(data);
    }

    boolean filtering = false;
    SearchView searchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuItem searchMenu = menu.findItem(R.id.search_menu);
        searchView = (SearchView) searchMenu.getActionView();
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

        // Local Mode Menu Item
        MenuItem localMode = menu.findItem(R.id.local_menu);
        localMode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                gotoLocal();
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    public void gotoLocal() {
        Intent intent = new Intent(this, LocalActivity.class);
        startActivity(intent);
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

    @Override
    public void onClick(int position) {
        String url = data.get(position).getUrl();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        (recyclerView.getContext()).startActivity(browserIntent);
    }
}