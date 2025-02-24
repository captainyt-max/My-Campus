package com.example.my_campus.Fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_campus.R;


public class fragmentSyllabus extends Fragment {

    ConstraintLayout buttonAutomobile, buttonCivil, buttonCSE, buttonElectrical, buttonElectronic, buttonMechanical;

    public fragmentSyllabus() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_syllabus, container, false);

        buttonAutomobile = view.findViewById(R.id.automobile);
        buttonCivil = view.findViewById(R.id.civil);
        buttonCSE = view.findViewById(R.id.cse);
        buttonElectrical = view.findViewById(R.id.electrical);
        buttonElectronic = view.findViewById(R.id.electronics);
        buttonMechanical = view.findViewById(R.id.mechanical);




        buttonAutomobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("auto");
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

        buttonCSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("cse");
                clickAnimation(view);
            }
        });

        buttonElectrical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("electrical");
                clickAnimation(view);
            }
        });

        buttonElectronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("electronic");
                clickAnimation(view);
            }
        });

        buttonMechanical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("mech");
                clickAnimation(view);
            }
        });


        return view;

    }

    public void nextFragment(String Syllabus) {
        Bundle bundle = new Bundle();
        bundle.putString("Syllabus", Syllabus);

        fragmentSelectedSyllabus fragmentselectedsyllabus = new fragmentSelectedSyllabus();
        fragmentselectedsyllabus.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,  // enter animation
                R.anim.slide_out_left,  // exit animation
                R.anim.slide_in_left,   // pop enter (when returning back)
                R.anim.slide_out_right  // pop exit (when returning back)
        );
        transaction.replace(R.id.mainLayout, fragmentselectedsyllabus);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void clickAnimation(View v) {
        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100);
            }
        });
    }
}