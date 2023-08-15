package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView flamme, tension, courant, adresseMacTextView, nomMachineTextView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference, databaseReference, ref;
    private Button btn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menu1;
    private LinearLayout Shape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nomMachineTextView = findViewById(R.id.nomMachineTextView1);
        adresseMacTextView = findViewById(R.id.adresseMacTextView1);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Machine");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Long numero = snapshot.child("numero").getValue(Long.class);
                String nomMachine = snapshot.child("nomMachine").getValue(String.class);
                // Vérification de la condition
                if (numero != null && numero == 1) {
                    nomMachineTextView.setText(nomMachine);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("adresse_mac").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String adresseMac = snapshot.getValue(String.class);
                adresseMacTextView.setText("Adresse Physique: " + adresseMac);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Erreur lors de la lecture de la valeur 'adresse_mac", Toast.LENGTH_SHORT).show();
            }
        });

        drawerLayout = findViewById(R.id.drawer8);
        navigationView = findViewById(R.id.navigationViewProfil1);
        menu1 = findViewById(R.id.menuP);
        Shape = findViewById(R.id.shape);
        navigationdrawer();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.listDevice:
                        startActivity(new Intent(MainActivity.this, principal.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addReclamation:
                        startActivity(new Intent(MainActivity.this, reclamation.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addProfil:
                        startActivity(new Intent(MainActivity.this, Profil.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addCapteurs:
                        startActivity(new Intent(MainActivity.this, dashborad.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addEn:
                        startActivity(new Intent(MainActivity.this, energieV.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addCapteur1:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addDéconnexion:
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "false");
                        editor.apply();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                        break;
                }
                return true;
            }
        });


        tension = findViewById(R.id.tension);
        flamme = findViewById(R.id.flamme);
        courant = findViewById(R.id.courant1);

        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, dashborad.class));
            }
        });


        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DataSnapshot> voltageSnapshots = new ArrayList<>();
                for (DataSnapshot child : snapshot.child("voltage").getChildren()) {
                    voltageSnapshots.add(child);
                }
                if (voltageSnapshots.size() == 0) {
                    tension.setText("Aucune valeur de tension trouvée");
                    tension.setTextColor(Color.RED);
                } else {
                    DataSnapshot lastVoltageSnapshot = voltageSnapshots.get(voltageSnapshots.size() - 1);
                    double lastVoltageValue = lastVoltageSnapshot.getValue(Double.class);
                    String voltageValue = "La dernière valeur de tension est : " + String.format("%.2f", lastVoltageValue) + " V";


                    ImageView lamp = findViewById(R.id.lamp);

                    if (lastVoltageValue == 0) {
                        lamp.setImageResource(R.drawable.lampe1);
                        tension.setText(voltageValue);
                        tension.setTextColor(Color.RED);
                    } else {
                        lamp.setImageResource(R.drawable.lamp_verte);
                        tension.setText(voltageValue);
                        tension.setTextColor(Color.BLACK);
                    }
                }


                Boolean valeur_flamme = snapshot.child("flamme").getValue(Boolean.class);
                ImageView flm = findViewById(R.id.ele);

                LinearLayout shape1 = findViewById(R.id.shape);
                if (valeur_flamme != null && valeur_flamme) {
                    flamme.setText("Danger : La flamme est détectée !");
                    flamme.setTextColor(Color.RED);
                    flm.setImageResource(R.drawable.flamme_red);
                    Shape.setBackgroundResource(R.drawable.shape1);

                } else {
                    flamme.setText("La flamme n'est pas détectée.");
                    flamme.setTextColor(Color.BLACK);
                    flm.setImageResource(R.drawable.flamme);
                    Shape.setBackgroundResource(R.drawable.shape);
                }

                List<DataSnapshot> courantSnapshot = new ArrayList<>();
                for (DataSnapshot child : snapshot.child("courant").getChildren()) {
                    courantSnapshot.add(child);
                }
                DataSnapshot Courantsnpshot2 = courantSnapshot.get(courantSnapshot.size() - 1);
                double CourantSN = Courantsnpshot2.getValue(Double.class);
                String valeur_courant = "la valeur de courant est : " + String.format("%.3f", CourantSN) + " A";
                ImageView eleCourant = findViewById(R.id.eleCourant);
                if (CourantSN == 0) {
                    courant.setText(valeur_courant);
                    courant.setTextColor(Color.RED);
                    eleCourant.setImageResource(R.drawable.courant3);
                    sendNotification1("Les coupures de courant sur les machines doivent être résolues rapidement");
                } else {
                    courant.setText(valeur_courant);
                    courant.setTextColor(Color.BLACK);
                    eleCourant.setImageResource(R.drawable.courant4);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "error !", Toast.LENGTH_SHORT).show();

            }
        });
        reference.child("flamme").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean capteurFlamme = snapshot.getValue(Boolean.class);
                if (capteurFlamme) {
                    reference.child("adresse_mac").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String ipAddress = snapshot.getValue(String.class);
                            reference.child("dateHeure").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String date = snapshot.getValue(String.class);
                                    sendNotification("Alerte de détection de flamme", "Une flamme a été détectée!\nAdresse Physique de la machine: " + ipAddress + "\nDate et heure: " + date);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.w("MainActivity", "onCancelled", error.toException());
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w("MainActivity", "onCancelled", error.toException());
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MainActivity", "onCancelled", error.toException());
            }
        });
