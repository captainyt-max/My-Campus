package com.example.my_campus;

import static android.app.PendingIntent.getActivity;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class utility {

    public interface DialogCallback {
        void onConfirm();
        void onCancel();
    }

    public void dialogBox(Context context, String message, DialogCallback callback){
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null.");
        }

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_box);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ConstraintLayout btnConfirm = dialog.findViewById(R.id.btnConfirm);
        ConstraintLayout btnCancel = dialog.findViewById(R.id.btnHide);
        TextView dialogMassage = dialog.findViewById(R.id.dialogMassage);

        dialogMassage.setText(message);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callback.onConfirm(); // Call the onConfirm callback
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callback.onCancel();
            }
        });
        dialog.show();
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities != null &&
                        (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            }
        }
        return false;
    }

    public String getDateTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM hh:mm a");
        String capturedTime = dateFormat.format(calendar.getTime());
        assert capturedTime != null;
        return capturedTime;
    }

    public void clickAnimation(View v){
        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100);
            }
        });
    }

    public void showUploadProgressDialog(Context context, UploadTask uploadTask){
        // Create and configure the dialog
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        TextView tvProgress = dialog.findViewById(R.id.tvProgress);
        ConstraintLayout btnCancel = dialog.findViewById(R.id.btnCancel);
        ConstraintLayout btnHide = dialog.findViewById(R.id.btnHide);

        btnCancel.setOnClickListener(v -> {
            if (uploadTask != null) {
                uploadTask.cancel();
                tvProgress.setText("Upload Canceled");// Cancel the upload task
                new Handler().postDelayed(dialog::dismiss, 2000);    // Dismiss the dialog
            }
        });

        btnHide.setOnClickListener(v -> {
            dialog.dismiss(); // Just close the dialog, but let the upload continue
        });

        // Show the dialog
        dialog.show();

        uploadTask.addOnProgressListener(snapshot -> {
            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            progressBar.setProgress((int) progress);
            tvProgress.setText(String.format("Uploaded: %.2f%%", progress));
        }).addOnSuccessListener(snapshot -> {
            tvProgress.setText("Upload Complete!");
            new Handler().postDelayed(dialog::dismiss, 1000); // Dismiss after 1 second
        }).addOnFailureListener(e -> {
            if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_CANCELED) {
                tvProgress.setText("Upload Canceled");
            } else {
                tvProgress.setText("Upload Failed: " + e.getMessage());
            }
            new Handler().postDelayed(dialog::dismiss, 2000); // Dismiss after 2 seconds
        });

    }

}
