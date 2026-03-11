package com.lavacar.controller;

import com.lavacar.domain.Categoria;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @GetMapping("/listado")
    public String listado(Model model) {
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(new Categoria(1L, "Shampoo", "Productos para lavado"));
        categorias.add(new Categoria(2L, "Ceras", "Productos para encerado"));

        model.addAttribute("categorias", categorias);
        return "categoria/listado";
    }

    @GetMapping("/nueva")
    public String nuevaCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria/formulario";
    }
}