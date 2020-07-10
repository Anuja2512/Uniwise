package com.example.uniwise;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mNavDrawer;
    private ImageView ProfileImg;
    final static int Gallery_Pick=1;
    private StorageReference ProfileImgRef;
    private FirebaseAuth mAuth;
    String currentUserID;
    FirebaseUser user;
    private TextView enrollment;
    private TextView username;
    private TextView email;
    private TextView department;
    private ImageView NavProfileImg;
    private TextView description;
    private TextView NavUsername;
    private TextView NavEmail;
    private ProgressDialog loadingBar;
    private static final int RESULT_CODE = 100;
    DatabaseReference UsersReference;
    DatabaseReference UsersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);



        ProfileImgRef= FirebaseStorage.getInstance().getReference().child("Profile Images");

        mAuth= FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        UsersReference=database.getReference().child("Users").child(currentUserID);
        UsersRef=database.getReference().child("Users");
        loadingBar=new ProgressDialog(this);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        username=(TextView)findViewById(R.id.profile_username) ;
        enrollment=(TextView)findViewById(R.id.profile_enrollment) ;
        email=(TextView)findViewById(R.id.profile_email) ;
        department=(TextView)findViewById(R.id.profile_department) ;
        description=(TextView)findViewById(R.id.profile_description) ;

        ProfileImg=(ImageView)findViewById(R.id.profile_pic);
        ProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Pick);
            }
        });

        UsersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if (dataSnapshot.exists()){
                   if (dataSnapshot.hasChild("profileImage")) {
                       String image = dataSnapshot.child("profileImage").getValue().toString();
                       loadingBar.dismiss();
                       Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.profile_img).into(ProfileImg);
                   }
                   else
                   {
                       loadingBar.dismiss();
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("Username")) {
                        String pusername = dataSnapshot.child("Username").getValue().toString();
                        username.setText(pusername);
                    }
                    if (dataSnapshot.hasChild("Email ID")) {
                        String mail = dataSnapshot.child("Email ID").getValue().toString();
                        email.setText(mail);
                    }
                    if (dataSnapshot.hasChild("Enrollment Number")) {
                        String enrollment_number = dataSnapshot.child("Enrollment Number").getValue().toString();
                        enrollment.setText(enrollment_number);
                    }
                    if (dataSnapshot.hasChild("Department")) {
                        String department1 = dataSnapshot.child("Department").getValue().toString();
                        department.setText(department1);
                    }
                    if (dataSnapshot.hasChild("Description")) {
                        String description1 = dataSnapshot.child("Description").getValue().toString();
                        description.setText(description1);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                loadingBar.setMessage("Please wait, while we update your profile picture.");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(false);
                Uri resultUri = result.getUri();

                final StorageReference filePath = ProfileImgRef.child(currentUserID + ".jpg");
                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();

                                UsersReference.child("profileImage").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                                    startActivity(intent);
                                                    finish();

                                                } else {
                                                    Toast.makeText(ProfileActivity.this, "Error occured..." + task.getException(), Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });
                            }
                        });
                    }
                });
            }
        }
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if (id== android.R.id.home){
            Intent homeIntent=new Intent(this,DashboardActivity.class);
            startActivity(homeIntent);
        }
        return super.onOptionsItemSelected(item);
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
                break;
            case R.id.SettingsTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment()).commit();
                break;
            case R.id.LogoutTab:
                logout();
                break;
        }
        mNavDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
