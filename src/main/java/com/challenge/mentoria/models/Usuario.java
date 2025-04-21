package com.challenge.mentoria.models;


import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String mail;
    private String pass;

    public Usuario(){}

    public Usuario(String mail, String pass) {
        this.mail = mail;
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
