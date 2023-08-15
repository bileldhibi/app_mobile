package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menu1;
    private ListView listMachine;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        drawerLayout = findViewById(R.id.drawerPrancipal1);
        navigationView = findViewById(R.id.navigationView1);
        menu1 = findViewById(R.id.menu1);

        listMachine = findViewById(R.id.listMachine);

        reference = FirebaseDatabase.getInstance().getReference();

        ArrayList<String> machineArraylist = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(principal.this, R.layout.list_item, machineArraylist);
        listMachine.setAdapter(adapter);
        DatabaseReference machineRef = FirebaseDatabase.getInstance().getReference().child("Machine");
        machineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                machineArraylist.clear();
                for (DataSnapshot machineSnapshot : snapshot.getChildren()) {
                    Machine mach = machineSnapshot.getValue(Machine.class);
                    String machineDetails ="<font color='#000000'>Numéro de Machine :</font> "+ mach.getNumero()+"<br><font color='#000000'>Nom de Machine :</font> " + mach.getNomMachine()
                            + "<br><font color='#000000'>Description :</font> " + mach.getDescription()
                            + "<br><font color='#000000'>Status :</font> " + mach.getStatus();
                    machineArraylist.add(Html.fromHtml(machineDetails).toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(principal.this, "Erreur !", Toast.LENGTH_SHORT).show();
            }
        });

        navigationdrawer();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.listDevice:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addReclamation:
                        startActivity(new Intent(principal.this, reclamation.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addCapteurs:
                        startActivity(new Intent(principal.this, dashborad.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addProfil:
                        startActivity(new Intent(principal.this, Profil.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addCapteur1:
                        startActivity(new Intent(principal.this, MainActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addEn:
                        startActivity(new Intent(principal.this, energieV.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addDéconnexion:
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "false");
                        editor.apply();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(principal.this, Login.class));
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void navigationdrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.listDevice);
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

    @Override
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