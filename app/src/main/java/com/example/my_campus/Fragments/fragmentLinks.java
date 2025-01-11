package com.example.my_campus.Fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_campus.R;


public class fragmentLinks extends Fragment {

    ConstraintLayout buttonSBTE, buttonNGP, buttonScholarship;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_links, container, false);

        buttonSBTE = view.findViewById(R.id.sbte);
        buttonNGP = view.findViewById(R.id.ngp);
        buttonScholarship = view.findViewById(R.id.scholarship);

        buttonSBTE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("sbte");
                clickAnimation(view);
            }
        });

        buttonNGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("ngp");
                clickAnimation(view);
            }
        });

        buttonScholarship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("scholar");
                clickAnimation(view);
            }
        });

        return view;
    }
    private void nextFragment(String link) {
        Bundle bundle = new Bundle();
        bundle.putString("link", link);

        fragmentSelectedLinks fragmentSelectedLinks = new fragmentSelectedLinks();
        fragmentSelectedLinks.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,  // enter animation
                R.anim.slide_out_left,  // exit animation
                R.anim.slide_in_left,   // pop enter (when returning back)
                R.anim.slide_out_right  // pop exit (when returning back)
        );
        transaction.replace(R.id.mainLayout, fragmentSelectedLinks);
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