package com.example.recyclenev;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Logo extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    ImageView logoView;
    Button choose, upload;
    String downloadUriDp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        logoView = (ImageView) findViewById(R.id.logoView);
        choose = (Button) findViewById(R.id.choose);
        upload = (Button) findViewById(R.id.Upload);


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFileChooser();

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String tym_stamp = String.valueOf(System.currentTimeMillis());

                String filePathAndName = "Status/" + "Status_" + tym_stamp;
                StorageReference ref =FirebaseStorage.getInstance().getReference().child(filePathAndName);
                ref.putFile(Uri.parse(String.valueOf(imageUri))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask =taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        downloadUriDp = uriTask.getResult().toString();

                        if(uriTask.isSuccessful()){
                            Intent intent = new Intent();
                            intent.putExtra("URI", downloadUriDp);
                            setResult(2, intent);
                            finish();
                        }
                    }
                });

            }
        });
    }

    public void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();

            Glide.with(Logo.this).asBitmap().load(imageUri).into(logoView);

        }
    }
}
