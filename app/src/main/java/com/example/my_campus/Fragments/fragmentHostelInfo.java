package com.example.my_campus.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_campus.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentHostelInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentHostelInfo extends Fragment {


    public fragmentHostelInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_hostel_info, container, false);
        return view;
    }
}