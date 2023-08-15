package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class energieV extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private GraphView graphView;
    private TextView dateTimeTextView, adresseMacTextView, nomMachineTextView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference, ref, databaseReference ,ref1;
    private int i;
    private Button btn, hostory;
    // drawer
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menu1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energie_v);
        dateTimeTextView = findViewById(R.id.date);
        nomMachineTextView = findViewById(R.id.nomMachineTextView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Machine");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Long numero = snapshot.child("numero").getValue(Long.class);
                String nomMachine = snapshot.child("nomMachine").getValue(String.class);
                double Coefficient = snapshot.child("Coefficient").getValue(double.class);
                // Vérification de la condition
                if (numero != null && numero == 1){
                    nomMachineTextView.setText(nomMachine+"\n   Facteur :"+Coefficient);
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

        adresseMacTextView = findViewById(R.id.adresseMacTextView);
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("adresse_mac").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String adresseMac = snapshot.getValue(String.class);
                adresseMacTextView.setText("Adresse Physique: " + adresseMac);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(energieV.this, "Erreur lors de la lecture de la valeur 'adresse_mac", Toast.LENGTH_SHORT).show();
            }
        });


        drawerLayout = findViewById(R.id.drawerEnergie);
        navigationView = findViewById(R.id.navigationViewEnergie);
        menu1 = findViewById(R.id.menuEnergie);
        navigationdrawer();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.listDevice:
                        startActivity(new Intent(energieV.this, principal.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addReclamation:
                        startActivity(new Intent(energieV.this, reclamation.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addCapteurs:
                        startActivity(new Intent(energieV.this, dashborad.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addProfil:
                        startActivity(new Intent(energieV.this, Profil.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addCapteur1:
                        startActivity(new Intent(energieV.this, MainActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addEn:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.addDéconnexion:
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "false");
                        editor.apply();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(energieV.this, Login.class));
                        finish();
                        break;
                }
                return true;
            }
        });
        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(energieV.this, dashborad.class));
            }
        });

        graphView = findViewById(R.id.graphView);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(170);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(20);

        // Initialisation des constantes pour le calcul de la consommation d'énergie
        ref1 = FirebaseDatabase.getInstance().getReference().child("Machine");
        ref1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Long numero = snapshot.child("numero").getValue(Long.class);
                double Coefficient = snapshot.child("Coefficient").getValue(double.class);
                // Vérification de la condition
                if (numero != null && numero == 1){
                    final double VOLTAGE_TO_POWER_FACTOR = Coefficient; // Facteur de conversion de tension en puissance consommée (à ajuster en fonction des caractéristiques de votre appareil)
                    final int TIME_INTERVAL = 1; // Intervalle de temps entre chaque mesure de tension en secondes

                 // Récupération des références de la base de données Firebase et du GraphView
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    reference = firebaseDatabase.getReference();

                   // Configuration du GraphView
                    graphView.setTitle("Surveillance de la consommation d'énergie");
                    graphView.getGridLabelRenderer().setVerticalAxisTitle("Tension (V)");
                    graphView.getGridLabelRenderer().setHorizontalAxisTitle("Temps (s)");
                    graphView.getGridLabelRenderer().setGridColor(Color.BLACK);
                    graphView.setBackgroundColor(Color.parseColor("#8692f7"));

                  // Création de la série de données pour le graphique
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                    series.setColor(Color.BLUE);
                    graphView.addSeries(series);

                    // Calcul de la consommation d'énergie pour chaque mesure de tension
                    reference.child("voltage").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<DataPoint> dataPoints = new ArrayList<>();
                            double previousValue = 0;
                            double totalEnergyConsumed = 0;
                            int i = 0;

                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                double voltageValue = childSnapshot.getValue(Double.class);
                                dataPoints.add(new DataPoint(i, voltageValue));
                                i++;

                                // Calcul de la consommation d'énergie pour cette mesure de tension
                                double powerConsumed = (voltageValue * voltageValue) * VOLTAGE_TO_POWER_FACTOR * TIME_INTERVAL;
                                totalEnergyConsumed += powerConsumed;

                                // Si la valeur de tension courante est différente de la précédente, la courbe est dessinée en rouge, sinon elle est dessinée en vert
                                if (voltageValue > previousValue) {
                                    series.setColor(Color.GREEN);
                                } else {
                                    series.setColor(Color.RED);
                                }

                                previousValue = voltageValue;
                            }

                            // Conversion des données en tableau de points de données pour l'affichage dans GraphView
                            DataPoint[] dataPointArray = new DataPoint[dataPoints.size()];
                            dataPointArray = dataPoints.toArray(dataPointArray);

                            // Mise à jour de la série de données pour le graphique
                            series.resetData(dataPointArray);

                            // Mise à jour de la date et de l'heure dans le TextView
                            String currentDateAndTime1 = DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString();
                            dateTimeTextView.setText("Date et Heure: " + currentDateAndTime1);

                            graphView.setTitleTextSize(33);
                            graphView.setTitle(String.valueOf(Html.fromHtml("<b>Surveillance de la consommation d'énergie-Total consommé : " + String.format("%.2f", totalEnergyConsumed) + " J</b>")));

                            // Condition pour envoyer une notification si la consommation d'énergie dépasse le seuil
                            if (totalEnergyConsumed >= 750) {
                                String currentDateAndTime = DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString();
                                String notificationMessage = "La consommation d'énergie dépasse la limite autorisée.\nDate et heure :" + currentDateAndTime;
                                // Envoyer la notification
                                sendNotification(notificationMessage);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("GraphView", "Error: " + error.getMessage());
                        }
                    });
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
        //final double VOLTAGE_TO_POWER_FACTOR = 0.25; // Facteur de conversion de tension en puissance consommée (à ajuster en fonction des caractéristiques de votre appareil)

        hostory = findViewById(R.id.historyButton);
        hostory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historique();
            }
        });
