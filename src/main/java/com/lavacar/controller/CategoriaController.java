package com.lavacar.controller;

import com.lavacar.domain.Categoria;
import com.lavacar.domain.Usuario;
import com.lavacar.repository.CategoriaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping("/listado")
    public String inicio(Model model, HttpSession session) {
        if (!estaLogueado(session)) {
            return "redirect:/login";
        }

        var categorias = categoriaRepository.findAll();
        model.addAttribute("categorias", categorias);
        model.addAttribute("totalCategorias", categorias.size());
        return "/categoria/listado";
    }

    @GetMapping("/nueva")
    public String nueva(Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/categoria/listado";
        }

        model.addAttribute("categoria", new Categoria());
        model.addAttribute("modoEdicion", false);
        return "/categoria/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/categoria/listado";
        }

        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            return "redirect:/categoria/listado";
        }

        model.addAttribute("categoria", categoria);
        model.addAttribute("modoEdicion", true);
        return "/categoria/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/categoria/listado";
        }

        categoriaRepository.save(categoria);
        return "redirect:/categoria/listado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/categoria/listado";
        }

        categoriaRepository.deleteById(id);
        return "redirect:/categoria/listado";
    }

    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        return usuario != null && "ADMIN".equalsIgnoreCase(usuario.getRol());
    }

    private boolean estaLogueado(HttpSession session) {
        return session.getAttribute("usuarioLogueado") != null;
    }
}
