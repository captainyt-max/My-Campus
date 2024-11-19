package com.example.my_campus.Fragments;

import static com.example.my_campus.MainActivity.clickAnimation;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_campus.R;


public class fragmentNearbyPlaces extends Fragment {

    ConstraintLayout buttonReference, buttonStationary, buttonCybercafe, buttonHospital, buttonMedical;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearby_places, container, false);

        buttonReference = view.findViewById(R.id.reference);
        buttonStationary = view.findViewById(R.id.stationary);
        buttonCybercafe = view.findViewById(R.id.cybercafe);
        buttonHospital = view.findViewById(R.id.hospital);
        buttonMedical = view.findViewById(R.id.medical);

        buttonReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("reference");
                clickAnimation(view);
            }
        });

        buttonStationary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("stationary");
                clickAnimation(view);
            }
        });

        buttonCybercafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("cybercafe");
                clickAnimation(view);
            }
        });

        buttonHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("hospital");
                clickAnimation(view);
            }
        });

        buttonMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("medical");
                clickAnimation(view);
            }
        });

        return view;
    }

    private void nextFragment(String reference) {
        Bundle bundle = new Bundle();
        bundle.putString("reference", reference);

        fragmentSelectedNearbyPlaces fragmentSelectedNearbyPlaces = new fragmentSelectedNearbyPlaces();
        fragmentSelectedNearbyPlaces.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,  // enter animation
                R.anim.slide_out_left,  // exit animation
                R.anim.slide_in_left,   // pop enter (when returning back)
                R.anim.slide_out_right  // pop exit (when returning back)
        );
        transaction.replace(R.id.mainLayout, fragmentSelectedNearbyPlaces);
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