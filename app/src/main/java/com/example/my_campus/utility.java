package com.example.my_campus;

import static android.app.PendingIntent.getActivity;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

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
        ConstraintLayout btnCancel = dialog.findViewById(R.id.btnCancel);
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

}
