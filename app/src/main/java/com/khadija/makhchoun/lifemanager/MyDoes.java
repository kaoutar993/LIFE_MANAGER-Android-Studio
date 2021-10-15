package com.khadija.makhchoun.lifemanager;

/**
 * created by pc on 03/03/2021.
 **/
public class MyDoes {
    String titledoes;
    String datedoes;
    String descdoes;
    String Keydoes;

    public MyDoes() {
    }

    public MyDoes(String titledoes, String datedoes, String descdoes, String keydoes) {
        this.titledoes = titledoes;
        this.datedoes = datedoes;
        this.descdoes = descdoes;
        Keydoes = keydoes;
    }

    public void setKeydoes(String keydoes) {
        Keydoes = keydoes;
    }

    public String getKeydoes() {
        return Keydoes;
    }

    public void setTitledoes(String titledoes) {
        this.titledoes = titledoes;
    }

    public void setDatedoes(String datedoes) {
        this.datedoes = datedoes;
    }

    public void setDescdoes(String descdoes) {
        this.descdoes = descdoes;
    }

    public String getTitledoes() {
        return titledoes;
    }

    public String getDatedoes() {
        return datedoes;
    }

    public String getDescdoes() {
        return descdoes;
    }
}

