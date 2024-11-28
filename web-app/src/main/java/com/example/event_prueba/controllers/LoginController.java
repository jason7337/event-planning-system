package com.example.event_prueba.controllers;

import com.example.event_prueba.models.UsuarioModel;
import com.example.event_prueba.service.UsuarioService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
    @Autowired
    private UsuarioService usuarioService;
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginForm(Model model){
        model.addAttribute("usuarioModel", new UsuarioModel());
        return "pages/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute(name = "usuarioModel") UsuarioModel usuarioModel, Model model){
        String correo = usuarioModel.getCorreoElectronico();
        String contraseña = usuarioModel.getContraseña();
        UsuarioModel usuario = usuarioService.buscarPorCorreo(correo);

        if (usuario != null && BCrypt.checkpw(contraseña, usuario.getContraseña()) && "usuario".equals(usuario.getTipousuario())) {
            model.addAttribute("nombre", usuario.getNombre());
            return "pages/index";
        }
        return "pages/login";
    }
}
