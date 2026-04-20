package com.lavacar.controller;

import com.lavacar.domain.ColaLavado;
import com.lavacar.domain.TipoLavado;
import com.lavacar.domain.Usuario;
import com.lavacar.repository.ColaLavadoRepository;
import com.lavacar.repository.TipoLavadoRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/lavados")
public class LavadoController {

    private final ColaLavadoRepository colaLavadoRepository;
    private final TipoLavadoRepository tipoLavadoRepository;

    public LavadoController(ColaLavadoRepository colaLavadoRepository, TipoLavadoRepository tipoLavadoRepository) {
        this.colaLavadoRepository = colaLavadoRepository;
        this.tipoLavadoRepository = tipoLavadoRepository;
    }

    @GetMapping("/cola")
    public String cola(Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        var cola = colaLavadoRepository.findAll();
        model.addAttribute("colaLavado", cola);
        model.addAttribute("totalCola", cola.size());
        return "/lavado/cola";
    }

    @GetMapping("/cola/nuevo")
    public String nuevoIngreso(Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        model.addAttribute("colaLavado", new ColaLavado());
        model.addAttribute("tiposLavado", tipoLavadoRepository.findAll());
        model.addAttribute("modoEdicion", false);
        return "/lavado/formulario-cola";
    }

    @GetMapping("/cola/editar/{id}")
    public String editarIngreso(@PathVariable Long id, Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        ColaLavado colaLavado = colaLavadoRepository.findById(id).orElse(null);
        if (colaLavado == null) {
            return "redirect:/lavados/cola";
        }

        model.addAttribute("colaLavado", colaLavado);
        model.addAttribute("tiposLavado", tipoLavadoRepository.findAll());
        model.addAttribute("modoEdicion", true);
        return "/lavado/formulario-cola";
    }

    @PostMapping("/cola/guardar")
    public String guardarIngreso(@ModelAttribute ColaLavado colaLavado, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        colaLavadoRepository.save(colaLavado);
        return "redirect:/lavados/cola";
    }

    @PostMapping("/cola/{id}/estado")
    public String actualizarEstado(@PathVariable Long id, @RequestParam String estado, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        colaLavadoRepository.findById(id).ifPresent(item -> {
            item.setEstado(estado);

            if ("EN_PROCESO".equalsIgnoreCase(estado)) {
                LocalDateTime ahora = LocalDateTime.now();
                item.setInicioProceso(ahora);
                Integer duracion = item.getTipoLavado() != null ? item.getTipoLavado().getDuracionMinutos() : null;
                item.setFinEstimada(duracion != null ? ahora.plusMinutes(duracion) : null);
            } else if ("TERMINADO".equalsIgnoreCase(estado) || "ENTREGADO".equalsIgnoreCase(estado)) {
                if (item.getInicioProceso() == null) {
                    item.setInicioProceso(LocalDateTime.now());
                }
                item.setFinEstimada(LocalDateTime.now());
            } else {
                item.setInicioProceso(null);
                item.setFinEstimada(null);
            }

            colaLavadoRepository.save(item);
        });

        return "redirect:/lavados/cola";
    }

    @PostMapping("/cola/eliminar/{id}")
    public String eliminarIngreso(@PathVariable Long id, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        colaLavadoRepository.deleteById(id);
        return "redirect:/lavados/cola";
    }

    @GetMapping("/tipos")
    public String tipos(Model model, HttpSession session) {
        if (!estaLogueado(session)) {
            return "redirect:/login";
        }

        var tipos = tipoLavadoRepository.findAll();
        model.addAttribute("tiposLavado", tipos);
        model.addAttribute("totalTipos", tipos.size());
        model.addAttribute("esAdmin", esAdmin(session));
        return "/lavado/tipos";
    }

    @GetMapping("/tipos/nuevo")
    public String nuevoTipo(Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        model.addAttribute("tipoLavado", new TipoLavado());
        model.addAttribute("modoEdicion", false);
        return "/lavado/formulario-tipo";
    }

    @GetMapping("/tipos/editar/{id}")
    public String editarTipo(@PathVariable Long id, Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        TipoLavado tipoLavado = tipoLavadoRepository.findById(id).orElse(null);
        if (tipoLavado == null) {
            return "redirect:/lavados/tipos";
        }

        model.addAttribute("tipoLavado", tipoLavado);
        model.addAttribute("modoEdicion", true);
        return "/lavado/formulario-tipo";
    }

    @PostMapping("/tipos/guardar")
    public String guardarTipo(@ModelAttribute TipoLavado tipoLavado, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        tipoLavadoRepository.save(tipoLavado);
        return "redirect:/lavados/tipos";
    }

    @PostMapping("/tipos/eliminar/{id}")
    public String eliminarTipo(@PathVariable Long id, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/producto/listado";
        }

        tipoLavadoRepository.deleteById(id);
        return "redirect:/lavados/tipos";
    }

    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        return usuario != null && "ADMIN".equalsIgnoreCase(usuario.getRol());
    }

    private boolean estaLogueado(HttpSession session) {
        return session.getAttribute("usuarioLogueado") != null;
    }
}
