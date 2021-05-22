package com.example.recyclenev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class StartingActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button buttonUser,buttonOrg,buttonFaculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        auth= FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null && auth.getCurrentUser().isEmailVerified())
        {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            String type=pref.getString("user",null);
            if(type!=null) {
                if (type.equals("User"))
                    startActivity(new Intent(StartingActivity.this, MainActivityUser.class));
                else if (type.equals("Faculty"))
                    startActivity(new Intent(StartingActivity.this, MainActivityFaculty.class));
                else if (type.equals("Org"))
                    startActivity(new Intent(StartingActivity.this, MainActivity.class));
            }
        }

        buttonUser=findViewById(R.id.buttonUserLogin);
        buttonOrg=findViewById(R.id.buttonOrgLogin);
        buttonFaculty=findViewById(R.id.buttonFacultyLogin);

        buttonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartingActivity.this,LoginUser.class));
            }
        });
        buttonOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartingActivity.this,login.class));
            }
        });
        buttonFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartingActivity.this,FacultyLogin.class));
            }
        });
    }
}
