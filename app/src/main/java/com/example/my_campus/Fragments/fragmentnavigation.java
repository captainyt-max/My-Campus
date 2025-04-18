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

public class fragmentnavigation extends Fragment {

    ConstraintLayout buttonCampus, buttonHostel;

    public fragmentnavigation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        buttonCampus = view.findViewById(R.id.campus);
        buttonHostel = view.findViewById(R.id.hostel);


        buttonCampus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("campus");
                clickAnimation(view);
            }
        });

        buttonHostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("hostel");
                clickAnimation(view);
            }
        });


        return  view;
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


    private void nextFragment(String navigation) {
        Bundle bundle = new Bundle();
        bundle.putString("navigation", navigation);

        fragmentSelectednavigation fragmentSelectednavigation = new fragmentSelectednavigation();
        fragmentSelectednavigation.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,  // enter animation
                R.anim.slide_out_left,  // exit animation
                R.anim.slide_in_left,   // pop enter (when returning back)
                R.anim.slide_out_right  // pop exit (when returning back)
        );
        transaction.replace(R.id.mainLayout, fragmentSelectednavigation);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void clickAnimation(View v) {
        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100);
            }
        });
    }
}