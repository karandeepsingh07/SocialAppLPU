package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

public class UserDetailActivity extends AppCompatActivity implements  ItemListDialogFragment.ItemClickListener{

    EditText editTextName,editTextregNo,editTextDepartment,editTextAltEmail,editTextContact;
    String name,regNo,department,altEmail,contact,imageViewUrl;
    FirebaseDatabase database;
    DatabaseReference reference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    CircleImageView imageView;
    EditText editTextDummy;
    Button button;
    FirebaseAuth auth;
    FirebaseUser user;
    String uid;
    Boolean opened=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        uid = user.getUid();

        editTextName=findViewById(R.id.editTextUserName);
        editTextregNo=findViewById(R.id.editTextUserReg);
        editTextDepartment=findViewById(R.id.editTextUserDepartment);
        editTextAltEmail=findViewById(R.id.editTextUserAltEmail);
        editTextContact=findViewById(R.id.editTextUserContact);
        imageView=findViewById(R.id.imageViewUser);
        button=findViewById(R.id.buttonSubmitDetails);

        database= FirebaseDatabase.getInstance();
        reference=database.getReference().child("User").child("Detail").child(uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserPC userPC=dataSnapshot.getValue(UserPC.class);
                name=userPC.getName();
                regNo=userPC.getRegNo();
                department=userPC.getDepartment();
                altEmail=userPC.getAltEmail();
                contact=userPC.getContact();
                imageViewUrl=userPC.getImageUrl();

                Glide.with(UserDetailActivity.this).load(imageViewUrl).into(imageView);
                editTextName.setText(name);
                editTextregNo.setText(regNo);
                editTextDepartment.setText(department);
                editTextAltEmail.setText(altEmail);
                editTextContact.setText(contact);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                opened=true;
                openFileChooser();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editTextName.getText().toString();
                uid = editTextregNo.getText().toString();
                department = editTextDepartment.getText().toString();
                altEmail = editTextAltEmail.getText().toString();
                contact = editTextContact.getText().toString();
                if(opened) {
                    String filePathAndName = "Posts/" + "post_";
                    StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                    ref.putFile(Uri.parse(imageViewUrl)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // image uploaded to firebase storage. get uri
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()) {
                                UserPC userPC = new UserPC();
                                userPC.setName(name);
                                userPC.setRegNo(regNo);
                                userPC.setDepartment(department);
                                userPC.setAltEmail(altEmail);
                                userPC.setContact(contact);
                                userPC.setImageUrl(downloadUri);

                                reference.setValue(userPC).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UserDetailActivity.this, "Status Uploaded", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserDetailActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    UserPC userPC = new UserPC();
                    userPC.setName(name);
                    userPC.setRegNo(regNo);
                    userPC.setDepartment(department);
                    userPC.setAltEmail(altEmail);
                    userPC.setContact(contact);
                    userPC.setImageUrl(imageViewUrl);
                }
            }
        });
    }

    public void editLayout(View view){
        final BottomSheetDialogFragment bottomSheetDialogFragment = new ItemListDialogFragment();
        editTextDummy=findViewById(view.getId());
        Bundle bundle = new Bundle();
        bundle.putString("data",editTextDummy.getText().toString());
        bottomSheetDialogFragment.setArguments(bundle);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
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
            imageViewUrl=imageUri.toString();

            Glide.with(UserDetailActivity.this).asBitmap().load(imageUri).into(imageView);

        }
    }
    @Override
    public void onItemClick(String item) {
        editTextDummy.setText(item);
    }
}
