package com.example.uniwise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SubjectActivity extends AppCompatActivity {

    TextView dept;
    String[] empty={"Empty List","Empty List"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String[] ITsubjectList = intent.getStringArrayExtra("ITkey");
        String[] ITcourseCode=intent.getStringArrayExtra("ITcode");
        String[] COMPsubjectList = intent.getStringArrayExtra("COMPkey");
        String[] COMPcourseCode=intent.getStringArrayExtra("COMPcode");
        String[] MECHsubjectList=intent.getStringArrayExtra("MECHkey");
        String[] CIVILsubjectList=intent.getStringArrayExtra("CIVILkey");
        String[] CIVILcourseCode=intent.getStringArrayExtra("CIVILcode");
        String[] ELECTRICALsubjectList=intent.getStringArrayExtra("ELECkey");
        String[] ENTCsubjectList=intent.getStringArrayExtra("ENTCkey");
        String[] METAsubjectList=intent.getStringArrayExtra("METAkey");
        String[] DDGMsubjectList=intent.getStringArrayExtra("DDGMkey");
        String[] MATHsubjectList=intent.getStringArrayExtra("MATHkey");
        String[] ENGsubjectList=intent.getStringArrayExtra("ENGkey");
        String[] PHYSICSsubjectList=intent.getStringArrayExtra("PHYSICSkey");
        String[] CHEMsubjectList=intent.getStringArrayExtra("CHEMkey");
        String[] Code=intent.getStringArrayExtra("Compcode");
        dept=findViewById(R.id.title_dept);

        RecyclerView Subjects = findViewById(R.id.list_subs);
        Subjects.setLayoutManager(new LinearLayoutManager(this));
        if(ITsubjectList!=null) {
            Subjects.setAdapter(new SubjectsAdapter(ITsubjectList,ITcourseCode,"IT_Department",this));
            dept.setText("IT Department");
        }
        else if(COMPsubjectList!=null){
            Subjects.setAdapter(new SubjectsAdapter(COMPsubjectList,COMPcourseCode,"Computer_Department",this));
            dept.setText("Computer Department");
        }
        else if(MECHsubjectList!=null){
            Subjects.setAdapter(new SubjectsAdapter(MECHsubjectList,Code,"Mechanical_Department",this));
            dept.setText("Mechanical Department");
        }
        else if(CIVILsubjectList!=null){
            Subjects.setAdapter(new SubjectsAdapter(CIVILsubjectList,CIVILcourseCode,"Civil_Department",this));
            dept.setText("Civil Department");
        }
        else if(ELECTRICALsubjectList!=null){
            Subjects.setAdapter(new SubjectsAdapter(ELECTRICALsubjectList,Code,"Electrical_Department",this));
            dept.setText("Electrical Department");
        }
        else if(ENTCsubjectList!=null){
            Subjects.setAdapter(new SubjectsAdapter(ENTCsubjectList,Code,"ENTC_Department",this));
            dept.setText("Electronics and Telecommunication Department");
        }
        else if(METAsubjectList!=null){
            Subjects.setAdapter(new SubjectsAdapter(METAsubjectList,Code,"Metallurgy_Department",this));
            dept.setText("Metallurgy Department");
        }
        else if(DDGMsubjectList!=null){
            Subjects.setAdapter(new SubjectsAdapter(DDGMsubjectList,Code,"DDGM_Department",this));
            dept.setText("Dress Designing and Garment Manufacturing Department");
        }
        else if(MATHsubjectList!=null){
            Subjects.setAdapter(new SubjectsAdapter(MATHsubjectList,Code,"math",this));
            dept.setText("Mathematics Department");
        }
        else if(ENGsubjectList!=null){
            Subjects.setAdapter(new SubjectsAdapter(ENGsubjectList,Code,"eng",this));
            dept.setText("English Department");
        }
        else if(PHYSICSsubjectList!=null){
            Subjects.setAdapter(new SubjectsAdapter(PHYSICSsubjectList,Code,"phy",this));
            dept.setText("Physics Department");
        }
        else if(CHEMsubjectList!=null){
            Subjects.setAdapter(new SubjectsAdapter(CHEMsubjectList,Code,"chem",this));
            dept.setText("Chemistry Department");
        }
        else {
            Subjects.setAdapter(new SubjectsAdapter(empty,empty,"empty",this));
        }
    }

}





