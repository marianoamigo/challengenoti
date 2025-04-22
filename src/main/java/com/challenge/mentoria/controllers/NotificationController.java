package com.challenge.mentoria.controllers;

import com.challenge.mentoria.errors.ErrorService;
import com.challenge.mentoria.models.Notification;
import com.challenge.mentoria.models.Usuario;
import com.challenge.mentoria.services.NotificationService;
import com.challenge.mentoria.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/user/{id]")
    public String notificationsByUser(@PathVariable int id, ModelMap model) { //el id lo sacará de la ruta con {id}

        try {
            Usuario usuario = userService.findById1(id);

            if (usuario == null) {
                throw new ErrorService("No se encontró el usuario con id "+ id);
            }
            List<Notification> notifications = notificationService.consult(id);
            if(notifications.isEmpty()) {
                throw new ErrorService("El usuario no tiene notificaciones");
            }
//            HttpHeaders headers = new HttpHeaders(); //para indicar el tipo de contenido que vamos a recibir.
//            headers.setContentType(MediaType.APPLICATION_JSON); //acá va lo que va a recibir, las notificaciones están serializadas en JSON
//            return new ResponseEntity<>(notifications, headers, HttpStatus.OK);
            model.addAttribute("notifications",notifications);
            model.addAttribute("userMail",usuario.getMail());
            return "notifications";
        } catch (ErrorService es) {
            model.put("error", es.getMessage());
            return "error";
        }

    }

}
