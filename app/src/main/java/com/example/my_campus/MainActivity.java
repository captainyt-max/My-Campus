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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.example.my_campus.Fragments.fragmentLinks;
import com.example.my_campus.Fragments.fragmentNearbyPlaces;
import com.example.my_campus.Fragments.fragmentRoutine;
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
import com.google.rpc.context.AttributeContext;


public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 0;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final String CHANNEL_ID = "campus_activity_channel";
    private String lastMessage = null;
    private final utility ut = new utility();
    private ImageView profileIconHome;

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

        //Fetching profile image url
        DocumentReference docref = db.collection("users").document(loginState.getUserEmail(this));
        docref.addSnapshotListener((documentSnapshot, e) -> {
            if( e != null){
                Toast.makeText(this, "Profile Image error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (documentSnapshot != null && documentSnapshot.exists()){
                String profileImageUrl = documentSnapshot.getString("profileImage");
                loginState.setProfileImageUrl(this, profileImageUrl);
            }
        });

        // Check and request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
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
                String profileImageUrl = documentSnapshot.getString("profileImage");
                ut.setProfileImage(this, profileImageUrl, profileIconHome);
                loginState.setUserRole(this, role);
                Toast.makeText(this, role, Toast.LENGTH_SHORT).show();
                Menu menu = navigationView.getMenu();
                MenuItem admin = menu.findItem(R.id.navAdmin);
                assert role != null;
                admin.setVisible(role.equals("admin"));
            }
        });


        // starting home layout
        setContentView(R.layout.home_activity);
        utility ut = new utility();

        if(!ut.isNetworkAvailable(this)){
            Toast.makeText(MainActivity.this, "Offline", Toast.LENGTH_SHORT).show();
        }


        //Set status bar and navigation bar color
        Window window = getWindow();
        // Set status bar color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.appAscent));
        // Set navigation bar color
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.appAscent));


        // set homepage fragment as default on start
        fragmentHomepage fragmentHomepage = new fragmentHomepage();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainLayout, fragmentHomepage);
        fragmentTransaction.commit();


        //finding views of main layout(Drawer layout) by id+
        drawerLayout = findViewById(R.id.drawer_layout);
        ConstraintLayout menuButton = findViewById(R.id.menuButton);
        navigationView = findViewById(R.id.navigation_view);
        profileIconHome = findViewById(R.id.profileIconHome);



        //Opening profile Dialog box
        profileIconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ut.clickAnimation(view);
                openProfileDialog();  //calling function created to handle dialog box
            }
        });

        ut.setProfileImage(this, loginState.getProfileImageUrl(this), profileIconHome);



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
                    fragmentHomepage fragmentHomepage = new fragmentHomepage();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentHomepage);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);

                }

                if (itemId == R.id.navNavigation){
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
                    fragmentHostelInfo fragmentHostelInfo = new fragmentHostelInfo();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentHostelInfo);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (itemId == R.id.navFaculties){
                    fragmentfacultiesinfo fragmentfacultiesinfo = new fragmentfacultiesinfo();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentfacultiesinfo);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (itemId == R.id.navSyllabus){
                    fragmentSyllabus fragmentsyllabus = new fragmentSyllabus();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentsyllabus);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (itemId == R.id.navRoutine){
                    fragmentRoutine fragmentroutine = new fragmentRoutine();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentroutine);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (itemId == R.id.nav_Links){
                    fragmentLinks fragmentlinks = new fragmentLinks();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentlinks);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (itemId == R.id.navNearbyPlaces){
                    fragmentNearbyPlaces fragmentNearbyPlaces = new fragmentNearbyPlaces();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainLayout, fragmentNearbyPlaces);
                    fragmentTransaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (itemId == R.id.navAdmin){
                    if(!ut.isNetworkAvailable(getBaseContext())){
                        Toast.makeText(MainActivity.this, "This feature is not available offline", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    else {
                        fragmentAdmin fragmentadmin = new fragmentAdmin();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainLayout, fragmentadmin);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
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
        int profileImageRes = (R.drawable.ic_default_user);

        ut.setProfileImage(this, loginState.getProfileImageUrl(this), profileIcon);

        profileIcon.setOnClickListener( click -> {
            ut.navigateToProfileImage(this,profileImageRes, "Profile Photo", "profileImage");
        });

        // Set onClickListeners
        logoutButton.setOnClickListener(view -> {
            utility ut = new utility();
            ut.dialogBox(this, "Are you sure to logout ?", new utility.DialogCallback() {
                @Override
                public void onConfirm() {
                    Toast.makeText(MainActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
                    ut.clickAnimation(view);
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
            ut.clickAnimation(view);
            Intent intent2 = new Intent(MainActivity.this, activityEditProfile.class);
            startActivity(intent2);
        });

        changePassword.setOnClickListener(view -> {
            ut.clickAnimation(view);
            Intent intent1 = new Intent(MainActivity.this, activityChangePassword.class);
            startActivity(intent1);
        });

        dialog.show();
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with notifications
                // You can initialize notifications here if needed
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No notification permission found, go to permissions settings and grant", Toast.LENGTH_SHORT).show();
            }
        }
    }

}