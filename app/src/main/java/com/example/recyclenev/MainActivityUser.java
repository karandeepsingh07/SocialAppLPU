package com.example.recyclenev;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivityUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private  DrawerLayout drawer;
    String uid,email;
    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth auth;
    String orgImageUrl;
    String orgName;
    TextView textViewNB;
    ImageView imageViewNB;
    String username,userImage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.homeMenu:
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new HomeFragment()).commit();
                    break;
                case R.id.organisationList:
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new OrganisationListFragment()).commit();
                    break;
                case R.id.statusList:
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new StatusListFragment()).commit();
                    break;
            }
            return true;
        }
    };

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        Toolbar toolbar = findViewById(R.id.tooll);
        setSupportActionBar(toolbar);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        Toast.makeText(this, ""+pref.getString("user",null), Toast.LENGTH_SHORT).show();

        drawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_View);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView navigation = findViewById(R.id.bottomNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_draawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.Home);
        }

        imageViewNB=navigationView.getHeaderView(0).findViewById(R.id.imageViewNavBar);
        textViewNB=navigationView.getHeaderView(0).findViewById(R.id.textViewNavBar);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        email = user.getEmail();
        reference =  FirebaseDatabase.getInstance().getReference().child("User").child("Detail").child(uid);

        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("MyPref", 0);
        username = pref2.getString("Username",null);
        userImage=pref2.getString("Userimage",null);
        if(username!=null){
            textViewNB.setText(username);
        }
        if(userImage!=null){
            Glide.with(this).load(userImage).into(imageViewNB);
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserPC organisationPC=dataSnapshot.getValue(UserPC.class);
                orgImageUrl=organisationPC.getImageUrl();
                orgName=organisationPC.getName();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Username",orgName);
                editor.putString("Userimage",orgImageUrl);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.ViewProfile:
                startActivity(new Intent(MainActivityUser.this,UserDetailActivity.class));
                break;
            case R.id.Home:
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new HomeFragment()).commit();
                break;
            case R.id.share:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "CODS");
                    String sAux = "\nLet me recommend you this CODS application\n\n";
                    sAux = sAux + "applink \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                break;
            case R.id.changePassword:
                FragmentTransaction transaction4=getSupportFragmentManager().beginTransaction();
                transaction4.replace(R.id.Fragment_container,new ChangePasswordFragment());
                Fragment fragment4=new ChangePasswordFragment();
                transaction4.addToBackStack(fragment4.getClass().getName());
                transaction4.commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.LOGOUT:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
            case R.id.announcement:
                startActivity(new Intent(MainActivityUser.this,AnnouncemntActivity.class));
                break;
            case R.id.search:
                startActivity(new Intent(MainActivityUser.this,SearchActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
