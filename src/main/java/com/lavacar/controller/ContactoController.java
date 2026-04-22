package com.lavacar.controller;

import com.lavacar.domain.Producto;
import com.lavacar.domain.Usuario;
import com.lavacar.repository.CategoriaRepository;
import com.lavacar.repository.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ContactoController {

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }
}