package com.lavacar.controller;

import com.lavacar.domain.Producto;
import com.lavacar.domain.Categoria;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @GetMapping("/listado")
    public String inicio(Model model) {

        Categoria cat1 = new Categoria(1L, "Shampoo", "Productos para lavado");

        List<Producto> productos = new ArrayList<>();

        productos.add(new Producto(1L, "Shampoo Espuma Activa", "Shampoo para carro", 3500, 10, cat1));
        productos.add(new Producto(2L, "Cera líquida", "Cera para brillo", 5000, 5, cat1));

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