package com.example.recyclenev;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private ArrayList<PostDetailPC> items;
    private Context context;
    Boolean liked=false;
    long like;

    public PostAdapter(ArrayList<PostDetailPC> items, Context context) {
        this.items = items;
        this.context = context;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_posts,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {

        final PostDetailPC postDetailPC=items.get(position);
        holder.textViewTitle.setText(postDetailPC.getEventName());
        holder.textViewTime.setText(postDetailPC.getEventUploadTime());
        holder.textViewName.setText(postDetailPC.getOrganisationName());
        holder.textViewLikes.setText(postDetailPC.getEventLikes());
        Glide.with(context).load(postDetailPC.getEventImageUrl()).into(holder.imageViewPost);
        Glide.with(context).load(postDetailPC.getOrganisationDP()).into(holder.imageViewOrgDp);

        String ppuid=postDetailPC.getPpUid();
        String puid=postDetailPC.getpUid();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        final String uid=user.getUid();
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Posts").child("Likes").child(ppuid).child(puid).child(uid);
        final DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference().child("Posts").child("Details").child(ppuid).child(puid).child("eventLikes");
        eventLike(ppuid,puid,holder.buttonLike);

        like=Long.parseLong(postDetailPC.getEventLikes());

        holder.imageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,PostDescriptionActivity.class);
                intent.putExtra("pid",postDetailPC.getpUid());
                intent.putExtra("upid",postDetailPC.getPpUid());
                intent.putExtra("epid",uid);
                context.startActivity(intent);
            }
        });
        holder.buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,CommentActivity.class);
                intent.putExtra("pid",postDetailPC.getpUid());
                intent.putExtra("upid",postDetailPC.getPpUid());
                context.startActivity(intent);
            }
        });

        holder.buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!liked) {
                    holder.buttonLike.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_thumb_up_blue_24dp, 0, 0, 0);
                    holder.buttonLike.setTextColor(Color.BLUE);
                    databaseReference.setValue(true);
                    like++;
                    databaseReference2.setValue(""+like);
                    holder.textViewLikes.setText(""+like);
                    liked=true;
                }
                else{
                    holder.buttonLike.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_like_black, 0, 0, 0);
                    holder.buttonLike.setTextColor(Color.BLACK);
                    databaseReference.setValue(false);
                    if(like>0)
                    like--;
                    databaseReference2.setValue(""+like);
                    holder.textViewLikes.setText(""+like);
                    liked=false;
                }
            }
        });

        holder.buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "CODS");
                    String sAux = "\nEventName- "+postDetailPC.getEventName()+"\nOrganised By- "+postDetailPC.getOrganisationName()+"\nImage Link- "+postDetailPC.getEventImageUrl()+"\nEvent Description"+postDetailPC.getEventDescription()+"" +
                            "\nEvent Date- "+postDetailPC.getEventDate()+"\nEvent Time- "+postDetailPC.getEventTime()+"\nLast date to register"+postDetailPC.getEventLastDate()+"" +
                            "\nDL Provided- "+postDetailPC.getEventDL()+"\nRegistration Link- "+postDetailPC.getEventLink()+"\nFee- "+postDetailPC.getEventFees()+"\nContact- "+postDetailPC.getEventContact();
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    context.startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
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
    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName,textViewTime,textViewTitle,textViewLikes;
        ImageView imageViewPost;
        CircleImageView imageViewOrgDp;
        Button buttonComment,buttonShare,buttonLike;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName=itemView.findViewById(R.id.uNameTv);
            textViewTime=itemView.findViewById(R.id.pTimeTv);
            textViewTitle=itemView.findViewById(R.id.pTitleTV);
            textViewLikes=itemView.findViewById(R.id.pLikesTv);
            imageViewPost=itemView.findViewById(R.id.pImageIv);
            buttonComment=itemView.findViewById(R.id.commentBt);
            imageViewOrgDp=itemView.findViewById(R.id.uPictureIv);
            buttonShare=itemView.findViewById(R.id.shareBt);
            buttonLike=itemView.findViewById(R.id.likeBt);
        }
    }
}
