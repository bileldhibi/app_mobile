package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText nomP, cinE, Phone, nomK;
    private Button edit, deconnecte;
    private FirebaseDatabase firebaseatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser User;
    private DatabaseReference reference;

    // drawer
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menu1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        nomP = findViewById(R.id.nomE);
        nomK = findViewById(R.id.nomK);
        cinE = findViewById(R.id.cinE);
        Phone = findViewById(R.id.numerE);
        edit = findViewById(R.id.modifie);
        deconnecte = findViewById(R.id.déconnecter);

        drawerLayout = findViewById(R.id.drawerProfil);
        navigationView = findViewById(R.id.navigationViewProfil);
        menu1 = findViewById(R.id.menuProfil);

        navigationdrawer();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.listDevice:
                        startActivity(new Intent(Profil.this, principal.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addReclamation:
                        startActivity(new Intent(Profil.this, reclamation.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addProfil:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addCapteurs:
                        startActivity(new Intent(Profil.this, dashborad.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addEn:
                        startActivity(new Intent(Profil.this, energieV.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addCapteur1:
                        startActivity(new Intent(Profil.this, MainActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addDéconnexion:
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "false");
                        editor.apply();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Profil.this, Login.class));
                        finish();
                        break;
                }
                return true;
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseatabase = FirebaseDatabase.getInstance();
        User = firebaseAuth.getCurrentUser();
        reference = firebaseatabase.getReference().child("Users").child(User.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String NomP = snapshot.child("prenom").getValue().toString();
                String NomK = snapshot.child("nom").getValue().toString();
                String cin = snapshot.child("cin").getValue().toString();
                String num = snapshot.child("telephone").getValue().toString();
                nomP.setText(NomP);
                nomK.setText(NomK);
                cinE.setText(cin);
                Phone.setText(num);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profil.this, "Erreur !", Toast.LENGTH_SHORT).show();
            }
        });
        deconnecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                firebaseAuth.signOut();
                startActivity(new Intent(Profil.this, Login.class));
                Toast.makeText(Profil.this, "déconnecté avec succès !", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomP.setFocusableInTouchMode(true);
                nomK.setFocusableInTouchMode(true);
                cinE.setFocusableInTouchMode(true);
                Phone.setFocusableInTouchMode(true);
                edit.setText("sauvegarder");


                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nomC = nomP.getText().toString();
                        String nomC1 = nomK.getText().toString();
                        String cinC = cinE.getText().toString();
                        String phoneC = Phone.getText().toString();
                        reference.child("prenom").setValue(nomC);
                        reference.child("nom").setValue(nomC1);
                        reference.child("cin").setValue(cinC);
                        reference.child("telephone").setValue(phoneC);
                        Toast.makeText(Profil.this, "vos données ont été modifiées avec succès", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Profil.this, principal.class));
                    }
                });

            }
        });

    }

    private void navigationdrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.addProfil);
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