package com.example.uniwise;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddButtonQue extends Fragment {
    EditText question;
    Button addBtn;
    String selectedSubject;
    FirebaseAuth mAuth;
    String currentUserID;
    DatabaseReference UsersReference;
    DatabaseReference UsersRef;
    String selectedDept;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.add_button_que, container, false);
        mAuth= FirebaseAuth.getInstance();
        if(getActivity()!=null) {
            Intent intent = getActivity().getIntent();
            selectedSubject = intent.getStringExtra("SelectedSubject");
            selectedDept=intent.getStringExtra("dept");

        }

        UsersRef=FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserID=mAuth.getCurrentUser().getUid();

        question=view.findViewById(R.id.add_question_txt);
        addBtn=view.findViewById(R.id.add_question_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure you want to Upload this Question?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addNewQuestion(selectedSubject,selectedDept);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

            }
        });


        return view;
    }

    public void addNewQuestion(final String selectedSubject, final String selectedDept) {

        final String userQuestion=question.getText().toString();

        if(TextUtils.isEmpty(userQuestion)){
            Toast.makeText(getActivity(),"Please write your question.",Toast.LENGTH_LONG).show();
            return;
        }

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String username=dataSnapshot.child("Username").getValue().toString();
                    String userprofileImg=dataSnapshot.child("profileImage").getValue().toString();
                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yy");
                    final String saveCurrentDate = currentDate.format(calForDate.getTime());

                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                    final String  saveCurrentTime = currentTime.format(calForDate.getTime());


                    final FirebaseDatabase database=FirebaseDatabase.getInstance();
                    UsersReference=database.getReference().child("Departments").child(selectedDept).child(selectedSubject);
                    String pushID=UsersReference.push().getKey();
                    HashMap userMap=new HashMap();
                    userMap.put("uid",currentUserID);
                    userMap.put("usernamee",username);
                    userMap.put("profileImage",userprofileImg);
                    userMap.put("question",userQuestion);
                    userMap.put("date",saveCurrentDate);
                    userMap.put("time",saveCurrentTime);
                    UsersReference.child(pushID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getActivity(),"Question added successfully",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {
                                Toast.makeText(getActivity(),task.getException().toString(),Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
