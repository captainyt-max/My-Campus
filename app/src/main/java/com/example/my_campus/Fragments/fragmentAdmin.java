package com.example.my_campus.Fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_campus.R;


public class fragmentAdmin extends Fragment {



    public fragmentAdmin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        ConstraintLayout buttonCampusActivity = view.findViewById(R.id.buttonCampusActivity);

        buttonCampusActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminCampusActivity adminCampusActivity = new adminCampusActivity();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.mainLayout, adminCampusActivity);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }
}