//        hostory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                reference = firebaseDatabase.getReference().child("voltage");
//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        StringBuffer sb = new StringBuffer();
//                        int i = 1;
//                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                            double voltageValue = childSnapshot.getValue(Double.class);
//                            //Le code ajoute la valeur de tension pour chaque itération dans une boucle à une chaîne de caractères avec le numéro d'itération et la formate avec deux décimales, puis ajoute un retour à la ligne à la fin.
//                            sb.append("La valeur de tension ").append(i).append(" est : ").append(String.format("%.2f", voltageValue)).append(" V\n");
//                            i++;
//                        }
//                        String voltageValues = sb.toString().trim();
//
//                        // Créer un ScrollView et une TextView pour afficher les valeurs de tension
//                        ScrollView scrollView = new ScrollView(energieV.this);
//                        scrollView.setBackgroundColor(Color.parseColor("#8692f7"));
//                        TextView textView = new TextView(energieV.this);
//                        textView.setText(voltageValues);
//                        textView.setTextSize(18);
//                        textView.setPadding(16, 16, 16, 16);
//                        scrollView.addView(textView);
//
//                        // Créer une boîte de dialogue et y ajouter la vue ScrollView
//                        AlertDialog.Builder builder = new AlertDialog.Builder(energieV.this);
//                        builder.setTitle("Historique des valeurs de tension");
//                        builder.setView(scrollView);
//                        builder.setPositiveButton("OK", null);
//                        builder.show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.e("TextView", "Error: " + error.getMessage());
//                    }
//                });
//            }
//        });


    }

    private void historique() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference voltage = databaseReference.child("voltage");
        DatabaseReference date_heure = databaseReference.child("dateHeureVoltage");

        ValueEventListener courant_date1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder histo = new StringBuilder();
                // Récupérer les valeurs courantes dans une liste
                List<Double> valeurCourant1 = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Double valeur1 = dataSnapshot.getValue(Double.class);
                    valeurCourant1.add(valeur1);
                }
                date_heure.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int i = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String dateHeure1 = dataSnapshot.getValue(String.class);
                            Double courantDouble = valeurCourant1.get(i);

                            histo.append("valeur de tension: ").append(String.format("%.3f", courantDouble)).append(" V")
                                    .append(" Date et Heure: ").append(dateHeure1).append("\n");
                            i++;
                        }
                        // Afficher une boîte de dialogue avec l'historique
                        AlertDialog.Builder builder = new AlertDialog.Builder(energieV.this);
                        builder.setTitle("Historique des valeurs de tension");
                        builder.setMessage(histo.toString());
                        builder.setPositiveButton("OK", null);
                        builder.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        voltage.addValueEventListener(courant_date1);
    }

    private void sendNotification(String message) {
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
        // Créer une notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Notification de consommation d'énergie")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Afficher la notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
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
        notificationManager.notify(1, builder.build());
    }

    private void navigationdrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.addEn);
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