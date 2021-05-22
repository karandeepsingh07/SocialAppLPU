package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
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

public class SignUpFaculty extends AppCompatActivity {

    EditText FacultyName, Facultyemail, Facultypassword,FacultyRegistrationId, FacultySeating;
    Button register_button;
    FirebaseAuth auth;
    Spinner FacultyDepartments;
    DatabaseReference datareference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_faculty);

        FacultyName=(EditText)findViewById(R.id.etUsernameFacultyS);
        Facultyemail = (EditText)findViewById(R.id.etEmailFacultyS);
        Facultypassword = (EditText)findViewById(R.id.etPasswordFacultyS);
        FacultyRegistrationId=(EditText)findViewById(R.id.etRegistrationIdFacultyS);
        FacultySeating =(EditText)findViewById(R.id.etSeetingFacultyS);
        register_button=(Button)findViewById(R.id.btSignup);
        FacultyDepartments =(Spinner) findViewById(R.id.FacultyDepartmentList);
        auth= FirebaseAuth.getInstance();
        datareference= FirebaseDatabase.getInstance().getReference().child("Faculty").child("Detail");
        progressBar=findViewById(R.id.progressBar);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(FacultyName.getText().toString())|| TextUtils.isEmpty(Facultyemail.getText().toString()) || TextUtils.isEmpty(Facultypassword.getText().toString())||TextUtils.isEmpty(FacultyRegistrationId.getText().toString())||TextUtils.isEmpty(FacultySeating.getText().toString())|| TextUtils.isEmpty(FacultyDepartments.getSelectedItem().toString()))
                {
                    Toast.makeText(SignUpFaculty.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(!Facultyemail.getText().toString().contains("@") || !Facultyemail.getText().toString().contains(".com"))
                {
                    Toast.makeText(SignUpFaculty.this, "please, enter valid email address", Toast.LENGTH_SHORT).show();
                }
                else if(Facultypassword.getText().toString().length()<6)
                {
                    Toast.makeText(SignUpFaculty.this, "Password must be atleast 6 character", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    registerfun(FacultyName.getText().toString(),Facultyemail.getText().toString(),Facultypassword.getText().toString(),FacultyRegistrationId.getText().toString(),FacultySeating.getText().toString(),FacultyDepartments.getSelectedItem().toString());
                }
            }
        });
    }
    public void registerfun(String name, String Email, final String pass,String regId,String seating,String department)
    {
        progressBar.setVisibility(View.VISIBLE);
        final HashMap<String,String> FacultyMap = new HashMap<>();

        FacultyMap.put("altEmail",Email);
        FacultyMap.put("uid",regId);
        FacultyMap.put("name",name);
        FacultyMap.put("contact",seating);
        FacultyMap.put("department",department);

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
                                Toast.makeText(SignUpFaculty.this, "Register Successfully.. Please check your email for varification", Toast.LENGTH_SHORT).show();
                                String name=FacultyName.getText().toString();
                                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                datareference.child(uid).setValue(FacultyMap);
                                FacultyName.setText("");
                                Facultyemail.setText("");

                                progressBar.setVisibility(View.GONE);
                                Intent intent=new Intent(SignUpFaculty.this,FacultyLogin.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(SignUpFaculty.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUpFaculty.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
