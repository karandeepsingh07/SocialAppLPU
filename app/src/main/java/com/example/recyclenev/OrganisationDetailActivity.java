package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ListMenuItemView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OrganisationDetailActivity extends AppCompatActivity implements ItemListDialogFragment.ItemClickListener{

    FirebaseDatabase database;
    DatabaseReference reference;
    EditText editTextName,editTextAbout,editTextCEO,editTextCoCEO,editTextCulSec,editTextContact,editTextTier,editTextDomain;
    String name,about,ceo,coCeo,culSec,contact,imageViewUrl,ceoImageUrl,coCeoImageUrl,culSecImageUrl,domain,tier;
    private static final int PICK_IMAGE_REQUEST = 1,PICK_IMAGE_REQUESTCEO = 2,PICK_IMAGE_REQUESTCOCEO = 3,PICK_IMAGE_REQUESTCULSEC = 4;
    private Uri imageUri,coCeoUri,ceoUri,culSecUri;
    CircleImageView imageView,imageViewCeo,imageViewCoCeo,imageViewCulSec;
    EditText editTextDummy;
    Button button;
    String uid;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation_detail);

        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        editTextName=findViewById(R.id.editTextOrgTitle);
        editTextAbout=findViewById(R.id.editTextAboutOrg);
        editTextCEO=findViewById(R.id.editTextCeoOrg);
        editTextCoCEO=findViewById(R.id.editTextCoCeoOrg);
        editTextCulSec=findViewById(R.id.editTextCulSecOrg);
        editTextContact=findViewById(R.id.editTextContactOrg);
        editTextTier=findViewById(R.id.editTextTierOrg);
        editTextDomain=findViewById(R.id.editTextDomainOrg);
        imageView=findViewById(R.id.imageViewOrganisation);
        button=findViewById(R.id.buttonSubmitDetails);
        imageViewCeo=findViewById(R.id.imageViewCEO);
        imageViewCoCeo=findViewById(R.id.imageViewCoCEO);
        imageViewCulSec=findViewById(R.id.imageViewCulSec);

        uid = user.getUid();

        database=FirebaseDatabase.getInstance();
        reference=database.getReference().child("Organisation").child("Detail");

        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrganisationPC organisationPC=dataSnapshot.getValue(OrganisationPC.class);
                name=organisationPC.getTitle();
                about=organisationPC.getAbout();
                ceo=organisationPC.getCeo();
                coCeo=organisationPC.getCoCeo();
                culSec=organisationPC.getCulSec();
                contact=organisationPC.getContact();
                imageViewUrl=organisationPC.getImageUrl();
                ceoImageUrl=organisationPC.getImageUrlCeo();
                coCeoImageUrl=organisationPC.getImageUrlCoCeo();
                culSecImageUrl=organisationPC.getImageUrlCulsec();
                domain=organisationPC.getDomain();
                tier=organisationPC.getTier();


                Glide.with(OrganisationDetailActivity.this).load(imageViewUrl).into(imageView);
                Glide.with(OrganisationDetailActivity.this).load(ceoImageUrl).into(imageViewCeo);
                Glide.with(OrganisationDetailActivity.this).load(coCeoImageUrl).into(imageViewCoCeo);
                Glide.with(OrganisationDetailActivity.this).load(culSecImageUrl).into(imageViewCulSec);
                editTextName.setText(name);
                editTextAbout.setText(about);
                editTextCEO.setText(ceo);
                editTextCoCEO.setText(coCeo);
                editTextCulSec.setText(culSec);
                editTextContact.setText(contact);
                editTextDomain.setText(domain);
                editTextTier.setText(tier);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        imageViewCeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooserCeo();
            }
        });

        imageViewCoCeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooserCoCeo();
            }
        });

        imageViewCulSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooserCulSec();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=editTextName.getText().toString();
                about=editTextAbout.getText().toString();
                ceo=editTextCEO.getText().toString();
                coCeo=editTextCoCEO.getText().toString();
                culSec=editTextCulSec.getText().toString();
                contact=editTextContact.getText().toString();
                tier=editTextTier.getText().toString();
                domain=editTextDomain.getText().toString();

                OrganisationPC organisationPC=new OrganisationPC();

                organisationPC.setTitle(name);
                organisationPC.setAbout(about);
                organisationPC.setCeo(ceo);
                organisationPC.setCoCeo(coCeo);
                organisationPC.setCulSec(culSec);
                organisationPC.setContact(contact);
                organisationPC.setImageUrl(imageViewUrl);
                organisationPC.setImageUrlCeo(ceoImageUrl);
                organisationPC.setImageUrlCoCeo(coCeoImageUrl);
                organisationPC.setImageUrlCulsec(culSecImageUrl);
                organisationPC.setDomain(domain);
                organisationPC.setTier(tier);
                organisationPC.setUid(uid);
                reference.child(uid).setValue(organisationPC);
            }
        });
    }

    public void editLayout(View view){
        BottomSheetDialogFragment bottomSheetDialogFragment = new ItemListDialogFragment();
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
    public void openFileChooserCeo() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUESTCEO);
    }
    public void openFileChooserCoCeo() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUESTCOCEO);
    }
    public void openFileChooserCulSec() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUESTCULSEC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewUrl=imageUri.toString();


            final String tym_stamp = String.valueOf(System.currentTimeMillis());

            String filePathAndName = "Posts/" + "post_" + tym_stamp;
            imageUri = data.getData();
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(imageUri.toString())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    String downloadUri = uriTask.getResult().toString();
                    imageViewUrl = downloadUri;

                    Glide.with(OrganisationDetailActivity.this).asBitmap().load(imageViewUrl).into(imageView);
                }
            });
        }
        if (requestCode == PICK_IMAGE_REQUESTCEO && resultCode == RESULT_OK && data != null && data.getData() != null) {

            final String tym_stamp = String.valueOf(System.currentTimeMillis());

            String filePathAndName = "Posts/" + "post_" + tym_stamp;
            ceoUri = data.getData();
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(ceoUri.toString())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    String downloadUri = uriTask.getResult().toString();
                    ceoImageUrl = downloadUri;

                    Glide.with(OrganisationDetailActivity.this).asBitmap().load(ceoImageUrl).into(imageViewCeo);
                }
            });



        }
        if (requestCode == PICK_IMAGE_REQUESTCOCEO && resultCode == RESULT_OK && data != null && data.getData() != null) {

            final String tym_stamp = String.valueOf(System.currentTimeMillis());

            String filePathAndName = "Posts/" + "post_" + tym_stamp;
            coCeoUri = data.getData();
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(coCeoUri.toString())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    String downloadUri = uriTask.getResult().toString();
                    coCeoImageUrl = downloadUri;

                    Glide.with(OrganisationDetailActivity.this).asBitmap().load(coCeoImageUrl).into(imageViewCoCeo);
                }
            });

        }
        if (requestCode == PICK_IMAGE_REQUESTCULSEC && resultCode == RESULT_OK && data != null && data.getData() != null) {

            final String tym_stamp = String.valueOf(System.currentTimeMillis());

            String filePathAndName = "Posts/" + "post_" + tym_stamp;
            culSecUri = data.getData();
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(culSecUri.toString())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    String downloadUri = uriTask.getResult().toString();
                    culSecImageUrl = downloadUri;

                    Glide.with(OrganisationDetailActivity.this).asBitmap().load(culSecUri).into(imageViewCulSec);
                }
            });
        }
    }

    @Override
    public void onItemClick(String item) {
        editTextDummy.setText(item);
    }
}
