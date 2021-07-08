package com.my_project.trending_github_repositories;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.my_project.trending_github_repositories.utility.Internet;

import java.util.ArrayList;
import java.util.List;

public class LocalActivity extends AppCompatActivity implements ReposAdapter.onclickListener {

    RecyclerView recyclerView;
    List<RepoModel> data;
    ReposAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setTheme(R.style.Theme_Trending_Github_Repositories);
        setContentView(R.layout.activity_local);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Repositories");
        actionBar.setSubtitle("Local");

        getData();
    }

    private void getData() {

        data = new ArrayList<RepoModel>();
        Cursor cursor = getContentResolver().query(RepoProvider.CONTENT_URI_LIST, null, null, null, null);

        while(cursor.moveToNext()){
            RepoModel repomodel = new RepoModel();

            repomodel.name = cursor.getString(0);
            repomodel.language = cursor.getString(1);
            repomodel.languageColor = cursor.getString(2);
            repomodel.author = cursor.getString(3);
            repomodel.avatar = cursor.getString(4);
            repomodel.username = cursor.getString(5);
            repomodel.url = cursor.getString(6);
            repomodel.stars = Integer.parseInt(cursor.getString(8));
            repomodel.forks = Integer.parseInt(cursor.getString(9));
            repomodel.currentPeriodStars = Integer.parseInt(cursor.getString(10));
            repomodel.description = cursor.getString(11);

            data.add(repomodel);
        }


        init();
    }
    public  void init(){

        Log.d("Inside", "INIT");
        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setAdapter();
    }

    private void setAdapter() {
        adapter = new ReposAdapter(this, data, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(int position) {
        String url = data.get(position).getUrl();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        (recyclerView.getContext()).startActivity(browserIntent);
    }

    boolean filtering = false;
    SearchView searchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cloud_menu, menu);
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

        // Cloud Mode Menu Item
        MenuItem cloudMode = menu.findItem(R.id.cloud_menu_icon);
        cloudMode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                gotoHome();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void gotoHome() {
        Context context = this;
        if(!Internet.isConnected(this)){
            AlertDialog.Builder ADBuilder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.no_internet_dialog, null);
            ADBuilder.setView(view);

            AlertDialog dialog = ADBuilder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);

            AppCompatButton button_retry = view.findViewById(R.id.Retry_Button);
            button_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    gotoHome();
                }
            });

            AppCompatButton button_back = view.findViewById(R.id.Back_Button);
            button_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        else{
            Intent intent = new Intent(this, CloudActivity.class);
            startActivity(intent);
        }
    }
}