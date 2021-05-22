package com.example.recyclenev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class status_description extends AppCompatActivity {
    ImageView statusimg;
    TextView username;
    String userImage;
    CircleImageView logo;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_description);

        statusimg=findViewById(R.id.Fullstatus);
        username=findViewById(R.id.statusDescriptionText);
        logo=findViewById(R.id.statusDescriptionImage);
        progressBar=(ProgressBar)findViewById(R.id.progress1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus<100)
                {
                    progressStatus+=1;
                    if(progressStatus==99)
                    {
                        finish();
                    }

                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });

                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        Intent i=getIntent();
        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        userImage=pref.getString("Userimage",null);
        username.setText(i.getStringExtra("username"));
        Glide.with(this).load(userImage).into(logo);
        Glide.with(this).load(i.getStringExtra("image")).into(statusimg);


    }

    public void back(View view) {
        finish();
    }
}
