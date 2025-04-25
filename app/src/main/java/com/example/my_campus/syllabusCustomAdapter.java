package com.example.my_campus;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class syllabusCustomAdapter extends BaseAdapter {
    public Context context;
    private String branchName;
    private List<String> semesterNames;
    private List<String> pdfUrls;
    private utility ut = new utility();
    private Map<Long, Integer> downloadPositionsMap = new HashMap<>();
    private long downloadID;

    public syllabusCustomAdapter(){}

    public syllabusCustomAdapter(Context context, String branchName, List<String> semesterNames, List<String> pdfUrls) {
        this.context = context;
        this.branchName= branchName;
        this.semesterNames = semesterNames;
        this.pdfUrls = pdfUrls;

//        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int getCount() {
        return semesterNames.size();
    }

    @Override
    public Object getItem(int position) {
        return semesterNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.syllabus_list_items, parent, false);
        }

        TextView semesterTextView = convertView.findViewById(R.id.pdfName);
        ConstraintLayout openButton = convertView.findViewById(R.id.btnOpen);
        TextView btnText = convertView.findViewById(R.id.btnText);

        // Set semester name
        semesterTextView.setText(semesterNames.get(position));

        String fileName = semesterNames.get(position)+" "+branchName+ ".pdf";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Pocket Campus/Syllabus", fileName);
        // Set button text based on file existence
        if (file.exists()) {
            btnText.setText("Open");
        } else {
            btnText.setText("Download");
        }

        // Set click listener on the "Open" button to open the PDF
        openButton.setOnClickListener(v -> {
            if (file.exists()) {
                ut.openFile(context, file);
            } else {
                ut.downloadFilePr(context, file, pdfUrls.get(position), ()->{
                    Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show();
                    btnText.setText("Open");
                });
            }
        });
        return convertView;
    }


    public final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                notifyDataSetChanged(); // Refresh the adapter
            }
        }
    };


    public  void unregisterReceiver() {
        if (context != null){
            context.unregisterReceiver(onDownloadComplete);
        }
    }


}
