package com.example.my_campus.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.my_campus.CustomListAdapter;
import com.example.my_campus.ListItem;
import com.example.my_campus.R;

import java.util.ArrayList;

public class fragmentBranchFaculty extends Fragment {

    //define ArrayList for all branch faculties
    ArrayList<ListItem> cseFacultieslist = new ArrayList<>();
    ArrayList<ListItem> civilFacultieslist = new ArrayList<>();
    ArrayList<ListItem> automobileFacultieslist = new ArrayList<>();
    ArrayList<ListItem> electricalFacultieslist = new ArrayList<>();
    ArrayList<ListItem> electronicsFacultieslist = new ArrayList<>();
    ArrayList<ListItem> mechanicalFacultieslist = new ArrayList<>();

    TextView branchName;
    ListView listView;

    public fragmentBranchFaculty() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_branch_faculty, container, false);

        branchName = view.findViewById(R.id.branch);
        //adding items to cse faculties array list
        listView = view.findViewById(R.id.facultyListView);


        cseFacultieslist.add(new ListItem("S.N. Singh", R.drawable.faculty_cse_saktinath, "Lecturer(HOD)", "9873647576", "snsngpp@ngpp.ac.in"));
        cseFacultieslist.add(new ListItem("Umar Farooque", R.drawable.faculty_cse_faruk, "Lecturer", "9771861868", "umarfarooque10@ngpp.ac.in"));
        cseFacultieslist.add(new ListItem("Mr. Sudhakar Kumar", R.drawable.faculty_cse_sudhakar, "Lecturer", "9673373296", "skumarngp13@gmail.com"));
        cseFacultieslist.add(new ListItem("Mrs. Divya Prabha", R.drawable.faculty_cse_divyaprabha, "Lecturer", "8210362921", "divyaprabha.ngp13@gmail.com"));
        cseFacultieslist.add(new ListItem("Ayesha Alam", R.drawable.faculty_cse_ayesha, "Lecturer", "8676824560", "ayeshaalam8676@gmail.com"));


        //adding items to civil faculties array list
        civilFacultieslist.add(new ListItem("Dr. Chandrasekhar Singh", R.drawable.faculty_civil_chandrashekhar ,"Principal", "", "cssingh2001@gmail.com"));
        civilFacultieslist.add(new ListItem("Syed Aftab Ahmed", R.drawable.faculty_civil_syedaftabahmed ,"Lecturer(HOD)", "7903156070", "sadduaftab@gmail.com"));
        civilFacultieslist.add(new ListItem("Mr. Tribhuwan Kumar", R.drawable.faculty_civil_tribhuwan,"Lecturer", "7979967740", "tribhuwan1633@gmail.com"));
        civilFacultieslist.add(new ListItem("Mr. Rohit Kumar", R.drawable.faculty_civil_rohit ,"Lecturer", "8010553831", " rohitrocksmit@gmail.com"));
        civilFacultieslist.add(new ListItem("Antra Kumari", R.drawable.faculty_civil_antra ,"Lecturer", "9241353765", "antra641ce@gmail.com"));
        civilFacultieslist.add(new ListItem("Binita", R.drawable.faculty_civil_binita ,"Lecturer", "7643879992", "abbinita96@gmail.com"));
        civilFacultieslist.add(new ListItem("Anandi Prasad Yadav", R.drawable.faculty_civil_anandiprasad ,"Lecturer", "9835269815", "anandiprasad990@gmail.com"));

        //adding items to automobile faculties array list
        automobileFacultieslist.add(new ListItem("Mr. Ashutosh Kumar", R.drawable.faculty_automobile_ashutosh, "Lecturer(HOD)", "8011242454", "ashutosh.mech@iitg.ac.in"));
        automobileFacultieslist.add(new ListItem("Mr. Adarsh Raj", R.drawable.faculty_automobile_adarsh, "lecturer", "8226864005", "adarsh4005raj@gmail.com"));

        //adding items to electrical faculties array list
        electricalFacultieslist.add(new ListItem("Mr. Daya Shankar Singh", R.drawable.faculty_electrical_dayashankar, "Lecturer(HOD)", "9430465850", "dssingh1770@gmail.com"));
        electricalFacultieslist.add(new ListItem("Mrs. Jaya Bharati", R.drawable.faculty_electrical_jayabharti, "Lecturer", "8777024993", "jbharatiele@gmail.com"));
        electricalFacultieslist.add(new ListItem("Mrs. Anuradha Krishna", 0, "Lecturer", "9431647490", "anuradha.krishna4@gmail.com"));
        electricalFacultieslist.add(new ListItem("Prashant Kumar", R.drawable.faculty_electrical_prashantkumar, "Lecturer", "8471053860", "prashant.kumar830@gmail.com"));
        electricalFacultieslist.add(new ListItem("Mrs. Puja Kumari", R.drawable.faculty_electrical_pujakumari, "Lecturer", "9861578760", ""));
        electricalFacultieslist.add(new ListItem("Ms. Diksha", R.drawable.faculty_electrical_diksha, "Lecturer", "6299560624", "singh.diksha1007@gmail.com"));

        //adding items to electronics faculties array list
        electronicsFacultieslist.add(new ListItem("Mrs. Roma Rani", R.drawable.faculties_electronics_romarani, "Lecturer(HOD)", "9798401079", "romangpp13@gmail.com"));
        electronicsFacultieslist.add(new ListItem("Mrs. Anushka Pradhan", R.drawable.faculties_electronics_anushkapradhan, "Lecturer", "8247220263", "a.pradhan2780@gmail.com"));
        electronicsFacultieslist.add(new ListItem("Ambul Prakash", R.drawable.faculties_electronics_ambulprakash, "Lecturer", "7991182933", "ambulngpp13@gmail.com"));
        electronicsFacultieslist.add(new ListItem("Pratyaya Amrit", R.drawable.faculties_electronics_pratyayamrit, "Lecturer", "9560888224", "pamrit619@gmail.com"));
        electronicsFacultieslist.add(new ListItem("Mrs. Anita Kumari", R.drawable.faculties_electronics_anita, "Lecturer", "9006313088", "anitangp13@gmail.com"));

        //adding items to mechanical faculties array list
        mechanicalFacultieslist.add(new ListItem("Mr. Nishant Kumar",R.drawable.faculties_mechanical_nishantkumar , "Lecturer(HOD)", "8800844748", "kumar.nishant57@gmail.com"));
        mechanicalFacultieslist.add(new ListItem("Mr. Shambhu Kumar", R.drawable.faculties_mechanical_shambhukumar, "Lecturer", "9852016356", "shambhu752@gmail.com"));
        mechanicalFacultieslist.add(new ListItem("Mr. Amit Narayan Mishra", R.drawable.faculties_mechanical_amitnarayan, "Lecturer", "8355857010", "amitprof93@gmail.com"));
        mechanicalFacultieslist.add(new ListItem("Mr. Ranjeet Kumar", R.drawable.faculties_mechanical_ranjeetsir, "Lecturer", "9931867592", "ranjeet31may@gmail.com"));
        mechanicalFacultieslist.add(new ListItem("Mr. Saurabh Kumar", R.drawable.faculties_mechanical_saurabhkumar, "Lecturer", "8825105191", "kumarsaurabhmech@gmail.com"));
        mechanicalFacultieslist.add(new ListItem("Praveen Kumar", R.drawable.faculties_mechanical_praveen, "Lecturer", "8267831241", "praveen23pro@gmail.com"));
        mechanicalFacultieslist.add(new ListItem("Mahesh kumar", R.drawable.faculties_mechanical_maheshkumar, "Lecturer", "7870521028", "mkgbitpg27@gmail.com"));


        //getting bundle containing branch name from previous fragment
        Bundle bundle = getArguments();
        if (bundle != null){
            String branchname = bundle.getString("branch");

            if (branchname.equals("automobile")){
                branchName.setText("Automobile Engineering");

                CustomListAdapter adapter = new CustomListAdapter(getContext(), automobileFacultieslist);
                listView.setAdapter(adapter);
            }

            if (branchname.equals("cse")){
                branchName.setText("Computer Science & Enginnering");

                CustomListAdapter adapter = new CustomListAdapter(getContext(), cseFacultieslist);
                listView.setAdapter(adapter);
            }

            if (branchname.equals("civil")) {
                branchName.setText("Civil Engineering");

                CustomListAdapter adapter = new CustomListAdapter(getContext(), civilFacultieslist);
                listView.setAdapter(adapter);

            }

            if (branchname.equals("electrical")) {
                branchName.setText("Electrical Engineering");

                CustomListAdapter adapter = new CustomListAdapter(getContext(), electricalFacultieslist);
                listView.setAdapter(adapter);
            }

            if (branchname.equals("electronics")) {
                branchName.setText("Electronics Engineering");

                CustomListAdapter adapter = new CustomListAdapter(getContext(), electronicsFacultieslist);
                listView.setAdapter(adapter);
            }

            if (branchname.equals("mechanical")) {
                branchName.setText("Mechanical Engineering");

                CustomListAdapter adapter = new CustomListAdapter(getContext(), mechanicalFacultieslist);
                listView.setAdapter(adapter);
            }


        }



        return view;
    }
}