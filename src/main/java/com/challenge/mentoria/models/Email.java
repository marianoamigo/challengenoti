package com.challenge.mentoria.models;

import jdk.swing.interop.SwingInterOpUtils;

public class Email extends Chanel {

    public Email(){}

    @Override
    public void send() {
        System.out.println("Enviado por EMAIL");
    }
}
