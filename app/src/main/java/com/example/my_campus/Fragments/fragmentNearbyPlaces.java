package com.example.my_campus.Fragments;



import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_campus.R;
import com.example.my_campus.utility;


public class fragmentNearbyPlaces extends Fragment {

    ConstraintLayout buttonReference, buttonStationary, buttonCybercafe, buttonHospital, buttonMedical;
    private utility ut = new utility();


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
                ut.clickAnimation(view);
            }
        });

        buttonStationary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("stationary");
                ut.clickAnimation(view);
            }
        });

        buttonCybercafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("cybercafe");
                ut.clickAnimation(view);
            }
        });

        buttonHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("hospital");
                ut.clickAnimation(view);
            }
        });

        buttonMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("medical");
                ut.clickAnimation(view);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Fragment fragment = getParentFragmentManager().findFragmentByTag("fragmentHomepage");

                        if (fragment == null || !fragment.isVisible()) {
                            // User is NOT on homepage → go to homepage
                            fragmentHomepage homePageFragment = new fragmentHomepage();
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.mainLayout, homePageFragment, "fragmentHomepage");
                            transaction.commit();
                        } else {
                            // User is already on homepage → exit app
                            requireActivity().finish();
                        }
                    }
                }
        );
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

}