package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpUser extends AppCompatActivity {

    EditText StudentName, email, password, registrationId, contact;
    Spinner departments;
    Button register_button;
    FirebaseAuth auth;
    DatabaseReference datareference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        StudentName=(EditText)findViewById(R.id.etUsernameS);
        email = (EditText)findViewById(R.id.etEmailS);
        password = (EditText)findViewById(R.id.etPasswordS);
        register_button=(Button)findViewById(R.id.btSignup);
        registrationId =(EditText)findViewById(R.id.etRegistrationIdS);
        departments = (Spinner) findViewById(R.id.DepartmentList);
        contact = (EditText)findViewById(R.id.etContactS);
        progressBar=findViewById(R.id.progressBar);

        auth= FirebaseAuth.getInstance();
        datareference= FirebaseDatabase.getInstance().getReference().child("User").child("Detail");

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(StudentName.getText().toString())|| TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())|| TextUtils.isEmpty(registrationId.getText().toString())|| TextUtils.isEmpty(contact.getText().toString())|| TextUtils.isEmpty(departments.getSelectedItem().toString()))
                {
                    Toast.makeText(SignUpUser.this, "Password must be atleast 6 character", Toast.LENGTH_SHORT).show();
                }
                else if(!email.getText().toString().contains("@") || !email.getText().toString().contains(".com")){
                    Toast.makeText(SignUpUser.this, "please, enter valid email address", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    registerfun(StudentName.getText().toString(),email.getText().toString(),password.getText().toString(),registrationId.getText().toString(),contact.getText().toString(),departments.getSelectedItem().toString());
                }
            }
        });
    }
    public void registerfun(String name, String Email, final String pass, String regId, String contact,String SelectedDepartment)
    {
        progressBar.setVisibility(View.VISIBLE);
        final HashMap<String ,String> UserHashMap = new HashMap<String,String>();
        UserHashMap.put("altEmail",email.getText().toString());
        UserHashMap.put("name",name);
        UserHashMap.put("department",SelectedDepartment);
        UserHashMap.put("regNo",regId);
        UserHashMap.put("contact",contact);

        auth.createUserWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(SignUpUser.this, "Register Successfully.. Please check your email for varification", Toast.LENGTH_SHORT).show();
                                String name=StudentName.getText().toString();
                                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                datareference.child(uid).setValue(UserHashMap);
                                StudentName.setText("");
                                email.setText("");
                                password.setText("");

                                progressBar.setVisibility(View.GONE);
                                Intent intent=new Intent(SignUpUser.this,LoginUser.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(SignUpUser.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
