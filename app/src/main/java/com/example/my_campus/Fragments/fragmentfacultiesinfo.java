package com.example.my_campus.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_campus.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentfacultiesinfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentfacultiesinfo extends Fragment {

    public fragmentfacultiesinfo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_facultiesinfo, container, false);
        return view;
    }
}