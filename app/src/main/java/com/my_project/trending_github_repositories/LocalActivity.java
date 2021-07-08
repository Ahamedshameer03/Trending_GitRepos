package com.my_project.trending_github_repositories;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LocalActivity extends AppCompatActivity implements ReposAdapter.onclickListener {

    RecyclerView recyclerView;
    List<RepoModel> data;
    ReposAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

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
}