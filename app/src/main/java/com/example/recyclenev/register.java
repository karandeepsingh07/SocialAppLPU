package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class register extends AppCompatActivity {

    EditText username, email, password;
    Button register_button;
    FirebaseAuth auth;
    DatabaseReference Datareference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username1);
        email = findViewById(R.id.email1);
        password = findViewById(R.id.password1);
        register_button=findViewById(R.id.register1);
        auth = FirebaseAuth.getInstance();

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username1=username.getText().toString();
                String emaii1=email.getText().toString();

                String password1=password.getText().toString();
                if(TextUtils.isEmpty(username1)|| TextUtils.isEmpty(emaii1) || TextUtils.isEmpty(password1))
                {
                    Toast.makeText(register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(!emaii1.contains("@"))
                {
                    Toast.makeText(register.this, "envalid email", Toast.LENGTH_SHORT).show();
                }
                else if(password1.length()<6)
                {
                    Toast.makeText(register.this, "Password must be atleast 6 character", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    registerfun(username1,emaii1,password1);
                }
            }
        });

    }

    public void registerfun(final String user, final String email, String pass)
    {
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    final FirebaseUser firebaseUser= auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid=firebaseUser.getUid();

                    Datareference= FirebaseDatabase.getInstance().getReference("Organization").child(userid);

                    HashMap<String, String> hashMap=new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username",user);
                    hashMap.put("email",email);

                    Datareference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Intent intent=new Intent(register.this,orgProfile.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("usernamr",user);
                                intent.putExtra("email",email);
                                intent.putExtra("uid",firebaseUser.getUid());
                                startActivity(intent);
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(register.this, "you cant register with this email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
