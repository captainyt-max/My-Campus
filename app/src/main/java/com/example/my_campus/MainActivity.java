package com.example.my_campus;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Dialog;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.my_campus.Fragments.fragmentAdmin;
import com.example.my_campus.Fragments.fragmentGuidance;
import com.example.my_campus.Fragments.fragmentHomepage;
import com.example.my_campus.Fragments.fragmentHostelInfo;
import com.example.my_campus.Fragments.fragmentSyllabus;
import com.example.my_campus.Fragments.fragmentfacultiesinfo;
import com.example.my_campus.Fragments.fragmentnavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 0;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final String CHANNEL_ID = "campus_activity_channel";
    private String lastMessage = null;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }


        // checking login state, if not logged in redirect to login page, else start the home layout
        if (!loginState.getLoginState(this)){
            Intent intent = new Intent(MainActivity.this, activityLogin.class);
            startActivity(intent);
            finish();
        }

        // Check and request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }

        Intent serviceIntent = new Intent(this, notificationService.class);
        startService(serviceIntent);



        //Setting Admin Section Visibility
        DocumentReference docRef = db.collection("users").document(loginState.getUserEmail(this));
        docRef.addSnapshotListener((documentSnapshot, e) ->{
            if(e != null){
                // Handle the error
                return;
            }
            if (documentSnapshot != null && documentSnapshot.exists()){
                String role = documentSnapshot.getString("role");
                Toast.makeText(this, role, Toast.LENGTH_SHORT).show();
                Menu menu = navigationView.getMenu();
                MenuItem admin = menu.findItem(R.id.navAdmin);
                assert role != null;
                admin.setVisible(role.equals("admin"));
            }

        });

        //Notification Testing





        // starting home layout
        setContentView(R.layout.home_activity);
        utility ut = new utility();
        if(!ut.isNetworkAvailable(this)){
            Toast.makeText(MainActivity.this, "Offline", Toast.LENGTH_SHORT).show();
        }




        // Initialising firestore database




        //Set status bar and navigation bar color
        Window window = getWindow();
        // Set status bar color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.appAscent));
        // Set navigation bar color
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.appAscent));

