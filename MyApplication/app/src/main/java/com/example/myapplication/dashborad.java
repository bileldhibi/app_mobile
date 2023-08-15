package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class dashborad extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView flm, tension, courant;
    SwitchCompat switchCompat;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menu1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashborad);

        drawerLayout = findViewById(R.id.drawerProfil);
        navigationView = findViewById(R.id.navigationViewProfil);
        menu1 = findViewById(R.id.menuProfil);
        navigationdrawer();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.listDevice:
                        startActivity(new Intent(dashborad.this, principal.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addReclamation:
                        startActivity(new Intent(dashborad.this, reclamation.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addCapteurs:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addProfil:
                        startActivity(new Intent(dashborad.this, Profil.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addCapteur1:
                        startActivity(new Intent(dashborad.this, MainActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addEn:
                        startActivity(new Intent(dashborad.this, energieV.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addDéconnexion:
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "false");
                        editor.apply();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(dashborad.this, Login.class));
                        finish();
                        break;
                }
                return true;
            }
        });


        flm = findViewById(R.id.clothingName);
        tension = findViewById(R.id.elecName);
        courant = findViewById(R.id.elNameCourant);

        switchCompat = findViewById(R.id.switchMode);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightMode", false);

        if (nightMode) {
            switchCompat.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchCompat.setTextColor(getResources().getColor(android.R.color.white));
        }

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", false);
                    switchCompat.setTextColor(getResources().getColor(android.R.color.black));
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", true);
                    switchCompat.setTextColor(getResources().getColor(android.R.color.white));
                }
                editor.apply();
            }
        });


        flm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashborad.this, controle.class));
            }
        });
        tension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashborad.this, energieV.class));
            }
        });
        courant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashborad.this, courant.class));
            }
        });
    }

    private void navigationdrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.addCapteurs);
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        drawerLayout.setScrimColor(getResources().getColor(R.color.app));
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

}