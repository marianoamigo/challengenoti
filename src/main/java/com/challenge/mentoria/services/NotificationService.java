package com.challenge.mentoria.services;

import com.challenge.mentoria.enums.Chanel;
import com.challenge.mentoria.errors.ErrorService;
import com.challenge.mentoria.models.*;
import com.challenge.mentoria.repositories.INotificationRepository;
import com.challenge.mentoria.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired /*esta anotacion indica que se auto-inicialice la instancia de IUserRepository*/
    private IUserRepository userRepository;
    @Autowired
    private INotificationRepository notificationRepository;

    @Transactional
    public void create(Integer idUser, String title, String content, Chanel channel) throws ErrorService {
        Notification notification = new Notification();
        Usuario usuario = userRepository.findById(idUser).get();
        validate(title, content, channel);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setNumSend(0);
        notification.setChannel(channel); //por ahora no simulamos el envío
        //send( idUser,  title,  content,  channel, notification);
        notification.setUsuario(usuario);
        notificationRepository.save(notification);
    }

    @Transactional
    public void modify(Integer idUser, Integer idNotif, String title, String content, Chanel channel) throws ErrorService {
        validate(title,content,channel);
        Optional<Notification> respuesta = notificationRepository.findById(idNotif);
        if (respuesta.isPresent()) {
            Notification notification = respuesta.get();
            if (notification.getUsuario().getId().equals(idUser)){
                notification.setTitle(title);
                notification.setContent(content);
                notification.setChannel(channel);
                notificationRepository.save(notification);
            } else {
                throw new ErrorService("No tiene permisos para modificar la notificacion");
            }
        } else {
            throw new ErrorService("No se encontró la notificación");
        }
    }

    @Transactional
    public void delete(Integer idUser, Integer idnotif) throws ErrorService {
        Optional<Notification> respuesta = notificationRepository.findById(idnotif);
        if (respuesta.isPresent()) {
            Notification notification = respuesta.get();
            if (notification.getUsuario().getId() == idUser){
                notificationRepository.delete(notification);
            } else {
                throw new ErrorService("No tiene permisos para eliminar la notificacion");
            }
        } else {
            throw new ErrorService("No se encontró la notificacion");
        }
    }

    public List<Notification> consult(Integer idUser) {
        return notificationRepository.findByUsuario_Id(idUser);
    }

    public void validate(String title, String content, Chanel channel) throws ErrorService{
        if (title == null || title.isEmpty()) {
            throw new ErrorService("El título no puede ser nulo");
        }
        if (content == null || content.isEmpty()) {
            throw new ErrorService("El cuerpo de la notificacion no puede ser nulo");
        }
        if (channel == null) {
            throw new ErrorService("El canal no puede ser nulo");
        }

    }

    private void send(Integer idUser, String title, String content, Chanel channel, Notification notification) throws ErrorService{
        switch (channel) {
            case Chanel.EMAIL:
                //validar formato
                notification.setNumSend(notification.getNumSend()+1);
                notification.setDateSend(new Date());
                break;

            case Chanel.SMS:
                if (content.length() > 160) {
                    throw new ErrorService("El cuerpo de la notificacion no puede ser  mayor a 160 caracteres");
                }
                notification.setNumSend(notification.getNumSend()+1);
                notification.setDateSend(new Date());
                break;

            case Chanel.PUSH:
                //validar token
                //formatear payload
                notification.setStateSend(true);
                break;
        }
    }

    public Notification findNotificationById(Integer id) throws ErrorService {
        return notificationRepository.findById(id).orElseThrow(() ->
                new ErrorService("No se encontró la notificacion con ID: " + id)
        );
    }


}

