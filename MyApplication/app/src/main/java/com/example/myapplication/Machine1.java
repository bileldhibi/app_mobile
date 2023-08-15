package com.example.myapplication;

public class Machine1 {
    int numero;
    String nomMachine;

    public Machine1(int numero, String nomMachine) {
        this.numero = numero;
        this.nomMachine = nomMachine;
    }

    public Machine1() {
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
}
