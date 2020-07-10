package com.example.uniwise;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class SettingsFragment extends Fragment {

    DatabaseReference UsersRef;
    DatabaseReference SettingUserRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    String currentUserID;
    String currentUsername;
    String currentPassword;
    EditText enteredUsername,enteredPassword,newUsername,newPassword,confirmPassword;
    Button save;
    Button deleteAccount;
    FirebaseUser firebaseUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_settings, container, false);


        mAuth= FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        currentUserID=mAuth.getCurrentUser().getUid();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        UsersRef=database.getReference().child("Users");
        SettingUserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        enteredUsername=view.findViewById(R.id.settings_current_username);
        enteredPassword=view.findViewById(R.id.settings_current_password);
        newUsername=view.findViewById(R.id.settings_new_username);
        newPassword=view.findViewById(R.id.settings_new_password);
        loadingBar=new ProgressDialog(getActivity());
        confirmPassword=view.findViewById(R.id.settings_confirm_password);
        save=view.findViewById(R.id.settings_save_button);
        deleteAccount=view.findViewById(R.id.delete_account);

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your " +
                        "account from the app and you won't able to access the app!");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUserData(currentUserID);
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();;
            }
        });

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("Username")) {
                       currentUsername = dataSnapshot.child("Username").getValue().toString();
                    }
                    if (dataSnapshot.hasChild("Password")) {
                        currentPassword = dataSnapshot.child("Password").getValue().toString();
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
            }
        });

        return view;
    }

    private void deleteUserData(String currentUserID) {
        DatabaseReference deUsers = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        deUsers.removeValue();
    }

    private void saveDetails() {
        loadingBar.setMessage("Updating.");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);

        String tempUsername=enteredUsername.getText().toString();
        String tempPassword=enteredPassword.getText().toString();
        String tempNewUsername=newUsername.getText().toString();
        String tempNewPassword=newPassword.getText().toString();
        String tempConfirmPassword=confirmPassword.getText().toString();

        if(tempUsername.equals(currentUsername)){
            if(tempPassword.equals(currentPassword)) {

                if (!TextUtils.isEmpty(tempNewUsername)) {
                    loadingBar.dismiss();
                    HashMap UserMap=new HashMap();
                    final Drawable updateIcon=getResources().getDrawable(R.drawable.checked_icon);
                    updateIcon.setBounds(0,0,updateIcon.getIntrinsicWidth(),updateIcon.getIntrinsicHeight());
                    UserMap.put("Username",tempNewUsername);
                    SettingUserRef.updateChildren(UserMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                newUsername.setError("Updated",updateIcon);
                            }
                            else {
                                newUsername.setError("Update Failed");
                            }

                        }
                    });
                }
                if (!TextUtils.isEmpty(tempNewPassword) && tempNewPassword.equals(tempConfirmPassword)) {
                    loadingBar.dismiss();
                    HashMap UserMap=new HashMap();
                    final Drawable updateIcon=getResources().getDrawable(R.drawable.checked_icon);
                    updateIcon.setBounds(0,0,updateIcon.getIntrinsicWidth(),updateIcon.getIntrinsicHeight());
                    UserMap.put("Password",tempNewPassword);
                    SettingUserRef.updateChildren(UserMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                newPassword.setError("Updated",updateIcon);
                            }
                            else {
                                newPassword.setError("Update Failed");
                            }

                        }
                    });
                }
                if((TextUtils.isEmpty(tempNewUsername)&&!(!TextUtils.isEmpty(tempNewPassword) && tempNewPassword.equals(tempConfirmPassword))))
                {
                    loadingBar.dismiss();
                    Toast.makeText(getActivity(), "Nothing was Updated.", Toast.LENGTH_SHORT).show();
                }

                loadingBar.dismiss();
            }
            else {
                loadingBar.dismiss();
                enteredPassword.setError("Please Enter Correct Password");
                enteredPassword.requestFocus();
            }
        }
        else {
            loadingBar.dismiss();
            enteredUsername.setError("Please Enter Correct Username");
            enteredUsername.requestFocus();
        }

    }

}
