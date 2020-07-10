package com.example.uniwise;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class DepartmentFragment extends Fragment {
    String[] IT={"  Applied Mathematics-1", "  Applied Mathematics-2", "  Basics Of IT",
            "Computer Workshop", "Communication Skills", "Client Side Scripting Using Javascript",
            "Computer Security", "Development Of Softskills-1", "Development Of Softskills-2",
            "Digital Technics and Microprocessor", "Data Structure", "Data Communication and Networking",
            "Database Administrator", "English", "Environmental Science", "Engineering Physics",
            "Engineering Mathematics", "E-Commerce", "Fundamentals Of Electronics", "Fundamentals Of Electrical",
            "Graphics and Gaming Technology", "Java Programming-1", "Java Programming-2", "Linux Basics", "Multimedia and Animation",
            "Management Information System", "Mobile Application Development", "Network Management and Administration", "Operating System",
            "Programming in C", "Programming using .Net", "Project and Seminar", "Relational Database Management", "Software Engineering",
            "Software Testing","Server Side Scripting Using PHP", "Web Designing Using HTML"};
    String[] ITcode={"SC181","SC182", "IT281 ", "CM283", "HU182", "IT482  ", "CM485","NC481", "NC482","IT381  ", "CM387", "IT383  ", "IT581  ", "HU181",
            "AU481", "SC183", "SC282", "AU486", "ET284","EE283", "IT584  ", "CM389", "CM482", "CM284", "IT382  ", "MA487", "IT481  "," CM586", "IT385  ", "CM282",
            "IT483  ", "CM481","IT384  ","IT484  ","CM486", "IT582  ", "CM285"};

    String[] COMP={"Applied Mathematics-1", "Applied Mathematics-2", "Applied Mathematics-3 ", "Advance Computer Network ", "Computer Essentials ",
            "Computer Workshop ", "Communication Skills ", "Computer Graphics ", "Computer Network", " CPHM ", "Computer Security ", "Digital Techniques ",
            "Data Structure ", "Development of Soft-Skill 1 ", "Development of Soft-Skill 2 ","English ", "Environmental Science ","Engineering Physics ",
            "E-Commerce ", "Fundamental of Electronics ", "Fundamentals of Electrical Engg  ", "Java Programming-1 ","Java Programming-2 ", "Linux Basics ",
            "MIS ", "Micro-processor Programming", "Multimedia Techniques ", "Network Management & Administration ", "Operating System ",
            "Object Oriented Programming:C++", "Programming in C ", "RDBMS ", "Software Engineering ", "Software Testing  ", "Scripting Technology using JSP ",
            "TEADAF ", "Web designing using HTML ", " Web Technology using JavaScript ", "Windows Programming"};
    String[] COMPcode={"SC181"," SC182"," SC282"," CM484"," CM281"," CM283"," HU182"," CM383"," CM386"," CM384"," CM485"," CM385", "CM387"," NC481"," NC482",
            " HU181"," AU481"," SC183"," AU486"," ET284"," EE283"," CM389", " CM482"," CM284"," MA487","  CM382", "CM584"," CM586"," CM381"," CM388"," CM282",
            " CM487"," CM483"," CM486"," CM585"," CM581"," CM285" ," CM583","CM582"};

    String[] MECH={"Power Engineering","Mechanics","CAD"};

    String[] CIVIL={"Applied Mathematics-1 ", "Applied Mathematics-2 ", "Applied Mathematics-3 ", "Applied Chemistry ", "Applied Physics ",
            "Analysis & design of structures ", "Communication Skills-1 ", "Communication Skills-2 ", " Construction Material ", "Construction Technology",
            "Concrete Technology ", "Civil Engineering Drawing ", "Community Development ", "Construction Equipment and Machine ", "Contracts and Accounts ",
            "Construction Management ","CAD & Communication Application ","Development of Soft Skills-1 ", "Development of Soft Skills-2 ", "Engineering Drawing",
            "Engineering Mechanics ", "Environmental Science ", "Environmental Engineering ", "Estimation & Costing ", "Highway & Bridge Engineering ","Hydraulics",
            "ICT ", " Irrigation Engineering ", "Mechanics of Materials & Structures", "Mini project", "Project & Seminar ", "Railway and tunnel engineering ",
            "Surveying-1 ", "Surveying-2 ", " Soil Mechanics ", "Theory of structures ", "Workshop Practise"};
    String[] CIVILcode={"SC1101"," SC1102"," SC281"," SC1106"," SC1103"," AM482"," HU1101"," HU1102"," CE2101"," CE387"," AM382"," CE382", "AU482"," CE584","CE485",
            " MA481"," CE486"," NC481"," NC482","AM2101","ME2103"," AU4101"," CE484", " CE482"," CE386","CE385", "CM2102"," CE483","AM381","CE381"," CE481"," CE583",
            " CE383"," CE384"," AM383"," AM481"," ME2101"};

    String[] ELECTRICAL={"elec1","elec2","elec3","elec4","elec5"};
    String[] Code={"SC1101"," SC1102"};
    String[] ENTC={"entc1","entc2","entc3","entc4","entc5"};
    String[] META={"Meta1","Meta2","Meta3","Meta4","Meta5"};
    String[] DDGM={"DDGM1","DDGM2","DDGM3","DDGM4","DDGM5"};
    String[] ENGLISH={"English","CMS"};
    String[] PHYSICS={"Engg Physics"};

    public DepartmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_department, container, false);

        CardView cdIT=(CardView)rootView.findViewById(R.id.itCard);
        CardView cdComp=(CardView)rootView.findViewById(R.id.compCard);
        CardView cdMech=(CardView)rootView.findViewById(R.id.mechanicalCard);
        CardView cdCivil=(CardView)rootView.findViewById(R.id.civilCard);
        CardView cdElectric=(CardView)rootView.findViewById(R.id.electricalCard);
        CardView cdENTC=(CardView)rootView.findViewById(R.id.entcCard);
        CardView cdMeta=(CardView)rootView.findViewById(R.id.metallurgyCard);
        CardView cdDDGM=(CardView)rootView.findViewById(R.id.ddgmCard);

        CardView cdEng=(CardView)rootView.findViewById(R.id.Label1Card);
        CardView cdPhy=(CardView)rootView.findViewById(R.id.Label2Card);



        cdIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent=new Intent(getActivity(),SubjectActivity.class);
                intent.putExtra("ITkey",IT);
                intent.putExtra("ITcode",ITcode);
                getActivity().startActivity(intent);

            }
        });

        cdComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(getActivity(),SubjectActivity.class);
                intent.putExtra("COMPkey",COMP);
                intent.putExtra("COMPcode",COMPcode);
                getActivity().startActivity(intent);
            }
        });

        cdMech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(getActivity(),SubjectActivity.class);
                intent.putExtra("MECHkey",MECH);
                intent.putExtra("COMPcode",Code);
                getActivity().startActivity(intent);
            }
        });

        cdCivil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(getActivity(),SubjectActivity.class);
                intent.putExtra("CIVILkey",CIVIL);
                intent.putExtra("CIVILcode",CIVILcode);
                getActivity().startActivity(intent);
            }
        });

        cdElectric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(getActivity(),SubjectActivity.class);
                intent.putExtra("ELECkey",ELECTRICAL);
                intent.putExtra("COMPcode",Code);
                getActivity().startActivity(intent);
            }
        });

        cdENTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(getActivity(),SubjectActivity.class);
                intent.putExtra("ENTCkey",ENTC);
                intent.putExtra("COMPcode",Code);
                getActivity().startActivity(intent);
            }
        });

        cdMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(getActivity(),SubjectActivity.class);
                intent.putExtra("METAkey",META);
                intent.putExtra("COMPcode",Code);
                getActivity().startActivity(intent);
            }
        });

        cdDDGM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(getActivity(),SubjectActivity.class);
                intent.putExtra("DDGMkey",DDGM);
                intent.putExtra("COMPcode",Code);
                getActivity().startActivity(intent);
            }
        });



        cdEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(getActivity(),SubjectActivity.class);
                intent.putExtra("ENGkey",ENGLISH);
                intent.putExtra("COMPcode",Code);
                getActivity().startActivity(intent);
            }
        });

        cdPhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(getActivity(),SubjectActivity.class);
                intent.putExtra("PHYSICSkey",PHYSICS);
                intent.putExtra("COMPcode",Code);
                getActivity().startActivity(intent);
            }
        });


        return rootView;
    }

}
