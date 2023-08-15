package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private Button but, rest;
    private EditText mailforget;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        but = findViewById(R.id.button);
        rest = findViewById(R.id.mot);
        mailforget = findViewById(R.id.mailforget);

        firebaseAuth = FirebaseAuth.getInstance();

        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailUser = mailforget.getText().toString().trim();
                if (emailUser.isEmpty() || !emailUser.contains("@") || !emailUser.contains(".")) {
                    Toast.makeText(ForgotPassword.this, "Veuillez saisir votre adresse é-mail !", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(emailUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this, "Un e-mail de réinitialisation du mot de passe a été envoyé", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this, Login.class));
                            } else {
                                Toast.makeText(ForgotPassword.this, "erreur !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPassword.this, Login.class));
            }
        });
    }
}