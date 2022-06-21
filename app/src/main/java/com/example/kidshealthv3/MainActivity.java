package com.example.kidshealthv3;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    Profile profileFragment = new Profile();
    HomePage homePageFragment = new HomePage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.Framelayout, homePageFragment).commit();

        bottomNav = findViewById(R.id.BottmBar);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.HomeBtn:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.fade_out)
                                .replace(R.id.Framelayout, homePageFragment).addToBackStack(null).commit();
                        break;
                    case R.id.ProfileBtn:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.fade_out)
                                .replace(R.id.Framelayout, profileFragment).addToBackStack(null).commit();
                        break;
                    case R.id.LogOutBtn:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        break;
                }

                return false;
            }
        });
    }
}