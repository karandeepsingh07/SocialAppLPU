package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostDescriptionActivity extends AppCompatActivity {

    Boolean liked=false;
    long like;
    FirebaseDatabase database;
    DatabaseReference reference;
    TextView textEventDate,textEventTime,textFees,textDescription,textContact,textLink,textLastDate,textDl,textViewName,textViewTime,textViewTitle,textViewLikes;
    ImageView imageViewPost;
    String eventDate,eventTime,eventFees,eventDescription,eventContact,eventLink,eventLastDate,eventDL,name,time,title,imageUrl;
    String likes;
    FirebaseAuth firebaseAuth;
    String uid,pid,epid;
    Button buttonComment,buttonShare;
    CircleImageView orgDp;
    Button buttonLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_description);

        uid=getIntent().getStringExtra("upid");
        pid=getIntent().getStringExtra("pid");
        epid=getIntent().getStringExtra("epid");
        init();
        getDataPostDesc();
        eventLike(uid,pid,buttonLike);

        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PostDescriptionActivity.this,CommentActivity.class);
                intent.putExtra("pid",pid);
                intent.putExtra("upid",uid);
                startActivity(intent);
            }
        });
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Posts").child("Likes").child(uid).child(pid).child(epid);
        final DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference().child("Posts").child("Details").child(uid).child(pid).child("eventLikes");

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!liked) {
                    buttonLike.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_thumb_up_blue_24dp, 0, 0, 0);
                    buttonLike.setTextColor(Color.BLUE);
                    databaseReference.setValue(true);
                    like++;
                    databaseReference2.setValue(""+like);
                    textViewLikes.setText(""+like);
                    liked=true;
                }
                else{
                    buttonLike.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_like_black, 0, 0, 0);
                    buttonLike.setTextColor(Color.BLACK);
                    databaseReference.setValue(false);
                    if(like>0)
                    like--;
                    databaseReference2.setValue(""+like);
                    textViewLikes.setText(""+like);
                    liked=false;
                }
            }
        });
    }

    public void init(){
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();                                //change the reference here
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();

        //TextView
        textViewName=findViewById(R.id.uNameTv);
        textViewTime=findViewById(R.id.pTimeTv);
        textViewTitle=findViewById(R.id.pTitltTV);
        textViewLikes=findViewById(R.id.pLikesTv);
        imageViewPost=findViewById(R.id.pImageIv);
        textEventDate=findViewById(R.id.pEventDate);
        textEventTime=findViewById(R.id.pEventTime);
        textFees=findViewById(R.id.pFees);
        textDescription=findViewById(R.id.pDescription);
        textContact=findViewById(R.id.pContact);
        textLink=findViewById(R.id.pLink);
        textLastDate=findViewById(R.id.pLastDate);
        textDl=findViewById(R.id.pDL);
        buttonComment=findViewById(R.id.commentBt);
        buttonShare=findViewById(R.id.shareBt);
        orgDp=findViewById(R.id.uPictureIv);
        buttonLike=findViewById(R.id.likeBt);
    }

    public void getDataPostDesc(){
        reference.child("Posts").child("Details").child(uid).child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final PostDetailPC postDetail=dataSnapshot.getValue(PostDetailPC.class);
                eventDate=postDetail.getEventDate();
                eventTime=postDetail.getEventTime();
                eventFees=postDetail.getEventFees();
                eventDescription=postDetail.getEventDescription();
                eventContact=postDetail.getEventContact();
                eventLink=postDetail.getEventLink();
                eventLastDate=postDetail.getEventLastDate();
                eventDL=postDetail.getEventDL();
                name=postDetail.getOrganisationName();
                time=postDetail.getEventUploadTime();
                title=postDetail.getEventName();
                likes=postDetail.getEventLikes();
                imageUrl=postDetail.getEventImageUrl();
                Glide.with(PostDescriptionActivity.this).load(postDetail.getOrganisationDP()).into(orgDp);

                setData();
                buttonShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_SUBJECT, "CODS");
                            String sAux = "\nEventName- "+postDetail.getEventName()+"\nOrganised By- "+postDetail.getOrganisationName()+"\nImage Link- "+postDetail.getEventImageUrl()+"\nEvent Description"+postDetail.getEventDescription()+"" +
                                    "\nEvent Date- "+postDetail.getEventDate()+"\nEvent Time- "+postDetail.getEventTime()+"\nLast date to register"+postDetail.getEventLastDate()+"" +
                                    "\nDL Provided- "+postDetail.getEventDL()+"\nRegistration Link- "+postDetail.getEventLink()+"\nFee- "+postDetail.getEventFees()+"\nContact- "+postDetail.getEventContact();
                            i.putExtra(Intent.EXTRA_TEXT, sAux);
                            startActivity(Intent.createChooser(i, "choose one"));
                        } catch(Exception e) {
                            //e.toString();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void eventLike(String ppuid, String puid, final Button likeBtn){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Posts").child("Likes").child(ppuid).child(puid).child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue().toString().equals("true")) {
                        liked = true;
                        likeBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_thumb_up_blue_24dp, 0, 0, 0);
                        likeBtn.setTextColor(Color.BLUE);
                    } else {
                        likeBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_like_black, 0, 0, 0);
                        likeBtn.setTextColor(Color.BLACK);
                        liked = false;
                    }
                }catch (NullPointerException e){
                    liked=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void setData(){
        textViewName.setText(""+name);
        textViewTime.setText(""+time);
        textViewTitle.setText(""+title);
        textViewLikes.setText(""+likes);
        textEventDate.setText(""+eventDate);
        textEventTime.setText(""+eventTime);
        textFees.setText(""+eventFees);
        textDescription.setText(""+eventDescription);
        textContact.setText(""+eventContact);
        textLink.setText(""+eventLink);
        textLastDate.setText(""+eventLastDate);
        textDl.setText(""+eventDL);
        Glide.with(this).load(imageUrl).into(imageViewPost);
    }
}
