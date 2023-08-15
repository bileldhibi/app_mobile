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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class reclamation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText nomMachine;
    private RadioGroup radioGroup;
    private Button Reclamation;
    private DatabaseReference claimRef;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menu1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamation);
        drawerLayout = findViewById(R.id.drawerReclamation);
        navigationView = findViewById(R.id.navigationViewReclamation);
        menu1 = findViewById(R.id.menuReclamation);

        navigationdrawer();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.listDevice:
                        startActivity(new Intent(reclamation.this, principal.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addReclamation:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addCapteurs:
                        startActivity(new Intent(reclamation.this, dashborad.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addProfil:
                        startActivity(new Intent(reclamation.this, Profil.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addCapteur1:
                        startActivity(new Intent(reclamation.this, MainActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addEn:
                        startActivity(new Intent(reclamation.this, energieV.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addDéconnexion:
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "false");
                        editor.apply();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(reclamation.this, Login.class));
                        finish();
                        break;
                }
                return true;
            }
        });


        nomMachine = findViewById(R.id.editTextMachineName);
        radioGroup = findViewById(R.id.radioGroupStatus);
        Reclamation = findViewById(R.id.buttonSend);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        claimRef = firebaseDatabase.getReference("Reclamation");

        Reclamation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitClaim();
            }
        });
    }

    private void submitClaim() {
        String machineName = nomMachine.getText().toString();
        int selectedStatusId = radioGroup.getCheckedRadioButtonId();

        if (selectedStatusId == -1 || machineName.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }
        String status = ((RadioButton) findViewById(selectedStatusId)).getText().toString();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Claim claim = new Claim(machineName, status, timestamp);
        claimRef.push().setValue(claim).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(reclamation.this, "Réclamation soumise avec succès", Toast.LENGTH_SHORT).show();
                // Réinitialiser les champs après la soumission réussie
                nomMachine.setText("");
                radioGroup.clearCheck();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(reclamation.this, "Erreur lors de la soumission de la réclamation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigationdrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.addReclamation);
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