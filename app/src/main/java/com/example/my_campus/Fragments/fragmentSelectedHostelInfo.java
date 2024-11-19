package com.example.my_campus.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.my_campus.CustomListAdapter;
import com.example.my_campus.HostelListAdapter;
import com.example.my_campus.HostellListItem;
import com.example.my_campus.R;
import com.google.firestore.bundle.BundledDocumentMetadata;

import java.util.ArrayList;


public class fragmentSelectedHostelInfo extends Fragment {

    ArrayList<HostellListItem> aryabhataHostellist = new ArrayList<>();
    ArrayList<HostellListItem> buddhaHostellist = new ArrayList<>();
    ArrayList<HostellListItem> chanakayaHostellist = new ArrayList<>();
    ArrayList<HostellListItem> godavariHostellist = new ArrayList<>();

    TextView hostelName;
    ListView listView;


    public fragmentSelectedHostelInfo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_hostel_info, container, false);

        hostelName = view.findViewById(R.id.hostel);
        listView = view.findViewById(R.id.HostelListView);

        //adding item to aryabhata array list
        aryabhataHostellist.add(new HostellListItem("Dr. Alka Kumari", 0, "Chief warden", "7549127280"));
        aryabhataHostellist.add(new HostellListItem("Roma Rani", 0, "Warden", ""));
        aryabhataHostellist.add(new HostellListItem("Prof. Binita Kumari", 0, "Warden Superitendent", ""));
        aryabhataHostellist.add(new HostellListItem("Dr. Alka Kumari", 0, "Chief warden", "7549127280"));
        aryabhataHostellist.add(new HostellListItem("Roma Rani", 0, "Warden", ""));
        aryabhataHostellist.add(new HostellListItem("Prof. Binita Kumari", 0, "Warden Superitendent", ""));
        aryabhataHostellist.add(new HostellListItem("", 0, "Mess Chief Incharge", ""));
        aryabhataHostellist.add(new HostellListItem("", 0, "Mess Incharge", ""));

        //adding item to chankaya array list
        chanakayaHostellist.add(new HostellListItem("Dr. R N Singh", 0 ,"Chief Warden", ""));
        chanakayaHostellist.add(new HostellListItem("Prof. Ranjit Kumar", 0, "Boys Hostel Superitendent", ""));
        chanakayaHostellist.add(new HostellListItem("Prof. Prashant Kumar", 0, "Boys Hostel Superitendent",""));
        chanakayaHostellist.add(new HostellListItem("Prof. Anandi Prasad Yadav", 0, "Boys Warden", ""));
        chanakayaHostellist.add(new HostellListItem("Prof. Ramendra Rahul", 0, "Boys Assistant Warden", ""));
        chanakayaHostellist.add(new HostellListItem("Shri Lalo Choudhary", 0, "Boys Assistant Warden", ""));
        chanakayaHostellist.add(new HostellListItem("Mr. Shubham Kumar Vats", 0, "Floor Incharge (1st + Ground Floor)", ""));
        chanakayaHostellist.add(new HostellListItem("Mr. Binit Kumar (NATS) ", 0, "Floor Incharge (2nd Floor)", ""));
        chanakayaHostellist.add(new HostellListItem("Mr. Rahul Kumar(Instractor)", 0, "Floor Incharge (3rd + 4th Floor)", ""));
        chanakayaHostellist.add(new HostellListItem("", 0, "Mess Chief Incharge", ""));
        chanakayaHostellist.add(new HostellListItem("", 0, "Mess Incharge", ""));



        //adding item to buddha array list
        buddhaHostellist.add(new HostellListItem("", 0, "", ""));
        buddhaHostellist.add(new HostellListItem("Dr. R N Singh", 0, "Chief Warden", ""));
        buddhaHostellist.add(new HostellListItem("Prof. Ranjit Kumar", 0, "Boys Hostel Superitendent", ""));
        buddhaHostellist.add(new HostellListItem("Prof. Prashant Kumar", 0, "Boys Hostel Superitendent", ""));
        buddhaHostellist.add(new HostellListItem("Prof. Anandi Prasad Yadav", 0, "Boys Warden", ""));
        buddhaHostellist.add(new HostellListItem("Prof. Ramendra Rahul", 0, "Boys Assistant Warden", ""));
        buddhaHostellist.add(new HostellListItem("Shri Lalo Choudhary", 0, "Boys Assistant Warden", ""));
        buddhaHostellist.add(new HostellListItem("Mr. Binit Kumar (NATS)", 0, "Floor Incharge (2nd + 3rd Floor)", ""));
        buddhaHostellist.add(new HostellListItem("Mr. Rahul Kumar", 0, "Floor Incharge (1st + Ground Floor)", ""));
        buddhaHostellist.add(new HostellListItem("", 0, "Mess Chief Incharge", ""));
        buddhaHostellist.add(new HostellListItem("", 0, "Mess Incharge",""));


        //adding item to godavari array list
        godavariHostellist.add(new HostellListItem("Dr. R N Singh", 0, "Chief Warden", ""));
        godavariHostellist.add(new HostellListItem("Prof. Ranjit Kumar", 0, "Boys Hostel Superitendent", ""));
        godavariHostellist.add(new HostellListItem("Prof. Prashant Kumar", 0, "Boys Hostel Superitendent", ""));
        godavariHostellist.add(new HostellListItem("Prof. Anandi Prasad Yadav", 0, "Boys Warden", ""));
        godavariHostellist.add(new HostellListItem("Prof. Ramendra Rahul", 0, "Boys Assistant Warden", ""));
        godavariHostellist.add(new HostellListItem("Shri Lalo Choudhary", 0, "Boys Assistant Warden", ""));
        godavariHostellist.add(new HostellListItem("Mr. Ganesh Kumar", 0, "Floor Incharge (1st + Ground Floor)", ""));
        godavariHostellist.add(new HostellListItem("Mr. Dinkar Dayal Nirala ", 0, "Floor Incharge (2nd Floor)", ""));
        godavariHostellist.add(new HostellListItem("", 0, "Mess Chief Incharge", ""));
        godavariHostellist.add(new HostellListItem("", 0, "Mess Incharge", ""));



        // getting bundle containing hostel name from previous fragment
        Bundle bundle = getArguments();
        if (bundle != null){
            String hostelname = bundle.getString("hostel");

            if (hostelname.equals("aryabhata")){
                hostelName.setText("Aryabhatta Girls Hostel");

                HostelListAdapter adapter = new HostelListAdapter(getContext(), aryabhataHostellist);
                listView.setAdapter(adapter);
            }
            if (hostelname.equals("chanakya")){
                hostelName.setText("Chanakaya Boys Hostel");

                HostelListAdapter adapter = new HostelListAdapter(getContext(), chanakayaHostellist);
                listView.setAdapter(adapter);
            }
            if (hostelname.equals("godavari")){
                hostelName.setText("Godavari Boys Hostel");

                HostelListAdapter adapter = new HostelListAdapter(getContext(), godavariHostellist);
                listView.setAdapter(adapter);
            }
            if (hostelname.equals("buddha")){
                hostelName.setText("Buddha Boys Hostel");

                HostelListAdapter adapter = new HostelListAdapter(getContext(), buddhaHostellist);
                listView.setAdapter(adapter);
            }
        }


        return view;
    }
}


