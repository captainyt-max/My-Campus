package com.example.my_campus;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
}