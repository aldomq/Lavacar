package com.lavacar.controller;

import com.lavacar.domain.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String admin(HttpSession session, Model model) {

        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");

  
        if (user == null) {
            return "redirect:/login";
        }

      
        if (!user.getRol().equals("ADMIN")) {
            return "redirect:/producto/listado";
        }

        model.addAttribute("usuario", user);
        return "admin";
    }
}