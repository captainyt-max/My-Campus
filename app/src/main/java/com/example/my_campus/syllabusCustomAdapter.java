package com.example.my_campus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class syllabusCustomAdapter extends BaseAdapter {
    private Context context;
    private List<String> semesterNames;
    private List<String> pdfUrls;

    public syllabusCustomAdapter(Context context, List<String> semesterNames, List<String> pdfUrls) {
        this.context = context;
        this.semesterNames = semesterNames;
        this.pdfUrls = pdfUrls;
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

        // Set semester name
        semesterTextView.setText(semesterNames.get(position));

        // Set click listener on the "Open" button to open the PDF
        openButton.setOnClickListener(v -> {
            String pdfUrl = pdfUrls.get(position);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl));
            context.startActivity(intent);
        });

        return convertView;
    }
}
