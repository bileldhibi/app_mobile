package com.example.myapplication;

import androidx.annotation.NonNull;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class controle extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private Button btn;
    private TextView flm1;
    private LinearLayout shape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle);
        flm1 = findViewById(R.id.flamme1);
        btn = findViewById(R.id.button5);
        shape = findViewById(R.id.sh);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(controle.this, dashborad.class));
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean valeur_flamme = snapshot.child("flamme").getValue(Boolean.class);
                ImageView flammeImg = findViewById(R.id.eleflmame);
                if (valeur_flamme != null && valeur_flamme) {
                    flm1.setText("Danger : La flamme est détectée !");
                    flm1.setTextColor(Color.RED);
                    flammeImg.setImageResource(R.drawable.flamme_red);
                    shape.setBackgroundResource(R.drawable.shape1);

                } else {
                    flm1.setText("La flamme n'est pas détectée.");
                    flm1.setTextColor(Color.BLACK);
                    flammeImg.setImageResource(R.drawable.flamme);
                    shape.setBackgroundResource(R.drawable.shape);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(controle.this, "erreur !", Toast.LENGTH_SHORT).show();
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
                                    Log.w("controle", "onCancelled", error.toException());
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w("controle", "onCancelled", error.toException());
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("controle", "onCancelled", error.toException());
            }
        });
    }

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
}