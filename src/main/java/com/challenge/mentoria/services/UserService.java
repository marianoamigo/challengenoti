package com.challenge.mentoria.services;
/*en Servicio se lleva a cabo la lógica del negocio*/

import com.challenge.mentoria.errors.ErrorService;
import com.challenge.mentoria.models.Usuario;
import com.challenge.mentoria.repositories.IUserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class UserService implements UserDetailsService {

    @Autowired /*esta anotacion indica que se auto inicialice la instancia de IUserRepository*/
    private IUserRepository userRepository;

    @Transactional
    public void register(String mail, String pass, String pass2) throws ErrorService {
        validate(mail,pass, pass2);
        Usuario usuario = new Usuario();
        usuario.setMail(mail);
        String encript = new BCryptPasswordEncoder().encode(pass);
        usuario.setPass(encript);

        userRepository.save(usuario);
    }

    @Transactional
    public void modify(Integer id, String mail, String pass, String pass2) throws ErrorService {

        validate(mail,pass, pass2);
        Optional<Usuario> respuesta = userRepository.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setMail(mail);
            String encript = new BCryptPasswordEncoder().encode(pass);
            usuario.setPass(encript);
            userRepository.save(usuario);
        } else {
            throw new ErrorService("No se encontró usuario solicitado");
        }


    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        //este método es llamado cuando un usuario intenta loguear
        Usuario usuario = userRepository.findByMail(mail);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
            List<GrantedAuthority> permissions = new ArrayList<>();
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USER_REGISTERED"); //asigna el permiso de usuario registrado
//            GrantedAuthority p2 = new SimpleGrantedAuthority();
//            GrantedAuthority p3 = new SimpleGrantedAuthority();
        permissions.add(p1);

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes(); //reupera los atributos del request http
        HttpSession session = attr.getRequest().getSession(true); //solicita los datos de sesion de esa solicitud http
        session.setAttribute("usersession",usuario); //crea un atributo en los datos de sesión "usersession" y ahi le guarda el usuario traido de la DB

            User user = new User(usuario.getMail(), usuario.getPass(), permissions);
            return user;
    }

    private void validate(String mail, String pass, String pass2) throws ErrorService {
        if (mail == null || mail.isEmpty()) {
            throw new ErrorService("El mail no puede ser nulo");
        }
        if (pass == null || pass.isEmpty() || pass.length() < 8) {
            throw new ErrorService("La contraseña no puede ser nula y debe tener mas de 8 digitos");
        }

        if (!pass.equals(pass2)) {
            throw new ErrorService("Las contraseñas no coinciden");
        }
    }

    public Usuario findUserById(Integer id) throws ErrorService {
        return userRepository.findById(id).orElseThrow(() ->
                new ErrorService("No se encontró el usuario con ID: " + id)
        );
    }

}