//        reference.child("adresse_ip").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String ipAddress = snapshot.getValue(String.class);
//                if (ipAddress != null && !ipAddress.isEmpty()) {
//                    // Récupérer l'état de détection de flamme de Firebase
//                    DatabaseReference flameDetectedRef = reference.child("flamme");
//                    flameDetectedRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            boolean capteurFlamme = snapshot.getValue(Boolean.class);
//                            if (capteurFlamme) {
//                                DatabaseReference dateRef = reference.child("date");
//                                dateRef.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        String date = snapshot.getValue(String.class);
//                                        if (date != null && !date.isEmpty()) {
//                                            DatabaseReference heureRef = reference.child("heure");
//                                            heureRef.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                    String heure = snapshot.getValue(String.class);
//                                                    if (heure != null && !heure.isEmpty()) {
//                                                        String message = "Une flamme a été détectée!\nAdresse IP de la machine: " + ipAddress + "\nDate: " + date + " \nHeure: " + heure;
//                                                        sendNotification("Alerte de détection de flamme", message);
//                                                    }
//
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) {
//                                                    Log.w("MainActivity", "onCancelled", error.toException());
//                                                }
//                                            });
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//                                        Log.w("MainActivity", "onCancelled", error.toException());
//                                    }
//                                });
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Log.w("MainActivity", "onCancelled", error.toException());
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("MainActivity", "onCancelled", error.toException());
//            }
//        });

    }

    private void navigationdrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.addCapteur1);
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

    //courant
    private void sendNotification1(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the notification channel
            String channelId = "my_channel_id";
            String channelName = "My Channel Name";
            String channelDescription = "My Channel Description";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id").setSmallIcon(R.drawable.notification).setContentTitle("Notification").setStyle(new NotificationCompat.BigTextStyle().bigText(message)).setPriority(NotificationCompat.PRIORITY_HIGH);
        //show notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(2, builder.build());
    }


    //flamme
    private void sendNotification(String title, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the notification channel
            String channelId = "my_channel_id";
            String channelName = "My Channel Name";
            String channelDescription = "My Channel Description";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id").setSmallIcon(R.drawable.notification).setContentTitle(title).setStyle(new NotificationCompat.BigTextStyle().bigText(message)).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(1, builder.build());


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference flameDetectedRef = database.getReference("flamme");
        flameDetectedRef.setValue(true);

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