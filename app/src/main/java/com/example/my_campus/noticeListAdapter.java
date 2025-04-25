package com.example.my_campus;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class noticeListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<noticeListItems> listItems;
    private utility ut = new utility();

    public noticeListAdapter (Context context, ArrayList<noticeListItems> listItems){
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.notice_list_item, parent, false);
        }

        noticeListItems currentItem = (noticeListItems) getItem(position);

        TextView fileName = convertView.findViewById(R.id.fileName);
        TextView uploadDate = convertView.findViewById(R.id.uploadDate);
        ConstraintLayout btnOpen = convertView.findViewById(R.id.btnOpen1);

        String fileNameText = currentItem.getFileName();
        String uploadTimeText = currentItem.getUploadTime();
        String uploadedByText = currentItem.getUploadedBy();
        String downloadUrl = currentItem.getDownloadUrl();
        String documentId = currentItem.getDocumentId();
        String fileSizeText = currentItem.getFileSize();

        // Truncate file name if it's too long
        if (fileNameText.length() > 15) {
            fileName.setText(fileNameText.substring(0, 15) + "....");
        } else {
            fileName.setText(fileNameText);
        }
        uploadDate.setText(currentItem.getUploadDate());

        // Create file object specific to this item
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Pocket Campus/Notices", fileNameText);

        btnOpen.setOnClickListener(click -> {
            ut.clickAnimation(click);
            if (file.exists()) {
                ut.openFile(context, file);
            } else {
                if (!ut.isNetworkAvailable(context)){
                    Toast.makeText(context, "offline", Toast.LENGTH_SHORT).show();
                    return;
                }
                ut.downloadFilePr(context, file, downloadUrl, ()->{
                    Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show();
                });
            }
        });

        convertView.setOnLongClickListener(view -> {
            showContextMenu(view, documentId, downloadUrl, fileNameText, fileSizeText, uploadTimeText, uploadedByText);
            return true;
        });

        return convertView;
    }


    private void showContextMenu(View anchor, String documentId, String fileUrl, String fileName, String fileSize, String uploadTime, String uploadedBy) {
        PopupMenu popupMenu = new PopupMenu(context, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.notice_context_menu, popupMenu.getMenu());  // Inflate the menu

        if (loginState.getUserRole(context).equals("admin")) {
            // Make the "Delete" menu item visible
            MenuItem deleteItem = popupMenu.getMenu().findItem(R.id.menu_delete);
            if (deleteItem != null) {
                deleteItem.setVisible(true);
                deleteItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }

        // Set a click listener for menu items
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_delete) {
                if (!ut.isNetworkAvailable(context)) {
                    Toast.makeText(context, "Offline", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    deleteFileFromFirestoreAndStorage(documentId, fileUrl);
                    return true;
                }
            }

            if (item.getItemId() == R.id.menu_info) {
                showFileInfoDialog(fileName, fileSize, uploadTime, uploadedBy);
            }
            return false;
        });

        popupMenu.show();
    }

    private void deleteFileFromFirestoreAndStorage(String documentId, String fileUrl) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        if (fileUrl.isEmpty()){
            return;
        }
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl);

        firestore.collection("notice pdfs").document(documentId)
                .delete()
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                });
        storageReference.delete().addOnSuccessListener( success -> {
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener( e -> {
            Toast.makeText(context, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void showFileInfoDialog(String fileName, String fileSize, String uploadTime, String uploadedBy) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.file_info_dialog, null);

        TextView uploaderName = dialogView.findViewById(R.id.uploaderName);
        TextView uploaderBranch = dialogView.findViewById(R.id.uploaderBrnach);
        TextView uploaderYear = dialogView.findViewById(R.id.uploaderYear);
        TextView uploadedFileName = dialogView.findViewById(R.id.uploadedFileName);
        TextView fileUploadTime = dialogView.findViewById(R.id.uploadTime);
        TextView uploadSize = dialogView.findViewById(R.id.uploadSize);
        ImageView uploaderImage = dialogView.findViewById(R.id.uploaderImage);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uploadedBy)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        String name = snapshot.getString("name");
                        String branch = snapshot.getString("branch");
                        String year = snapshot.getString("year");
                        String imageUrl = snapshot.getString("profileImage");
                        uploaderName.setText(name);
                        uploaderBranch.setText(branch);
                        uploaderYear.setText(year);
                        uploadedFileName.setText(fileName);
                        fileUploadTime.setText(uploadTime);
                        uploadSize.setText(fileSize);
                        Glide.with(context)
                                .load(imageUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache both original and resized image
                                .placeholder(R.drawable.ic_default_user)
                                .error(R.drawable.ic_default_user)
                                .fitCenter()
                                .circleCrop()
                                .into(uploaderImage);

                        uploaderImage.setOnClickListener( click -> {
                            ut.navigateToProfileImage(context, imageUrl, name, "noticeInfo");
                        });
                    }
                });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog fileInfoDialog = builder.create();
        Objects.requireNonNull(fileInfoDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        fileInfoDialog.show();
    }

}
