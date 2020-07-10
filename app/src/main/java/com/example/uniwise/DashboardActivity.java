package com.example.uniwise;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RESULT_CODE = 100;
    private DrawerLayout mNavDrawer;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private ProgressDialog loadingBar;
    private ImageView NavProfileImg;
    private TextView NavUsername;
    private TextView NavEmail;
    String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        UsersRef=FirebaseDatabase.getInstance().getReference().child("Users");
        loadingBar=new ProgressDialog(this);


        NavigationView navigationView=findViewById(R.id.navigation_view);
        View navView=navigationView.inflateHeaderView(R.layout.header);

        NavProfileImg=(ImageView)navView.findViewById(R.id.nav_profileimg);
        NavUsername=(TextView)navView.findViewById(R.id.nav_profileusnm);
        NavEmail=(TextView)navView.findViewById(R.id.nav_profilemail);

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("Username")) {
                        String username = dataSnapshot.child("Username").getValue().toString();
                        NavUsername.setText(username);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Your Username was not set up successfully",Toast.LENGTH_LONG).show();
                    }
                    if (dataSnapshot.hasChild("Email ID")) {
                        String mail = dataSnapshot.child("Email ID").getValue().toString();
                        NavEmail.setText(mail);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Your mail was not set up successfully",Toast.LENGTH_LONG).show();
                    }
                    if (dataSnapshot.hasChild("profileImage")) {
                        String profilePic = dataSnapshot.child("profileImage").getValue().toString();
                        Picasso.with(DashboardActivity.this).load(profilePic).placeholder(R.drawable.profile_img).into(NavProfileImg);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Your profile was not set up successfully",Toast.LENGTH_LONG).show();
                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNavDrawer=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,mNavDrawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        mNavDrawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.HomeTab);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUserID==null){
          SendUserToLogin();
        }
        else{
            checkUserExistence();
        }
    }

    private void checkUserExistence()
    {
        loadingBar.setMessage("Please wait, while we fetch your Profile.");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);

     final String current_user_id=mAuth.getCurrentUser().getUid();
     UsersRef.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             if(!dataSnapshot.hasChild(currentUserID)) {
                 Toast.makeText(getApplicationContext(),"Your profile was not set up successfully, Please set it in the Profile menu...",Toast.LENGTH_LONG).show();
                 loadingBar.dismiss();
             }
             else {
                 loadingBar.dismiss();

             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });
    }

    private void SendUserToLogin() {
        Intent loginIntent=new Intent(DashboardActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


@Override
    public void onBackPressed() {
        if(mNavDrawer.isDrawerOpen(GravityCompat.START)){
            mNavDrawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    private void profile(){
        Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void onInviteClicked(){
        Intent intent=new AppInviteInvitation.IntentBuilder("Uniwise Invite")
                .setMessage("Upgrade your knowledge with Uniwise")
                .setDeepLink(Uri.parse("http://google.com"))
                .setCallToActionText("Invitation")
                .build();
        startActivityForResult(intent,RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_CODE){
            if(resultCode==RESULT_OK){
               String[] ids= AppInviteInvitation.getInvitationIds(resultCode,data);
               for(String id:ids)
               {
                   Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
               }
            }
            else {
                //error
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.HomeTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                break;
            case R.id.ProfileTab:
                profile();
                break;
            case R.id.DeptTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DepartmentFragment()).commit();
                break;
            case R.id.AboutTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                break;
            case R.id.ShareTab:
                onInviteClicked();
                break;
            case R.id.SettingsTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment()).commit();
                break;
            case R.id.LogoutTab:
                dialog();
                break;
        }
        mNavDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void dialog() {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            DashboardActivity.super.onBackPressed();
                            logout();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }
}










