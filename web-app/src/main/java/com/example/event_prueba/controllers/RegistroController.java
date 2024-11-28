package com.example.event_prueba.controllers;

import com.example.event_prueba.models.UsuarioModel;
import com.example.event_prueba.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegistroController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registrar")
    public String getRegisterForm(Model model)
    {
        model.addAttribute("usuarioModel", new UsuarioModel());
        return "pages/registrar";
    }
    //Registrar nuevo Usuario
    @PostMapping("/registrar")
    public String registrarUsuario(@ModelAttribute(name = "usuarioModel")UsuarioModel usuarioModel, Model model)
    {
        String correo = usuarioModel.getCorreoElectronico();
        String contraseña = usuarioModel.getContraseña();
        String tipoUsuario = usuarioModel.getTipousuario();
        String nombre = usuarioModel.getNombre();
        String telefono = usuarioModel.getTelefono();
        UsuarioModel usuario = usuarioService.buscarPorCorreo(correo);

        if(usuario != null)
        {
            model.addAttribute("error", "El correo ya está registrado");
            return "pages/registrar";
        }
        String contraseñaCrypt = BCrypt.hashpw(contraseña, BCrypt.gensalt());

        UsuarioModel nuevoUsuario = new UsuarioModel();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setContraseña(contraseñaCrypt);
        nuevoUsuario.setTelefono(telefono);
        nuevoUsuario.setCorreoElectronico(correo);
        nuevoUsuario.setTipousuario("usuario");
        usuarioService.guardarUsuario(nuevoUsuario);
        return "redirect:/login";

    }
    @GetMapping("/perfil")
    public String mostrarPerfil(Model model, HttpSession session)
    {
        UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("nombre", usuario.getNombre());
            return "pages/perfil";
        }
        return "redirect:/login";
    }
    @PostMapping
    @RequestMapping("/guardar")
    public String guardar_usuario(UsuarioModel usuario, Model model, HttpSession session)
    {
        UsuarioModel usuarioExist = usuarioService.buscarId(usuario.getIdusuario());
        if(usuario.getCorreoElectronico() == null)
        {
            usuario.setCorreoElectronico(usuarioExist.getCorreoElectronico());
        }
        if(usuario.getContraseña() == null)
        {
            usuario.setContraseña(usuarioExist.getContraseña());
        }
        if(usuario.getContraseña() == null)
        {
            usuario.setContraseña(usuarioExist.getContraseña());
        }
        if(usuario.getTipousuario() == null)
        {
            usuario.setTipousuario(usuarioExist.getTipousuario());
        }
        usuarioService.guardarUsuario(usuario);
        session.setAttribute("usuario", usuario);
        return "redirect:/perfil";
    }
}
