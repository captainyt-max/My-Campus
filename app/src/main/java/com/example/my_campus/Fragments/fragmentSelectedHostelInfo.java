package com.example.my_campus.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.example.my_campus.HostelListAdapter;
import com.example.my_campus.HostellListItem;
import com.example.my_campus.ImageSliderAdapter;
import com.example.my_campus.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class fragmentSelectedHostelInfo extends Fragment {

    private TextView hostelName;
    private ListView listView;
    private ViewPager2 imageSlider;

    private ArrayList<HostellListItem> aryabhataHostellist = new ArrayList<>();
    private ArrayList<HostellListItem> buddhaHostellist = new ArrayList<>();
    private ArrayList<HostellListItem> chanakayaHostellist = new ArrayList<>();
    private ArrayList<HostellListItem> godavariHostellist = new ArrayList<>();

    public fragmentSelectedHostelInfo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_hostel_info, container, false);

        hostelName = view.findViewById(R.id.hostel);
        listView = view.findViewById(R.id.HostelListView);
        imageSlider = view.findViewById(R.id.imageSlider);

        // Populate hostel lists
        populateHostelData();

        // Get hostel name from previous fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            String hostel = bundle.getString("hostel");

            if (hostel != null) {
                hostelName.setText(getHostelFullName(hostel));
                setHostelData(hostel);
                loadImagesForHostel(hostel);
            }
        }

        return view;
    }

    private void populateHostelData() {
        // Aryabhata Hostel Data
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
    }

    private void setHostelData(String hostel) {
        ArrayList<HostellListItem> selectedHostelList = new ArrayList<>();

        switch (hostel) {
            case "aryabhata":
                selectedHostelList = aryabhataHostellist;
                break;
            case "chanakya":
                selectedHostelList = chanakayaHostellist;
                break;
            case "buddha":
                selectedHostelList = buddhaHostellist;
                break;
            case "godavari":
                selectedHostelList = godavariHostellist;
                break;
        }

        HostelListAdapter adapter = new HostelListAdapter(getContext(), selectedHostelList);
        listView.setAdapter(adapter);
    }

    private void loadImagesForHostel(String hostel) {
        List<Integer> imageList = Arrays.asList(R.drawable.chanakya);

        switch (hostel) {
            case "aryabhata":
                imageList = Arrays.asList(R.drawable.aryabhata_1, R.drawable.chanakya, R.drawable.chanakya);
                break;
            case "chanakya":
                imageList = Arrays.asList(R.drawable.chanakya, R.drawable.chanakay_1, R.drawable.chankaya_2);
                break;
            case "buddha":
                imageList = Arrays.asList(R.drawable.chankaya_3, R.drawable.chanakya,R.drawable.chanakya);
                break;
            case "godavari":
                imageList = Arrays.asList(R.drawable.chanakya, R.drawable.chanakya, R.drawable.chanakya);
                break;
        }

        ImageSliderAdapter adapter = new ImageSliderAdapter(imageList);
        imageSlider.setAdapter(adapter);
    }

    private String getHostelFullName(String hostel) {
        switch (hostel) {
            case "aryabhata":
                return "Aryabhatta Girls Hostel";
            case "chanakya":
                return "Chanakya Boys Hostel";
            case "buddha":
                return "Buddha Boys Hostel";
            case "godavari":
                return "Godavari Boys Hostel";
            default:
                return "Unknown Hostel";
        }
    }
}
