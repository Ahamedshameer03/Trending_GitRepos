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
        Cursor cursor = getContentResolver().query(RepoProvider.CONTENT_URI_LIST, null, null, null, "_id");

        while(cursor.moveToNext()){
            RepoModel repomodel = new RepoModel();

            repomodel.name = cursor.getString(1);
            repomodel.language = cursor.getString(2);
            repomodel.languageColor = cursor.getString(3);
            repomodel.author = cursor.getString(4);
            repomodel.avatar = cursor.getString(5);
            repomodel.username = cursor.getString(6);
            repomodel.url = cursor.getString(7);
            repomodel.stars = Integer.parseInt(cursor.getString(9));
            repomodel.forks = Integer.parseInt(cursor.getString(10));
            repomodel.currentPeriodStars = Integer.parseInt(cursor.getString(11));
            repomodel.description = cursor.getString(12);

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