package com.example.my_campus.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.my_campus.R;
import com.example.my_campus.Fragments.fragmentSelectedGuidance;


public class fragmentGuidance extends Fragment {

    ConstraintLayout buttonScholarship, buttonDrcc, buttonKyp, buttonExam_form, buttonEnrollment, buttonResult, buttonLibrary, buttonRules, buttonLinks;

    public fragmentGuidance() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__guidance, container, false);
        buttonScholarship = view.findViewById(R.id.scholarShipApply);
        buttonDrcc = view.findViewById(R.id.drcc);
        buttonKyp = view.findViewById(R.id.kyp);
        buttonExam_form = view.findViewById(R.id.exam_form);
        buttonEnrollment = view.findViewById(R.id.enrollment);
        buttonResult = view.findViewById(R.id.result);
        buttonLibrary = view.findViewById(R.id.library);
        buttonRules = view.findViewById(R.id.rules);



        buttonScholarship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("scholarShipApply");
                clickAnimation(view);
            }
        });

        buttonDrcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("drcc");
                clickAnimation(view);
            }
        });

        buttonKyp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("kyp");
                clickAnimation(view);
            }
        });

        buttonExam_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("exam_form");
                clickAnimation(view);
            }
        });

        buttonEnrollment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("enrollment");
                clickAnimation(view);
            }
        });

        buttonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("result");
                clickAnimation(view);
            }
        });

        buttonLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("library");
                clickAnimation(view);
            }
        });

        buttonRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("rules");
                clickAnimation(view);
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


    private void nextFragment(String Guidance) {
        Bundle bundle = new Bundle();
        bundle.putString("Guidance", Guidance);

        fragmentSelectedGuidance fragmentSelectedGuidance = new fragmentSelectedGuidance();
        fragmentSelectedGuidance.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,  // enter animation
                R.anim.slide_out_left,  // exit animation
                R.anim.slide_in_left,   // pop enter (when returning back)
                R.anim.slide_out_right  // pop exit (when returning back)
        );
        transaction.replace(R.id.mainLayout, fragmentSelectedGuidance);
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