package com.example.my_campus.Fragments;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.my_campus.R;

public class fragmentSelectednavigation extends Fragment {
    private WebView webView;

    public fragmentSelectednavigation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_selectednavigation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView title = view.findViewById(R.id.tittle);
        webView = view.findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        Bundle bundle = getArguments();
        if (bundle != null) {
            String selectedTittle = bundle.getString("navigation");
            String mapHtml = "";
            if (selectedTittle != null) {
                if (selectedTittle.equals("campus")) {
                    title.setText("College Campus Map");
                    mapHtml = "<iframe src=\"https://www.google.com/maps/d/embed?mid=1TAmar8DlRbwC095t2dkLE3aZXIQtO9k&ehbc=2E312F\" "
                            + "style=\"width:100%; height:100vh; border:0;\" "
                            + "allowfullscreen"
                            + "loading=\"lazy\">"
                            + "</iframe>";


                } else if (selectedTittle.equals("hostel")) {
                    title.setText("Hostel Map");
                    mapHtml = "<iframe src=\"https://www.google.com/maps/d/embed?mid=1B_9vhN7VTzhWRIv95uyUW3V0G0zbHSU&hl=en&ehbc=2E312F\" "
                            + "style=\"width:100%; height:100vh; border:0;\" "
                            + "allowfullscreen "
                            + "loading=\"lazy\">"
                            + "</iframe>";

                }

                String htmlData = "<html><body style='margin:0;padding:0;'><div style='width:100%; height:100vh;'>" + mapHtml + "</div></body></html>";
                webView.loadData(htmlData, "text/html", "UTF-8");
            }

        }
    }
}
