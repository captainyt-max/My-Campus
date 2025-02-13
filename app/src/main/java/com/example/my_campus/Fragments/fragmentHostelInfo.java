package com.example.my_campus.Fragments;


import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_campus.R;


public class fragmentHostelInfo extends Fragment {

    ConstraintLayout buttonChanakya, buttonAryabhata, buttonGodavari, buttonBuddha;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hostel_info, container, false);

        buttonAryabhata = view.findViewById(R.id.aryabhata);
        buttonChanakya = view.findViewById(R.id.chanakya);
        buttonGodavari= view.findViewById(R.id.godavari);
        buttonBuddha = view.findViewById(R.id.buddha);


        buttonAryabhata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("aryabhata");
                clickAnimation(view);
            }
        });

        buttonChanakya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("chanakya");
                clickAnimation(view);
            }
        });

        buttonGodavari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("godavari");
                clickAnimation(view);
            }
        });

        buttonBuddha.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment("buddha");
                clickAnimation(view);
            }
        }));

        return view;
    }

    private void nextFragment(String hostel) {
        Bundle bundle = new Bundle();
        bundle.putString("hostel", hostel);

        fragmentSelectedHostelInfo fragmentSelectedHostelInfo = new fragmentSelectedHostelInfo();
        fragmentSelectedHostelInfo.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,  // enter animation
                R.anim.slide_out_left,  // exit animation
                R.anim.slide_in_left,   // pop enter (when returning back)
                R.anim.slide_out_right  // pop exit (when returning back)
        );
        transaction.replace(R.id.mainLayout, fragmentSelectedHostelInfo);
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