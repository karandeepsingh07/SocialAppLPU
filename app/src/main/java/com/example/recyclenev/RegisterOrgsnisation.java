package com.example.recyclenev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterOrgsnisation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_orgsnisation);
    }

    public void organization(View view) {
        Intent intent= new Intent(RegisterOrgsnisation.this,register.class);
        startActivity(intent);
    }
}
