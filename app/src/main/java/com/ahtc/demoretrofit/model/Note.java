package com.ahtc.demoretrofit.model;

public class Note {

    private int id = 0;
    private String text = "";
    private int idUser = 0;

    public Note(int id, String text) {
        this.id = id;
        this.text = text;
        this.idUser = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
