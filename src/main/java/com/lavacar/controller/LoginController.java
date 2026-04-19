package com.lavacar.controller;

import com.lavacar.domain.Usuario;
import com.lavacar.service.UsuarioService;
import com.lavacar.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository; 

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        Usuario user = usuarioService.login(username, password);

        if (user != null) {
            session.setAttribute("usuarioLogueado", user);
            return "redirect:/producto/listado";
        } else {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login";
        }
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute Usuario usuario) {

        usuario.setRol("USER");
        usuario.setActivo(true);

        usuarioRepository.save(usuario);

        return "redirect:/login";
    }
}