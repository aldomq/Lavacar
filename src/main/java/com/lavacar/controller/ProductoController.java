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
@RequestMapping("/producto")
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoController(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping("/listado")
    public String inicio(@RequestParam(required = false) String nombre,
                         @RequestParam(required = false) Long categoriaId,
                         HttpSession session,
                         Model model) {
        if (!estaLogueado(session)) {
            return "redirect:/login";
        }

        String nombreFiltro = normalizarTexto(nombre);
        Long categoriaFiltro = categoriaId;

        var productos = productoRepository.filtrarProductos(nombreFiltro, categoriaFiltro);
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("nombreFiltro", nombre != null ? nombre : "");
        model.addAttribute("categoriaFiltro", categoriaId);
        return "/producto/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("modoEdicion", false);
        return "/producto/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            return "redirect:/producto/listado";
        }

        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("modoEdicion", true);
        return "/producto/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        productoRepository.save(producto);
        return "redirect:/producto/listado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        productoRepository.deleteById(id);
        return "redirect:/producto/listado";
    }

    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        return usuario != null && "ADMIN".equalsIgnoreCase(usuario.getRol());
    }

    private boolean estaLogueado(HttpSession session) {
        return session.getAttribute("usuarioLogueado") != null;
    }

    private String normalizarTexto(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
