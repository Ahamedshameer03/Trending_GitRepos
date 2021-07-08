package com.my_project.trending_github_repositories.utility;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import com.my_project.trending_github_repositories.R;

public class Network_Listener extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Internet.isConnected(context)) {
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
                    onReceive(context, intent);

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
    }
}
