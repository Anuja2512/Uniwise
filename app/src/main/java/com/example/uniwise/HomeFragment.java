package com.example.uniwise;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ViewPager viewPager;
    CustomSwipeAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);
        viewPager=(ViewPager)view.findViewById(R.id.view_pager);
        adapter=new CustomSwipeAdapter(this.getActivity());
        viewPager.setAdapter(adapter);
        ArrayList<String> eventArray=new ArrayList<>();
        eventArray.add("Verification and Photocopy notice for rhr students");
        eventArray.add("Online Exam Time table");
        eventArray.add("Leaving Certificate Application form");
        eventArray.add("Quotation for supply of DDGM Department equipments");
        eventArray.add("Leaving certificate format");
        eventArray.add("Notice for Convocation");
        eventArray.add("Form for ID card");
        eventArray.add("Instruction for Students for New MIS 2019");
        eventArray.add("Concession Forms");
        eventArray.add("DSA Fees structure");
        eventArray.add("Quotation for supply of Civil Department equipments");
        eventArray.add("Quotation for supply of diploma certificates and term-marks");
        eventArray.add("Quotation for supply of applied mech. department equipments");
        eventArray.add( "Academic Calendar Odd 2019");
        eventArray.add("Quotation for supply of Electrical Department equipments");
        eventArray.add("Quotation for supply of Electronics & Telecommunication Department equipments");
        eventArray.add("Quotation for supply of Information & Technology Department equipments");
        eventArray.add("Quotation for supply of Mechanical Department softwares");
        eventArray.add("Academic Calendar First year 2019");
        eventArray.add("Reassessment");
        eventArray.add("PhotoCopy notice");
        eventArray.add("Walk in visiting interview notice");
        eventArray.add("180(OB) Students notice");
        eventArray.add("Verification & photocopy for 2017 Even");
        eventArray.add("Certified Courses (CPE): IOT/C & C++, Robotics");
        eventArray.add("Important Notice for students: Equivalence to the cources under 180Q Scheme with courses under 180S Scheme for Admission 2nd & 3rd year 2019-20");

        RecyclerView recyclerView=view.findViewById(R.id.EventsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        EventAdapter eventAdapter=new EventAdapter(getActivity(),eventArray);
        recyclerView.setAdapter(eventAdapter);
        return view;

    }

}
