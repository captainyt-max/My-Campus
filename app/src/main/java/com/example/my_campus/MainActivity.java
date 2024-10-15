package com.example.my_campus;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.my_campus.Fragments.fragmentHomepage;
import com.example.my_campus.Fragments.fragmentHostelInfo;
import com.example.my_campus.Fragments.fragmentSyllabus;
import com.example.my_campus.Fragments.fragmentfacultiesinfo;
import com.example.my_campus.Fragments.fragmentnavigation;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private NavigationView navigationView;
    private ImageButton profileIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_activity);

        //Set status bar and navigation bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.appAscent));
        getWindow().setNavigationBarColor(ContextCompat.getColor(MainActivity.this, R.color.appAscent));


        // set homepage fragment as default on start
        fragmentHomepage fragmentHomepage = new fragmentHomepage();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainLayout, fragmentHomepage);
        fragmentTransaction.commit();


        //finding views of main layout(Drawer layout) by id
        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.menuButton);
        navigationView = findViewById(R.id.navigation_view);
        profileIcon = findViewById(R.id.profileIcon);


        // Temporary - Setting profile image
        profileIcon.setImageResource(R.drawable.ic_home_profile);

        //Opening profile Dialog box
        profileIcon.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(MainActivity.this, "Clicked On Guidance", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Clicked On admin", Toast.LENGTH_SHORT).show();
                }


                return false;
            }
        });

    }


    //function to handle profile dialog box
    public void openProfileDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.profile_dialouge_box);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.TOP; // Set the gravity to Top

        params.y = 40;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setLayout(
                (int) (getResources().getDisplayMetrics().widthPixels * 0.96),
                WindowManager.LayoutParams.WRAP_CONTENT
                //(int) (getResources().getDisplayMetrics().heightPixels * 0.43)
        );
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.profile_dialouge_background);
        dialog.show();

        //finding views from profile box
        ConstraintLayout logoutButton = dialog.findViewById(R.id.logoutButton);
        ImageView profileIcon = dialog.findViewById(R.id.profileIcon);
        TextView name = dialog.findViewById(R.id.name);
        TextView branch = dialog.findViewById(R.id.branch);
        TextView semester = dialog.findViewById(R.id.semester);
        TextView rollno = dialog.findViewById(R.id.rollno);
        TextView phoneno = dialog.findViewById(R.id.phoneno);
        TextView changePassword = dialog.findViewById(R.id.changePassword);
        TextView changeProfile = dialog.findViewById(R.id.changeProfile);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                clickAnimation(view);
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Change Profile", Toast.LENGTH_SHORT).show();
                clickAnimation(view);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Change Password", Toast.LENGTH_SHORT).show();
                clickAnimation(view);
            }
        });

    }

    public void clickAnimation(View v){
        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100);
            }
        });
    }
}