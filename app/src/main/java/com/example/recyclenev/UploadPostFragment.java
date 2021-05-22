package com.example.recyclenev;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.sql.BatchUpdateException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class UploadPostFragment extends Fragment {

    FirebaseAuth auth;
    EditText PUtitle, PUdescription,PUFormLink,PUdl,PUstartDate,PUendDate,PUfees,PUcontact;
    ImageView PUimage;
    Button uploadButton;
    Uri image_rui = null;
    DatabaseReference dbref;
    FirebaseUser user;
    View v;
    String orgImageUrl;
    String orgName;
    FirebaseDatabase databasePost;
    DatabaseReference reference;
    String time;
    Calendar calander;
    String uid,email;
    Spinner spinnerCategory;
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
        v = getLayoutInflater().inflate(R.layout.fragment_uploadpost,container,false);

        cameraPermission =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE};

        auth = FirebaseAuth.getInstance();
        PUtitle = (EditText)v.findViewById(R.id.postTitle);
        PUdescription =(EditText)v.findViewById(R.id.postDescription);
        PUimage = (ImageView)v.findViewById(R.id.postImage);
        uploadButton = (Button)v.findViewById(R.id.uploadButton);
        user = auth.getCurrentUser();
        PUFormLink =(EditText)v.findViewById(R.id.FormLink);
        PUdl =(EditText)v.findViewById(R.id.postDL);
        PUstartDate=(EditText)v.findViewById(R.id.postStartDate);
        PUendDate=(EditText)v.findViewById(R.id.postEndDate);
        PUfees=(EditText)v.findViewById(R.id.postFee);
        PUcontact=(EditText)v.findViewById(R.id.postContact);
        spinnerCategory=v.findViewById(R.id.postCategory);
        progressBar=v.findViewById(R.id.progressBar);

        uid = user.getUid();
        email = user.getEmail();
        databasePost = FirebaseDatabase.getInstance();
        reference =  databasePost.getReference();

        reference.child("Organisation").child("Detail").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrganisationPC organisationPC=dataSnapshot.getValue(OrganisationPC.class);
                orgImageUrl=organisationPC.getImageUrl();
                orgName=organisationPC.getTitle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //uid="CHECK_UID";
        //email="email@gmail.com";
        // get image from camera or galary on click
        PUimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show image pick dialog
                showImagePickDialog();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                String title = PUtitle.getText().toString().trim();
                String description = PUdescription.getText().toString().trim();
                String dl= PUdl.getText().toString().trim();
                String startDate=PUstartDate.getText().toString().trim();
                String endDate=PUendDate.getText().toString().trim();
                String fees = PUfees.getText().toString().trim();
                String formLink= PUFormLink.getText().toString().trim();
                String contact=PUcontact.getText().toString().trim();
                String category=spinnerCategory.getSelectedItem().toString();

                if(TextUtils.isEmpty(title))
                {
                    Toast.makeText(getContext(), "Enter title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(description))
                {
                    Toast.makeText(getContext(), "Enter description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(dl))
                {
                    Toast.makeText(getContext(), "Enter DL(Yes/No)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(startDate))
                {
                    Toast.makeText(getContext(), "Enter StartDate", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(endDate))
                {
                    Toast.makeText(getContext(), "Enter End Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(fees))
                {
                    Toast.makeText(getContext(), "Enter fees", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(formLink))
                {
                    Toast.makeText(getContext(), "Enter Form Link", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(contact))
                {
                    Toast.makeText(getContext(), "Enter Contact", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(spinnerCategory.getSelectedItemPosition()==0){
                    Toast.makeText(getContext(), "Select Category", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(image_rui == null)
                {
                    //post without image
                    uploadData(title,description,"No Image",dl,startDate,endDate,fees,formLink,contact,category);

                }
                else{
                    //post with image
                    uploadData(title,description,String.valueOf(image_rui),dl,startDate,endDate,fees,formLink,contact,category);
                }

            }
        });
        return v;
    }

    private void uploadData(final String title, final String description, String uri, final String dl, final String startDate, final String endDate, final String fees, final String formLink,final String contact,final String category) {
        final String tym_stamp = String.valueOf(System.currentTimeMillis());
        calander = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");

        time = simpleDateFormat.format(calander.getTime());

        String filePathAndName = "Posts/" + "post_" + tym_stamp;
        if(!uri.equals("noImage"))
        {
            // post wd image
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // image uploaded to firebase storage. get uri
                    Task<Uri> uriTask =taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isSuccessful());
                    String downloadUri = uriTask.getResult().toString();

                    if(uriTask.isSuccessful()){

                        // upload post to firebase database
                        HashMap<Object,String>hashMap = new HashMap<>();
                        hashMap.put("ppUid",uid);
                        hashMap.put("pUid",tym_stamp);
                        hashMap.put("eventName",title);
                        hashMap.put("eventDate",startDate);
                        hashMap.put("eventTime",time);
                        hashMap.put("eventFees",fees);
                        hashMap.put("eventDescription",description);
                        hashMap.put("eventLink",formLink);
                        hashMap.put("eventLastDate",endDate);
                        hashMap.put("eventDL",dl);
                        hashMap.put("eventImageUrl", downloadUri);
                        hashMap.put("eventContact",contact);
                        hashMap.put("eventLikes","0");
                        hashMap.put("eventUploadTime",time);
                        hashMap.put("organisationDP",orgImageUrl);
                        hashMap.put("organisationName",orgName);
                        hashMap.put("category",category);


                        // path to stored post data
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child("Details");
                        ref.child(uid).child(tym_stamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {   //change abc to timestamp
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Post Published", Toast.LENGTH_SHORT).show();
                                PUtitle.setText("");
                                PUdescription.setText("");
                                PUcontact.setText("");
                                PUdl.setText("");
                                PUendDate.setText("");
                                PUstartDate.setText("");
                                PUfees.setText("");
                                PUFormLink.setText("");
                                spinnerCategory.setSelection(0);
                                PUimage.setImageURI(null);
                                image_rui = null;
                                getFragmentManager().beginTransaction().remove(UploadPostFragment.this).commit();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        else{
            // post withoutdout image

            final HashMap<Object,String>hashMap = new HashMap<>();
            hashMap.put("ppUid",uid);
            hashMap.put("pUid",tym_stamp);
            hashMap.put("eventName",title);
            hashMap.put("eventDate",startDate);
            hashMap.put("eventTime",time);
            hashMap.put("eventFees",fees);
            hashMap.put("eventDescription",description);
            hashMap.put("eventLink",formLink);
            hashMap.put("eventLastDate",endDate);
            hashMap.put("eventDL",dl);
            hashMap.put("eventImageUrl", "No_Image");
            hashMap.put("eventContact",contact);
            hashMap.put("eventLikes","0");
            hashMap.put("eventUploadTime",time);
            hashMap.put("organisationDP",orgImageUrl);
            hashMap.put("organisationName",orgName);
            hashMap.put("category",category);

            // path to stored post data
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            ref.child("Posts").child("Details").child(uid).child(tym_stamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    ref.child("Organisation").child("Posts").child(uid).child(tym_stamp).setValue(hashMap);
                    Toast.makeText(getContext(), "Post Published", Toast.LENGTH_SHORT).show();
                    //reset views
                    PUtitle.setText("");
                    PUdescription.setText("");
                    PUcontact.setText("");
                    PUdl.setText("");
                    PUendDate.setText("");
                    PUstartDate.setText("");
                    PUfees.setText("");
                    PUFormLink.setText("");
                    PUimage.setImageURI(null);
                    spinnerCategory.setSelection(0);
                    image_rui = null;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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
        boolean result = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);
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

                PUimage.setImageURI(image_rui);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE)
            {
                PUimage.setImageURI(image_rui);
            }
        }
    }
}

