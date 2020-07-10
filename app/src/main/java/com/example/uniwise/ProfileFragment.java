package com.example.uniwise;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

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
    private TextView description;
    private ProgressDialog loadingBar;
    private static final int RESULT_CODE = 100;
    DatabaseReference UsersReference;
    DatabaseReference UsersRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview=inflater.inflate(R.layout.fragment_profile, container, false);

        ProfileImgRef= FirebaseStorage.getInstance().getReference().child("Profile Images");

        mAuth= FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        UsersReference=database.getReference().child("Users").child(currentUserID);
        UsersRef=database.getReference().child("Users");
        loadingBar=new ProgressDialog(rootview.getContext());
        username=(TextView)rootview.findViewById(R.id.profile_username) ;
        enrollment=(TextView)rootview.findViewById(R.id.profile_enrollment) ;
        email=(TextView)rootview.findViewById(R.id.profile_email) ;
        department=(TextView)rootview.findViewById(R.id.profile_department) ;
        description=(TextView)rootview.findViewById(R.id.profile_description) ;

        ProfileImg=(ImageView)rootview.findViewById(R.id.profile_pic);
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
                        Picasso.with(getActivity()).load(image).placeholder(R.drawable.profile_img).into(ProfileImg);
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
                        Toast.makeText(getActivity(),"Your profile was not set up successfully",Toast.LENGTH_LONG).show();
                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getActivity());
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
                                                    Intent intent = new Intent(getActivity(), ProfileFragment.class);
                                                    startActivity(intent);
                                                    getActivity().finish();

                                                } else {
                                                    Toast.makeText(getActivity(), "Error occured..." + task.getException(), Toast.LENGTH_LONG).show();

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


}
