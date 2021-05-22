package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    EditText organizarionName, email, password,tier, ceo, domain, contacts, CocCeo, CulSec, description,uid;
    Button register_button;
    CircleImageView organizationDp;
    FirebaseAuth auth;
    String imageUri;
    DatabaseReference datareference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        organizarionName=(EditText)findViewById(R.id.etORGName);
        email = (EditText)findViewById(R.id.etorgEmail);
        password = (EditText)findViewById(R.id.etorgPassword);
        tier =(EditText)findViewById(R.id.etorgTier);
        ceo =(EditText)findViewById(R.id.etNameCEO);
        domain =(EditText)findViewById(R.id.etorgDomain);
        CocCeo =(EditText)findViewById(R.id.etCoCeoName);
        CulSec =(EditText)findViewById(R.id.etCultSecName);
        description =(EditText)findViewById(R.id.etorgDescription);
        contacts=(EditText)findViewById(R.id.etorgContact) ;
        organizationDp =(CircleImageView)findViewById(R.id.updateLogoORG);
        progressBar=findViewById(R.id.progressBar);

        register_button=(Button)findViewById(R.id.btSignup);
        auth= FirebaseAuth.getInstance();
        datareference = FirebaseDatabase.getInstance().getReference().child("Organisation").child("Detail");




        organizationDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Logo.class);
                startActivityForResult(intent, 2);
            }
        });



        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(organizarionName.getText().toString())|| TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())|| TextUtils.isEmpty(ceo.getText().toString())||TextUtils.isEmpty(CocCeo.getText().toString())|| TextUtils.isEmpty(CulSec.getText().toString())||TextUtils.isEmpty(contacts.getText().toString())|| TextUtils.isEmpty(description.getText().toString())||TextUtils.isEmpty(tier.getText()))
                {
                    Toast.makeText(SignUp.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(!email.getText().toString().contains("@") || !email.getText().toString().contains(".com"))
                {
                    Toast.makeText(SignUp.this, "please, enter valid email address", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().length()<6)
                {
                    Toast.makeText(SignUp.this, "Password must be atleast 6 character", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(tier.getText().toString())>5){
                    Toast.makeText(SignUp.this, "Tier should be less than 6", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    registerfun(organizarionName.getText().toString(),email.getText().toString(),password.getText().toString(),ceo.getText().toString(),CocCeo.getText().toString(),CulSec.getText().toString(),domain.getText().toString(),contacts.getText().toString(),description.getText().toString(),tier.getText().toString());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2){
            imageUri = data.getStringExtra("URI");
            Glide.with(SignUp.this).asBitmap().load(imageUri).into(organizationDp);
            Toast.makeText(this, "Logo updation successful", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, imageUri, Toast.LENGTH_LONG).show();
        }
    }

    public void registerfun(String name, String Email, final String pass, String CEO, String COCEO, String CULSEC, String DOMAIN, String CONTACT, String DESC, String TIER)
    {

        progressBar.setVisibility(View.VISIBLE);
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
                                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final HashMap<String,String> hashMap =new HashMap<>();
                                hashMap.put("ceo",ceo.getText().toString());
                                hashMap.put("coCeo",CocCeo.getText().toString());
                                hashMap.put("contact", contacts.getText().toString());
                                hashMap.put("culSec",CulSec.getText().toString());
                                hashMap.put("domain",domain.getText().toString());
                                hashMap.put("tier",tier.getText().toString());
                                hashMap.put("title",organizarionName.getText().toString());
                                hashMap.put("uid",uid);
                                hashMap.put("imageUrl",imageUri);
                                hashMap.put("imageUrlCeo","");
                                hashMap.put("imageUrlCoCeo","");
                                hashMap.put("imageUrlCulsec","");
                                hashMap.put("about",description.getText().toString());

                                Toast.makeText(SignUp.this, "Register Successfully.. Please check your email for varification", Toast.LENGTH_SHORT).show();

                                datareference.child(uid).setValue(hashMap);
                                organizarionName.setText("");
                                email.setText("");
                                password.setText("");
                                ceo.setText("");
                                CocCeo.setText("");
                                CulSec.setText("");
                                domain.setText("");
                                contacts.setText("");
                                description.setText("");

                                progressBar.setVisibility(View.GONE);
                                Intent intent=new Intent(SignUp.this,login.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
