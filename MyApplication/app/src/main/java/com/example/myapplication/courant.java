package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class courant extends AppCompatActivity {
    private TextView courant1, historiqueTextView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference, ref;
    private Button button, historique, afficherHistoriqueButton;
    private LinearLayout shape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courant);
        courant1 = findViewById(R.id.courant);
        button = findViewById(R.id.button1);
        shape = findViewById(R.id.shCourant);

        ref = FirebaseDatabase.getInstance().getReference();


        historique = findViewById(R.id.historyB);
        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Historique();
            }
        });

//        historique.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                reference = firebaseDatabase.getReference().child("courant");
//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        StringBuffer sb = new StringBuffer();
//                        int i = 1;
//                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                            double CourantValue = childSnapshot.getValue(Double.class);
//                            sb.append("La valeur de courant ").append(i).append(" est : ").append(String.format("%.3f", CourantValue)).append(" A\n");
//                            i++;
//                        }
//                        String CourantValues = sb.toString().trim();
//                        ScrollView scrollView = new ScrollView(courant.this);
//                        scrollView.setBackgroundColor(Color.parseColor("#8692f7"));
//                        TextView textView = new TextView(courant.this);
//                        textView.setText(CourantValues);
//                        textView.setTextSize(18);
//                        textView.setPadding(16, 16, 16, 16);
//                        scrollView.addView(textView);
//                        // Créer une boîte de dialogue et y ajouter la vue ScrollView
//                        AlertDialog.Builder builder = new AlertDialog.Builder(courant.this);
//                        builder.setTitle("Historique des valeurs de courant");
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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(courant.this, dashborad.class));
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DataSnapshot> courantSnapshot = new ArrayList<>();
                for (DataSnapshot child : snapshot.child("courant").getChildren()) {
                    courantSnapshot.add(child);
                }
                DataSnapshot Courantsnpshot2 = courantSnapshot.get(courantSnapshot.size() - 1);
                double CourantSN = Courantsnpshot2.getValue(Double.class);
                String courant2 = " valeur de courant est : " + String.format("%.3f", CourantSN) + " A";
                // check if courant is 0
                ImageView imgCourant = findViewById(R.id.ele3);
                if (CourantSN == 0) {
                    sendNotification("Les coupures de courant sur les machines doivent être résolues rapidement");
                    courant1.setText(courant2);
                    courant1.setTextColor(Color.RED);
                    imgCourant.setImageResource(R.drawable.courant3);
                    shape.setBackgroundResource(R.drawable.shape1);
                } else {
                    courant1.setText(courant2);
                    courant1.setTextColor(Color.BLACK);
                    imgCourant.setImageResource(R.drawable.courant4);
                    shape.setBackgroundResource(R.drawable.shape);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(courant.this, "error !", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void Historique() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference Courant = databaseReference.child("courant");
        DatabaseReference date_heure = databaseReference.child("dateHeureCourant");

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

                            histo.append("Valeur courant: ").append(String.format("%.3f", courantDouble)).append(" A")
                                    .append(" Date et Heure: ").append(dateHeure1).append("\n");
                            i++;
                        }
                        // Afficher une boîte de dialogue avec l'historique
                        AlertDialog.Builder builder = new AlertDialog.Builder(courant.this);
                        builder.setTitle("Historique des valeurs de courant");
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
        Courant.addValueEventListener(courant_date1);
    }

    //


    //
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Notification")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
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
        notificationManagerCompat.notify(1, builder.build());
    }
}