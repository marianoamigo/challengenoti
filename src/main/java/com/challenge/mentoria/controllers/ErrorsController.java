package com.challenge.mentoria.controllers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorsController implements ErrorController {

    @RequestMapping(value= "/error", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) { //lo que se recupere del request con http va a entrar al método
        ModelAndView errorPage = new ModelAndView("error");
        String errorMsg = "";
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                errorMsg = "El recurso solicitado no existe";
                break;
            }
            case 401: {
                errorMsg = "No se encuentra autorizado";
                break;
            }
            case 403: {
                errorMsg = "No tiene permisos para acceder al recurso";
                break;
            }
            case 404: {
                errorMsg = "El recurso solicitado no fue encontrado";
                break;
            }
            case 500: {
                errorMsg = "Ocurrió un error interno";
                break;
            }
        }

        errorPage.addObject("code", httpErrorCode);
        errorPage.addObject("message", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        Map map = httpRequest.getParameterMap();
        for (Object key: map.keySet()) {
            String[] values = (String[]) map.get(key);
            for (String value: values) {
                System.out.println(key.toString() + ": " + value);
            }

        }
        Enumeration<String> attributes = httpRequest.getAttributeNames();
        while (attributes.hasMoreElements()) {
            String key = attributes.nextElement();
            System.out.println(key + ": " + httpRequest.getAttribute(key));
        }
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");

    }


}
