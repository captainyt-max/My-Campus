package com.example.my_campus.Fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my_campus.R;

public class fragmentAdmin extends Fragment {

    private String [] campusCurrentMassageText = new String[1];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view = inflater.inflate(R.layout.fragment_admin, container, false);

     TextView campusActivityCurrentMassage = view.findViewById(R.id.campusActivityCurrentMassage);
     ImageView campusActivityDelBtn = view.findViewById(R.id.campusActivityDelBtn);
     ImageView campusActivityEditBtn = view.findViewById(R.id.campusActivityEditBtn);
     TextView campusActivityUpdateTime = view.findViewById(R.id.campusActivityUpdateTime);
     EditText campusActivityNewMassage = view.findViewById(R.id.campusActivtyNewMassage);
     ConstraintLayout campusActivityNewMassageSendBtn = view.findViewById(R.id.campusActivityNewMassageSendBtn);
     ConstraintLayout currentMassageCancelBtn = view.findViewById(R.id.currentMassageCancelBtn);
     ConstraintLayout campusActivityEditMassageSendBtn = view.findViewById(R.id.campusActivityEditMassageSendBtn);

     EditText convertToEditText = new EditText(getActivity());

    campusActivityEditBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //ConstraintLayout campusCurrentMassageLayout = view.findViewById(R.id.campusCurrentMassageLayout);
            convertToEditText.setLayoutParams(campusActivityCurrentMassage.getLayoutParams());
            convertToEditText.setText(campusActivityCurrentMassage.getText());
            campusCurrentMassageText [0] = campusActivityCurrentMassage.getText().toString();

            convertToEditText.setBackgroundResource(R.drawable.massage_input_background);
            convertToEditText.setPadding(25,27,25,27);

            ViewGroup parent = (ViewGroup) campusActivityCurrentMassage.getParent();
            int index = parent.indexOfChild(campusActivityCurrentMassage);
            parent.removeView(campusActivityCurrentMassage);
            parent.addView(convertToEditText, index);

            campusActivityEditBtn.setEnabled(false);
            campusActivityEditBtn.setColorFilter(Color.parseColor("#00a6ff"), PorterDuff.Mode.SRC_IN);
            currentMassageCancelBtn.setVisibility(View.VISIBLE);
            campusActivityEditMassageSendBtn.setVisibility(View.VISIBLE);
        }
    });

    currentMassageCancelBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            campusActivityCurrentMassage.setText(campusCurrentMassageText[0]);

            ViewGroup parent = (ViewGroup) convertToEditText.getParent();
            int index = parent.indexOfChild(convertToEditText);
            parent.removeView(convertToEditText);
            parent.addView(campusActivityCurrentMassage, index);

            campusActivityEditMassageSendBtn.setVisibility(View.GONE);
            currentMassageCancelBtn.setVisibility(View.GONE);
            campusActivityEditBtn.setEnabled(true);
            campusActivityEditBtn.clearColorFilter();
        }
    });

        return view;
    }
}