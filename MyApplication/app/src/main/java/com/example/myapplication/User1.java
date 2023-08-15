package com.example.myapplication;

public class User1 {
    String prenom, nom, email, cin, telephone;

    public User1() {
    }

    public User1(String prenom, String nom, String email, String cin, String telephone) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.cin = cin;
        this.telephone = telephone;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getCin() {
        return cin;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
