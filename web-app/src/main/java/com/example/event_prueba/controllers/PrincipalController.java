package com.example.event_prueba.controllers;

import com.example.event_prueba.models.EventoModel;
import com.example.event_prueba.models.UsuarioModel;
import com.example.event_prueba.service.EventoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PrincipalController {
    @Autowired
    private EventoService eventoService;
    @GetMapping("/")
    public String principal(HttpSession session, Model model){
        UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
        if (usuario!=null){
            model.addAttribute("nombre", usuario.getNombre());
        } else {
            return "redirect:/login";
        }
        return "pages/index";
    }

    @GetMapping("/eventos")
    @RequestMapping("/eventos")
    public String principal_eventos(HttpSession session, Model model) {
        UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
        List<EventoModel> lista_eventos = eventoService.getEventos();
        model.addAttribute("data_evento", lista_eventos);
        model.addAttribute("evento", new EventoModel());
        if (usuario!=null){
            model.addAttribute("nombre", usuario.getNombre());
        }
        return "pages/eventos";
    }
}