// You can use this fallback if WindowInsetsController is giving issues

        // set homepage fragment as default on start
        fragmentHomepage fragmentHomepage = new fragmentHomepage();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainLayout, fragmentHomepage);
        fragmentTransaction.commit();


        //finding views of main layout(Drawer layout) by id
        drawerLayout = findViewById(R.id.drawer_layout);
        ConstraintLayout menuButton = findViewById(R.id.menuButton);
        navigationView = findViewById(R.id.navigation_view);
        ImageView profileIconHome = findViewById(R.id.profileIconHome);


        // Temporary - Setting profile image
        profileIconHome.setImageResource(R.drawable.ic_default_user);

        //Opening profile Dialog box
        profileIconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                openProfileDialog();  //calling function created to handle dialog box
            }
        });



        // open sidebar navigation drawer
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);  // Close sidebar if open
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);  // Open sidebar if closed
                }
            }
        });




        // action for every item in navigation drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navHome){
                    Toast.makeText(MainActivity.this, "Clicked On Home", Toast.LENGTH_SHORT).show();
                    fragmentHomepage fragmentHomepage = new fragmentHomepage();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentHomepage);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);

                }

                if (itemId == R.id.navNavigation){
                    Toast.makeText(MainActivity.this, "Clicked On Navigation", Toast.LENGTH_SHORT).show();
                    fragmentnavigation fragmentnavigation = new fragmentnavigation();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentnavigation);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (itemId == R.id.navGuidance){
                    fragmentGuidance fragment_Guidance = new fragmentGuidance();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragment_Guidance);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (itemId == R.id.navHostel){
                    Toast.makeText(MainActivity.this, "Clicked On Hostel", Toast.LENGTH_SHORT).show();
                    fragmentHostelInfo fragmentHostelInfo = new fragmentHostelInfo();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentHostelInfo);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (itemId == R.id.navFaculties){
                    Toast.makeText(MainActivity.this, "Clicked On Faculties", Toast.LENGTH_SHORT).show();
                    fragmentfacultiesinfo fragmentfacultiesinfo = new fragmentfacultiesinfo();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentfacultiesinfo);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (itemId == R.id.navSyllabus){
                    Toast.makeText(MainActivity.this, "Clicked On Syllabus", Toast.LENGTH_SHORT).show();
                    fragmentSyllabus fragmentsyllabus = new fragmentSyllabus();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentsyllabus);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (itemId == R.id.navRoutine){
                    Toast.makeText(MainActivity.this, "Clicked On Routine", Toast.LENGTH_SHORT).show();
                }

                if (itemId == R.id.nav_Medical){
                    Toast.makeText(MainActivity.this, "Clicked On Medical", Toast.LENGTH_SHORT).show();
                }

                if (itemId == R.id.navNearbyPlaces){
                    Toast.makeText(MainActivity.this, "Clicked On Nearby Places", Toast.LENGTH_SHORT).show();
                }

                if (itemId == R.id.navAdmin){
                    if(!ut.isNetworkAvailable(getBaseContext())){
                        Toast.makeText(MainActivity.this, "This feature is not available offline", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    fragmentAdmin fragmentadmin = new fragmentAdmin();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentadmin);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });

    }


    public void openProfileDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.profile_dialouge_box);

        // Ensure dialog window is not null
        if (dialog.getWindow() != null) {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity = Gravity.TOP; // Set the gravity to Top
            params.y = 40; // Set Y offset from the top
            dialog.getWindow().setAttributes(params);

            dialog.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.96),
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.profile_dialouge_background);
        }

        // Finding views from profile box
        ConstraintLayout logoutButton = dialog.findViewById(R.id.logoutButton);
        ImageView profileIcon = dialog.findViewById(R.id.profileIcon);
        TextView name = dialog.findViewById(R.id.name);
        TextView branch = dialog.findViewById(R.id.branch);
        TextView year = dialog.findViewById(R.id.year);
        TextView rollNo = dialog.findViewById(R.id.rollno);
        TextView phoneNo = dialog.findViewById(R.id.phoneNo);
        TextView emailId = dialog.findViewById(R.id.emailId);
        TextView changePassword = dialog.findViewById(R.id.changePassword);
        TextView changeProfile = dialog.findViewById(R.id.changeProfile);

        name.setText(loginState.getUserName(this));
        branch.setText(loginState.getUserBranch(this));
        year.setText(loginState.getUserYear(this));
        rollNo.setText(loginState.getUserRollNo(this));
        emailId.setText(loginState.getUserEmail(this));
        phoneNo.setText(loginState.getUserMobileNumber(this));
        profileIcon.setImageResource(R.drawable.ic_default_user);

        // Set onClickListeners
        logoutButton.setOnClickListener(view -> {
            utility ut = new utility();
            ut.dialogBox(this, "Are you sure to logout ?", new utility.DialogCallback() {
                @Override
                public void onConfirm() {
                    Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                    clickAnimation(view);
                    loginState.setLoginState(MainActivity.this, false);
                    Intent loginIntent = new Intent(MainActivity.this, activityLogin.class);
                    startActivity(loginIntent);
                    finish();
                }
                @Override
                public void onCancel() {
                    //Do nothing
                }
            });

        });

        changeProfile.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Change Profile", Toast.LENGTH_SHORT).show();
            clickAnimation(view);
        });

        changePassword.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Change Password", Toast.LENGTH_SHORT).show();
            clickAnimation(view);
        });

        dialog.show();
    }


    public static void clickAnimation(View v){
        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100);
            }
        });
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with notifications
                // You can initialize notifications here if needed
            } else {
                // Permission denied, show a message or handle accordingly
            }
        }
    }

}