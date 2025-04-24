package com.challenge.mentoria.controllers;

import com.challenge.mentoria.enums.Chanel;
import com.challenge.mentoria.errors.ErrorService;
import com.challenge.mentoria.models.Notification;
import com.challenge.mentoria.models.Usuario;
import com.challenge.mentoria.services.NotificationService;
import com.challenge.mentoria.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@PreAuthorize("hasRole('USER_REGISTERED')")
@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/edit-profile")
    public String editProfile (HttpSession session, @RequestParam(required = false) Integer id, ModelMap model, @RequestParam(required = false) String action) throws ErrorService {
        if (action == null) {
            action = "Create";
        }

        Usuario login = (Usuario) session.getAttribute("usersession");
        if (login == null) {
            return "redirect:/login";
        }
        Notification notification = new Notification();
        if (id != null && !id.describeConstable().isEmpty()) {
            notification = notificationService.findNotificationById(id);
        } else {
            notification.setChannel(Chanel.EMAIL);
        }
        model.put("profile", notification);
        model.put("action", action);
        model.put("channels", Chanel.values());
        model.put("selectedChannel", notification.getChannel()); // ya no puede ser null por la validación
        return "notification";
    }

//    @GetMapping("/user/{id}")
//    public String notificationsByUser(@PathVariable int id, ModelMap model) { //el id lo sacará de la ruta con {id}
//
//        try {
//            Usuario usuario = userService.findUserById(id);
//
//            if (usuario == null) {
//                throw new ErrorService("No se encontró el usuario con id "+ id);
//            }
//            List<Notification> notifications = notificationService.consult(id);
//            if(notifications.isEmpty()) {
//                throw new ErrorService("El usuario no tiene notificaciones");
//            }
////            HttpHeaders headers = new HttpHeaders(); //para indicar el tipo de contenido que vamos a recibir.
////            headers.setContentType(MediaType.APPLICATION_JSON); //acá va lo que va a recibir, las notificaciones están serializadas en JSON
////            return new ResponseEntity<>(notifications, headers, HttpStatus.OK);
//            model.addAttribute("notifications",notifications);
//            model.addAttribute("userMail",usuario.getMail());
//            return "notifications";
//        } catch (ErrorService es) {
//            model.put("error", es.getMessage());
//            return "error";
//        }
//    }

    @PostMapping("/edit-profile")
    public String register (ModelMap model , HttpSession session, @RequestParam(required = false) Integer id, @RequestParam String title, @RequestParam String content, @RequestParam Chanel channel) throws ErrorService {

        Usuario login = (Usuario) session.getAttribute("usersession");
        if(login == null) {
            return "redirect:/login";
        }
        try {
            if (id == null || id.describeConstable().isEmpty()) {
                notificationService.create(login.getId(), title, content, channel);
            } else {
                notificationService.modify(login.getId(), id, title, content, channel);
            }
            return "redirect:/logged";
        } catch (ErrorService es) {
            Notification notification = new Notification();
            notification.setTitle(title);
            notification.setContent(content);
            notification.setChannel(channel);
            model.put("channels", Chanel.values());
            model.put("profile", notification);
            model.put("error", es);
            return "notification";
        }
    }

    @GetMapping("/my-notifications")
    public String show(HttpSession session , ModelMap model){
        Usuario login = (Usuario)  session.getAttribute("usersession");
        if(login == null) {
            return "redirect:/login";
        }
        List<Notification> notifications = notificationService.consult(login.getId());
        model.put("notifications", notifications);
        model.addAttribute("userMail",login.getMail());
        return "notifications";
    }

    @PostMapping("/delete")
    public String delete (HttpSession session, @RequestParam Integer id) throws ErrorService {
        try {
            Usuario login = (Usuario) session.getAttribute("usersession");
            notificationService.delete(login.getId(), id);
            return "redirect:/notification/my-notifications";
        } catch (ErrorService es) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, es);
        }
    return "redirect:/notification/my-notifications";
    }



}
