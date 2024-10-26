package com.example.my_campus.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.my_campus.R;

public class fragmentSelectedGuidance extends Fragment {

    public fragmentSelectedGuidance() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_guidance, container, false);
        TextView tittle = view.findViewById(R.id.tittle);
        TextView article = view.findViewById(R.id.article);

        Bundle bundle = getArguments();
        if (bundle != null){
            String selectedTittle = bundle.getString("Guidance");

            if(selectedTittle.equals("scholarShipApply")){
                tittle.setText("Scholarship Apply");
                article.setText(R.string.scholarship_article);
            }

            if (selectedTittle.equals("drcc")){
                tittle.setText("Bihar Student Credit Card");
                article.setText(R.string.drcc_article);
            }
            if (selectedTittle.equals("kyp")){
                tittle.setText("KYP");
                article.setText(R.string.kyp_article);
            }
            if (selectedTittle.equals("exam_form")){
                tittle.setText("Exam Form Fill-up");
                article.setText(R.string.exam_form_article);
            }
            if (selectedTittle.equals("enrollment")){
                tittle.setText("Semester Enrollment");
                article.setText(R.string.enrollment_article);
            }
            if (selectedTittle.equals("result")){
                tittle.setText("Result Check");
                article.setText(R.string.result_article);
            }
            if (selectedTittle.equals("library")){
                tittle.setText("Library");
                article.setText(R.string.library_article);
            }
            if (selectedTittle.equals("rules")){
                tittle.setText("Rules and Regulation");
                article.setText(R.string.rules_article);
            }
            if (selectedTittle.equals("links")){
                tittle.setText("Important Links");
                article.setText(R.string.links_article);
            }
        }

        return view;
    }
}