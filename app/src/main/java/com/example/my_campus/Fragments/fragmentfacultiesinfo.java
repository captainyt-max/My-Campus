package com.example.my_campus.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.my_campus.R;


public class fragmentfacultiesinfo extends Fragment {

    ConstraintLayout buttonAutomobile, buttonCivil, buttonCse, buttonElectrical, buttonElectronics, buttonMechanical;

    public fragmentfacultiesinfo() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_facultiesinfo, container, false);

        buttonAutomobile = view.findViewById(R.id.automobile);
        buttonCivil = view.findViewById(R.id.civil);
        buttonCse = view.findViewById(R.id.cse);
        buttonElectrical = view.findViewById(R.id.electrical);
        buttonElectronics = view.findViewById(R.id.electronics);
        buttonMechanical = view.findViewById(R.id.mechanical);


        buttonAutomobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("automobile");
                clickAnimation(view);
            }
        });

        buttonCse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("cse");
                clickAnimation(view);
            }
        });

        buttonCivil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("civil");
                clickAnimation(view);
            }
        });

        buttonElectrical.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("electrical");
                clickAnimation(view);
            }
        }));

        buttonElectronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("electronics");
                clickAnimation(view);
            }
        });

        buttonMechanical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("mechanical");
                clickAnimation(view);
            }
        });

        return view;
    }

    //function created to move to next fragment, and passing bundle containing branch name to next fragment
    public void nextFragment(String branch){
        Bundle bundle = new Bundle();
        bundle.putString("branch", branch);

        fragmentBranchFaculty fragmentbranchfaculty = new fragmentBranchFaculty();
        fragmentbranchfaculty.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,  // enter animation
                R.anim.slide_out_left,  // exit animation
                R.anim.slide_in_left,   // pop enter (when returning back)
                R.anim.slide_out_right  // pop exit (when returning back)
        );
        transaction.replace(R.id.mainLayout, fragmentbranchfaculty);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void clickAnimation(View v){
        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100);
            }
        });
    }
}