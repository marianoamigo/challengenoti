package com.challenge.mentoria.models;

import jdk.swing.interop.SwingInterOpUtils;

public class Push extends Chanel{

    public Push(){}
    @Override
    public void send() {
        System.out.println("Enviado por PUSH");
    }
}
