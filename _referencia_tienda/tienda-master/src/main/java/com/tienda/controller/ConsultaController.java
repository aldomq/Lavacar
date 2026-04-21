package com.tienda.controller;

import com.tienda.domain.Producto;
import com.tienda.service.ProductoService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.tienda.service.CategoriaService;
@Controller
@RequestMapping("/consultas")
public class ConsultaController {
    private final ProductoService productoService;

    public ConsultaController( ProductoService productoService) {
      this.productoService = productoService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        return "/consultas/listado";
    }
@PostMapping("/consultaDerivada")
public String consultaDerivada(@RequestParam() double precioInf,
        @RequestParam double precioSup,
        Model model){
   var productos = productoService.consultaDerivada(precioInf,precioSup);
        model.addAttribute("productos", productos);
         model.addAttribute("precioInf", precioInf);
          model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
}
@PostMapping("/consultajpql")
public String consultaJPQL(@RequestParam() double precioInf,
        @RequestParam double precioSup,
        Model model){
   var productos = productoService.consultaJPQL(precioInf,precioSup);
        model.addAttribute("productos", productos);
         model.addAttribute("precioInf", precioInf);
          model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
}
@PostMapping("/consultasql")
public String consultaSQL(@RequestParam() double precioInf,
        @RequestParam double precioSup,
        Model model){
   var productos = productoService.consultaSQL(precioInf,precioSup);
        model.addAttribute("productos", productos);
         model.addAttribute("precioInf", precioInf);
          model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
}
@PostMapping("/consultaExistencias")
public String consultaExistencias(
        @RequestParam int existenciasMin,
        Model model) {

    var lista = productoService.productosConExistencias(existenciasMin);

    model.addAttribute("productos", lista);
    return "/consultas/listado";
}
}
