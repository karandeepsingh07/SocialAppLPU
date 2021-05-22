package com.example.recyclenev;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class UpdateStatusFragment extends Fragment {

    ImageView imageView;
    Uri image_rui = null;
    FirebaseAuth auth;
    String uid,username,userImage;
    FirebaseUser user;
    ImageButton buttonChoose,buttonUpload;
    ProgressBar progressBar;

    private  static  final int CAMERA_REQUEST_CODE = 100;
    private  static  final int STORAGE_REQUEST_CODE = 200;


    private  static  final int IMAGE_PICK_CAMERA_CODE = 300;
    private  static  final int IMAGE_PICK_GALLERY_CODE = 400;


    String[] cameraPermission;
    String[] storagePermission;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_updatestatus,container,false);
        imageView=v.findViewById(R.id.statusImage);
        buttonChoose=v.findViewById(R.id.statusChooseImageButton);
        buttonUpload=v.findViewById(R.id.statusUploadImageButton);
        progressBar =v.findViewById(R.id.progressBar);

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                uploadImage();
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        uid = user.getUid();
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        username = pref.getString("Username",null);
        userImage=pref.getString("Userimage",null);
        return v;
    }


    public void uploadImage(){
        String uri=String.valueOf(image_rui);
        final String tym_stamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post_" + tym_stamp;

        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
        ref.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploaded to firebase storage. get uri
                Task<Uri> uriTask =taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                String downloadUri = uriTask.getResult().toString();

                if(uriTask.isSuccessful()){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

                    String currentDateTimeString = simpleDateFormat.format(new Date());
                    Date date1 = null;
                    try {
                        date1 = simpleDateFormat.parse(currentDateTimeString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Status").child(uid).child(tym_stamp);
                    StatusPC statusPC=new StatusPC();
                    statusPC.setStatusUrl(downloadUri);
                    statusPC.setUsername(username);
                    statusPC.setUserImage(userImage);
                    statusPC.setUploadTime(String.valueOf(date1.getTime()));
                    reference.setValue(statusPC).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Status Uploaded", Toast.LENGTH_SHORT).show();
                            getFragmentManager().beginTransaction().remove(UpdateStatusFragment.this).commit();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showImagePickDialog()
    {
        //option(camera, gallary ) to show dialog.

        String[] option = {"Camera","Gallery"};
        //dialog
        AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        builder.setTitle("Choose image from");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //item click handle
                if(which==0){
                    // camere clicked
                    if(!checkCameraPermission())
                    {
                        requestCameraPermission();
                    }
                    else{
                        pickFromCAmera();
                    }

                }
                if(which == 1)
                {
                    // gallery clicked
                    if(!checkStroragePermission())
                    {
                        requestStoragePermission();
                    }
                    else{

                        pickFromGallery();
                    }
                }
            }
        });
        // create and show dialog
        builder.create().show();
    }

    private void pickFromCAmera() {
        ContentValues cv =new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Desc");
        image_rui = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);

    }

    private void pickFromGallery() {

        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }


    public boolean checkStroragePermission()
    {
        // check storage permission
        // retirn true if enable otherwise false
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission()
    {
        // request runtime storage permission
        ActivityCompat.requestPermissions(getActivity(), storagePermission,STORAGE_REQUEST_CODE);
    }


    public boolean checkCameraPermission()
    {
        // check camera permission
        // retirn true if enable otherwise false
        boolean result = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)== (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);


        return result1 && result;

    }

    private void requestCameraPermission()
    {
        // request runtime camera permission
        ActivityCompat.requestPermissions(getActivity(), cameraPermission,CAMERA_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)  {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0)
                {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean stoarageAccepted = grantResults[1]== PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && stoarageAccepted)
                    {
                        pickFromCAmera();
                    }else
                    {
                        Toast.makeText(getContext(), "camera and storage both permission are necessary...", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                }

            }
            break;
            case STORAGE_REQUEST_CODE:
            {
                if(grantResults.length>0)
                {
                    boolean storageAccepted = grantResults[0] ==  PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(getContext(), "Storage permission necessary", Toast.LENGTH_SHORT).show();
                    }
                }
                else{}
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                image_rui= data.getData();

                imageView.setImageURI(image_rui);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE)
            {
                imageView.setImageURI(image_rui);
            }
        }
    }
}
