package com.challenge.mentoria.controllers;

import com.challenge.mentoria.errors.ErrorService;
import com.challenge.mentoria.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PreAuthorize("hasAnyRole ('ROLE_USER_REGISTERED')") //solo un usuario autenticado puede acceder a la pagina /logged ya que ahi van los que ya loguearon
    @GetMapping("/logged")
    public String logged(){
        return "logged";
    }
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) { //el parametro error puede venir pero no siempre
        if(logout!= null) {
            model.put("logout","Ha salido correctamente del sistema");
        }

        if(error!= null){
            model.put("error","Usuario o clave incorrectos");
        }
        return "login"; // va a buscar login.html
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // va a buscar register.html
    }
    @PostMapping("/registration")
    public String registration(ModelMap model ,@RequestParam(required = false) String mail, @RequestParam(required = false) String pass1, @RequestParam (required = false) String pass2) {
//        System.out.println("Mail: "+mail);      para ver si el servidor recibe los parámetros por http
//        System.out.println("Pass1: "+pass1);
//        System.out.println("Pass2: "+pass2);
        try {
            userService.register(mail,pass1, pass2);
        } catch (ErrorService es) {
            model.put("error", es.getMessage());
            model.put("mail",mail);
            model.put("pass1",pass1);
            model.put("pass2",pass2); //trae de nuevo lo que estaba escrito en caso de un error
            return "register";
        }
        model.put("title","Bienvenido a la App de Notificaciones");
        model.put("description","Registro generado de forma exitosa");
        return "success"; // va a buscar success.html
    }
}
