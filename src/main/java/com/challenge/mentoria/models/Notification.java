package com.challenge.mentoria.models;

import com.challenge.mentoria.enums.Chanel;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private Chanel channel;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario usuario;
    private int numSend;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSend; //despues se setea con new Date
    private boolean stateSend;

    public int getNumSend() {
        return numSend;
    }

    public void setNumSend(int numSend) {
        this.numSend = numSend;
    }

    public Date getDateSend() {
        return dateSend;
    }

    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }

    public boolean getStateSend() {
        return stateSend;
    }

    public void setStateSend(boolean stateSend) {
        this.stateSend = stateSend;
    }


    public Notification(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public com.challenge.mentoria.enums.Chanel getChannel() {
        return channel;
    }

    public void setChannel(Chanel channel) {
        this.channel = channel;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
