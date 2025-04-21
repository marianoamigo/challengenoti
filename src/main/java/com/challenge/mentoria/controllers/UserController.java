package com.challenge.mentoria.controllers;

import com.challenge.mentoria.errors.ErrorService;
import com.challenge.mentoria.models.Usuario;
import com.challenge.mentoria.repositories.IUserRepository;
import com.challenge.mentoria.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole ('ROLE_USER_REGISTERED')")
    @GetMapping("/edit-profile")
    public String editProfile (HttpSession session, @RequestParam int id, ModelMap model) throws ErrorService{
            Usuario login = (Usuario) session.getAttribute("usersession");
            if(login == null || login.getId() != id){
                return "redirect:/logged";
            }
            try {
                Usuario usuario = userService.findById1(id);
                model.addAttribute("profile", usuario);
            } catch (ErrorService e) {
                model.addAttribute("error", e.getMessage());
            }
            return "profile";
    }

    @PreAuthorize("hasAnyRole ('ROLE_USER_REGISTERED')")
    @PostMapping("/edit-profile")
    public String register (ModelMap model , HttpSession session, @RequestParam int id, @RequestParam String mail, @RequestParam String pass1, @RequestParam String pass2) throws ErrorService{
        Usuario usuario = null;
        try {
            Usuario login = (Usuario) session.getAttribute("usersession");
            if(login == null || login.getId() != id){
                return "redirect:/logged";
            }
            usuario = userService.findById1(id);
            userService.modify(id, mail, pass1, pass2);
            session.setAttribute("usersession",usuario);
            return "redirect:/logged";
        } catch (ErrorService es) {
            model.put("error",es.getMessage());
            model.put("profile", usuario);

            return "profile";
        }
    }

}
