package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterAct extends AppCompatActivity {
    private TextView Newcompte;
    private EditText NomPrenom, Nom, Email, Cin, tfn, Mdp;
    private Button BtnR;
    private String NomPrenoms, Emails, Cins, tfns, Mdps, Noms;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Newcompte = findViewById(R.id.newh);

        NomPrenom = findViewById(R.id.nomprenom);
        Nom = findViewById(R.id.nom);
        Email = findViewById(R.id.email);
        Cin = findViewById(R.id.cin);
        tfn = findViewById(R.id.numero);
        Mdp = findViewById(R.id.mdp);
        BtnR = findViewById(R.id.btnRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);

        Newcompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterAct.this, Login.class));
            }
        });

        BtnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("S'il vous plaît, attendez...!");
                progressDialog.show();
                if (validate()) {
                    String User_email = Email.getText().toString().trim();
                    String User_mdp = Mdp.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(User_email, User_mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendEmailVerification();
                            } else {
                                Toast.makeText(RegisterAct.this, "l'enregistrement a échoué !", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    });


                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        sendUserDate();
                        Toast.makeText(RegisterAct.this, "inscription faite ! Veuillez vérifier vos e-mail !", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterAct.this, Login.class));
                        progressDialog.dismiss();
                    } else {

                    }
                }
            });
        }
    }

    private void sendUserDate() {
        DatabaseReference myRef = firebaseDatabase.getReference("Users");
        User1 user = new User1(NomPrenoms, Noms, Emails, Cins, tfns);
        myRef.child("" + firebaseAuth.getUid()).setValue(user);
    }

    private boolean validate() {
        boolean res = false;
        NomPrenoms = NomPrenom.getText().toString();
        Noms = Nom.getText().toString();
        Emails = Email.getText().toString();
        Cins = Cin.getText().toString();
        tfns = tfn.getText().toString();
        Mdps = Mdp.getText().toString();

        if (NomPrenoms.isEmpty() || NomPrenoms.length() < 3) {
            NomPrenom.setError("Prénom est invalide !");
        } else if (Noms.isEmpty() || Noms.length() < 3) {
            NomPrenom.setError("Nom est invalide !");
        } else if (Emails.isEmpty() || !Emails.contains("@") || !Emails.contains(".")) {
            Email.setError("Email est invalide !");

        } else if (Cins.isEmpty() || Cins.length() != 8) {
            Cin.setError("CIN est invalide !");
        } else if (tfns.isEmpty() || tfns.length() != 8) {
            tfn.setError("numéro de téléphone est invalide !");
        } else if (Mdps.isEmpty() || Mdps.length() < 7) {
            Mdp.setError("Mot de passe est invalide");
        } else {
            res = true;
        }

        return res;
    }
}