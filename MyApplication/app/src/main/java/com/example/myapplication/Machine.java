package com.example.myapplication;

public class Machine {
    String nomMachine, description, status;
    int numero;

    public Machine(int numero, String nomMachine, String description, String status) {
        this.numero = numero;
        this.nomMachine = nomMachine;
        this.description = description;
        this.status = status;
    }

    public Machine() {
    }
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    public String getNomMachine() {
        return nomMachine;
    }

    public void setNomMachine(String nomMachine) {
        this.nomMachine = nomMachine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
