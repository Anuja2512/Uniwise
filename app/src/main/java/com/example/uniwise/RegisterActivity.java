package com.example.uniwise;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText email,password,enrollment,username,phone,confirmPassword,department;
    Button submit,goLogin;
    private ProgressDialog loadingBar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference UsersReference;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth= FirebaseAuth.getInstance();

        submit=(Button)findViewById(R.id.RSubmitBtn);
        user=mAuth.getCurrentUser();

        email=findViewById(R.id.REmailTxt);
        enrollment=findViewById(R.id.REnrollmentTxt);
        username=findViewById(R.id.RUsernameTxt);
        phone=findViewById(R.id.RPhoneTxt);
        password=findViewById(R.id.RPasswordTxt);
        confirmPassword=findViewById(R.id.RConfirmPassTxt);
        department=findViewById(R.id.Rdepartmenttxt);

        loadingBar=new ProgressDialog(this);
        goLogin=findViewById(R.id.RtoLSwitchBtn);
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail,pass,cpass,enroll,Username,phn,dept;
                mail=email.getText().toString();
                pass=password.getText().toString();
                cpass=confirmPassword.getText().toString();
                enroll=enrollment.getText().toString();
                Username=username.getText().toString();
                phn=phone.getText().toString();
                dept=department.getText().toString();
                registerNewUser(mail,pass,cpass,enroll,Username,phn,dept);


            }
        });
    }
    private void storeUserOnFireBase(String userName,String Enrollment,String email,String phone,String passwd,String UID,String dept){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        UsersReference=database.getReference().child("Users").child(UID);
        HashMap userMap=new HashMap();
        userMap.put("Username",userName);
        userMap.put("Enrollment Number",Enrollment);
        userMap.put("Department",dept);
        userMap.put("Email ID",email);
        userMap.put("Phone Number",phone);
        userMap.put("Alumini","none");
        userMap.put("Password",passwd);
        userMap.put("Description","Student of Government Polytechnic, Pune.");
        UsersReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {

                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
    public void registerNewUser(String mail,String pswd,String cpass,String enroll,String Username,String phn,String dept)
    {
        if(TextUtils.isEmpty(enroll)){
            enrollment.setError("Please enter Enrollment.");
            enrollment.requestFocus();
            return;
        }
        else  if(TextUtils.isEmpty(Username)){
            username.setError("Please enter Username.");
            username.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(mail)){
            email.setError("Please enter Email.");
            email.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(dept)){
            department.setError("Please enter Department.");
            department.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(phn)){
            phone.setError("Please enter Phone No.");
            phone.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(pswd)){
            password.setError("Please enter Password.");
            password.requestFocus();
            return;
        }
        else if(!pswd.equals(cpass)){
                confirmPassword.setError("Please confirm Password.");
                confirmPassword.requestFocus();
            return;
        }

        loadingBar.setTitle("Create New Account");
        loadingBar.setMessage("Please wait, while we create your new account.");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);

        mAuth.createUserWithEmailAndPassword(mail,pswd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Verification link has been sent to "+ mAuth.getCurrentUser().getEmail()+", Please verify it.",Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            currentUserID=mAuth.getCurrentUser().getUid();
                            String mail,pass,enroll,usnm,phn,dept;
                            mail=email.getText().toString();
                            enroll=enrollment.getText().toString();
                            usnm=username.getText().toString();
                            phn=phone.getText().toString();
                            pass=password.getText().toString();
                            dept=department.getText().toString();


                            storeUserOnFireBase(usnm,enroll,mail,phn,pass,currentUserID,dept);

                            user=mAuth.getCurrentUser();
                            user.sendEmailVerification();

                            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                           startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }


}



