package com.challenge.mentoria.models;

public class Sms extends Chanel {

    public Sms(){}
    @Override
    public void send() {
        System.out.println("Enviado por SMS");
    }
}
