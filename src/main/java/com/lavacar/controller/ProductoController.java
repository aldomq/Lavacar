package com.lavacar.controller;

import com.lavacar.domain.Producto;
import com.lavacar.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/listado")
    public String inicio(Model model) {

        var productos = productoRepository.findAll();

        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());

        return "/producto/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "/producto/formulario";
    }
}