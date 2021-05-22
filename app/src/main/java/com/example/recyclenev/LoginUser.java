package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginUser extends AppCompatActivity {

    EditText emailL,PasswordL;
    Button login;
    FirebaseAuth auth;
    ImageButton imbtn;
    TextView tvforget;
    boolean toRestrictUpdateActivity = true;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        emailL = (EditText)findViewById(R.id.editemail);
        PasswordL = (EditText)findViewById(R.id.editpassword);
        login = (Button)findViewById(R.id.btLoginL);
        auth= FirebaseAuth.getInstance();
        imbtn= (ImageButton)findViewById(R.id.imgbtn);
        tvforget=(TextView)findViewById(R.id.tvForgot);
        relativeLayout = (RelativeLayout)findViewById(R.id.rltv);
        progressBar=findViewById(R.id.progressBar);


        if(auth.getCurrentUser()!=null && auth.getCurrentUser().isEmailVerified())
        {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("user","User");
            editor.apply();
            startActivity(new Intent(LoginUser.this,MainActivityUser.class));
        }

        imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginUser.this,SignUpUser.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(emailL.getText().toString(),PasswordL.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            if(auth.getCurrentUser().isEmailVerified())
                            {
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user","User");
                                editor.apply();
                                progressBar.setVisibility(View.GONE);
                                Intent intent=new Intent(LoginUser.this,MainActivityUser.class);
                                startActivity(intent);

                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginUser.this, "please.. verify your email address", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

    }
    public void foregetpassword(View VIEW)
    {
        Intent i=new Intent(LoginUser.this,ForgotPass.class);
        startActivity(i);

    }
}
