package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private TextView mdp, register;
    private EditText emailLogin, mdpLogin;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mdp = findViewById(R.id.mdp);
        register = findViewById(R.id.register);
        emailLogin = findViewById(R.id.email_input);
        mdpLogin = findViewById(R.id.password_input);
        btnLogin = findViewById(R.id.buttonSo);
        remember = findViewById(R.id.check);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkBox = preferences.getString("remember", "");

        if (checkBox.equals("true")) {
            startActivity(new Intent(Login.this, principal.class));
        } else if (checkBox.equals("false")) {
            Toast.makeText(this, "Veuillez vous connecter !", Toast.LENGTH_SHORT).show();

        }
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                } else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();

                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailLogin.getText().toString().isEmpty() || !emailLogin.getText().toString().contains("@")) {
                    emailLogin.setError("Email est invalide !");
                } else if (mdpLogin.getText().toString().isEmpty() || mdpLogin.getText().toString().length() < 8) {
                    mdpLogin.setError("mod passe est invalide !");
                } else {
                    valide(emailLogin.getText().toString(), mdpLogin.getText().toString());
                }
            }
        });

        mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, RegisterAct.class));
            }
        });

    }
    private void valide(String emailo, String mdpo) {
        progressDialog.setMessage("S'il vous plaît, attendez...!");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(emailo, mdpo).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    checkEmailverification();
                } else {
                    Toast.makeText(Login.this, "Veuillez vérifier que vos données sont correctes !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void checkEmailverification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        boolean mailFlag = user.isEmailVerified();

        if (mailFlag) {
            finish();
            startActivity(new Intent(Login.this, principal.class));
        } else {
            Toast.makeText(this, "Veuillez vérifier vos e-mail !", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}