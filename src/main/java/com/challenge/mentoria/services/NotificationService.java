package com.challenge.mentoria.services;

import com.challenge.mentoria.errors.ErrorService;
import com.challenge.mentoria.models.*;
import com.challenge.mentoria.repositories.INotificationRepository;
import com.challenge.mentoria.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired /*esta anotacion indica que se auto-inicialice la instancia de IUserRepository*/
    private IUserRepository userRepository;
    @Autowired
    private INotificationRepository notificationRepository;

    @Transactional
    public void createNotification(Integer idUser, String title, String content, String channel) throws ErrorService {

        Usuario usuario = userRepository.findById(idUser).get();
        validate(title, content, channel);
        Notification notification = new Notification();
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Ingrese titulo");
//        String title = scanner.nextLine();
        notification.setTitle(title);
//        System.out.println("Ingrese contenido");
//        String content = scanner.nextLine();
        notification.setContent(content);
//        System.out.println("Ingrese canal");
//        String canal = scanner.nextLine();
        switch (channel.toUpperCase()) {
            case ("EMAIL"):
                Email email = new Email();
                email.send();
                notification.setChannel(channel);
                break;
            case ("SMS"):
                Sms sms = new Sms();
                sms.send();
                notification.setChannel(channel);
                break;
            case ("PUSH"):
                Push push = new Push();
                push.send();
                notification.setChannel(channel);
                break;
            default:
                throw new ErrorService("Mal elegido el canal");
        }
        notificationRepository.save(notification);
    }

    @Transactional
    public void modify(Integer iduser, Integer idnotif, String title, String content, String channel) throws ErrorService {
        validate(title,content,channel);

//        Scanner scanner = new Scanner(System.in);
        Optional<Notification> respuesta = notificationRepository.findById(idnotif);
        if (respuesta.isPresent()) {
            Notification notification = respuesta.get();
            if (notification.getUsuario().getId() == iduser){
                notification.setTitle(title);
                notification.setContent(content);
                notificationRepository.save(notification);
            } else {
                throw new ErrorService("No tiene permisos para modificar la notificacion");
            }
        } else {
            throw new ErrorService ("No se encontró la notificacion");
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

    public void consult(Integer iduser, Integer idnotif) {
        List<Notification> notifications = notificationRepository.findByUsuario_Id(iduser);

        if (notifications.isEmpty()) {
            System.out.println("El usuario no tiene notificaciones");
        } else {
            for (Notification n : notifications) {
                System.out.println("Titulo: " + n.getTitle());
                System.out.println("Contenido: " + n.getContent());
                System.out.println("Canal: " + n.getChannel());
                System.out.println("----------");

            }
        }

    }

    public void validate(String title, String content, String channel) throws ErrorService{
        if (title == null || title.isEmpty() || title.length() > 45) {
            throw new ErrorService("El título no puede ser nulo, ni mayor a 45 caracteres");
        }
        if (content == null || content.isEmpty() || content.length() > 255) {
            throw new ErrorService("El cuerpo de la notificacion no puede ser nulo ni puede ser mayor a 255 caracteres");
        }
        if (channel == null || channel.isEmpty()) {
            throw new ErrorService("Debe elegir un canal válido");
        }

    }

}

