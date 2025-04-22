package com.example.my_campus.Fragments;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.os.FileObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_campus.R;
import com.example.my_campus.loginState;
import com.example.my_campus.utility;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class fragmentRoutine extends Fragment {


    private TextView pdfName, btnText;
    private ConstraintLayout btnDownload;
    private String documentName;
    private String fieldName;
    private String pdfUrl;
    private ImageView pdfIcon;
    utility ut = new utility();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private File file;
    private long downloadId;
    private FileObserver fileObserver;
    private boolean isReceiverRegistered = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_routine, container, false);
        pdfName = view.findViewById(R.id.pdfName);
        btnDownload = view.findViewById(R.id.btnDonwload);
        pdfIcon = view.findViewById(R.id.pdf_icon);
        btnText = view.findViewById(R.id.btnText);

//        requireContext().registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));



        Map<String, String> branchMap = new HashMap<>();
        branchMap.put("Computer Science & Engineering", "cse");
        branchMap.put("Civil Engineering", "civil");
        branchMap.put("Electrical Engineering", "electrical");
        branchMap.put("Electronics Engineering", "electronics");
        branchMap.put("Mechanical Engineering", "mech");
        branchMap.put("Automobile Engineering", "auto");

        documentName = branchMap.get(loginState.getUserBranch(requireContext()));
        fieldName = loginState.getUserYear(requireContext());

        //Getting pdf
        DocumentReference docref = db.collection("routine").document(documentName);
        docref.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Toast.makeText(getContext(), "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (documentSnapshot != null && documentSnapshot.exists()){
                pdfUrl = documentSnapshot.getString(fieldName);
                String fileName = documentSnapshot.getString(fieldName + " File Name");
                if (pdfUrl != null && !pdfUrl.isEmpty()){
                    btnDownload.setVisibility(View.VISIBLE);
                    pdfIcon.setVisibility(View.VISIBLE);
                    pdfName.setText(fileName);
                }
                else {
                    btnDownload.setVisibility(View.GONE);
                    pdfIcon.setVisibility(View.GONE);
                    pdfName.setText("No such file uploaded");
                }
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+ "/Pocket Campus/Routine/", "Routine.pdf" );
                if (file.exists()){
                    btnText.setText("Open");
                }
                else {
                    btnText.setText("Download");
                }

            }
        });

        btnDownload.setOnClickListener(click -> {
            ut.clickAnimation(btnDownload);
            if (file.exists()){
                ut.openPdf(requireContext(), file);
            }
            else {
                downloadId = ut.downloadFile(requireContext(), pdfUrl, file);
            }
        });

        return view;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.d("Routine", "completed Id : " + id);
            if (downloadId == id) {
                Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show();
                btnText.setText("Open"); // Refresh the adapter
            }
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        if (!isReceiverRegistered) {
//            requireContext().registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiverSafely();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiverSafely();
    }

    private void unregisterReceiverSafely() {
        try {
            if (isReceiverRegistered) {
                requireContext().unregisterReceiver(broadcastReceiver);
                isReceiverRegistered = false;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // Prevents the app from crashing
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Fragment fragment = getParentFragmentManager().findFragmentByTag("fragmentHomepage");

                        if (fragment == null || !fragment.isVisible()) {
                            // User is NOT on homepage → go to homepage
                            fragmentHomepage homePageFragment = new fragmentHomepage();
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.mainLayout, homePageFragment, "fragmentHomepage");
                            transaction.commit();
                        } else {
                            // User is already on homepage → exit app
                            requireActivity().finish();
                        }
                    }
                }
        );
    }
}
