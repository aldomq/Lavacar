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
@RequestMapping("/constante")
public class ConstanteController {
    private final ProductoService productoService;

    public ConstanteController( ProductoService productoService) {
      this.productoService = productoService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        return "/constantes/listado";
    }
@PostMapping("/constanteDerivada")
public String constanteDerivada(@RequestParam() double precioInf,
        @RequestParam double precioSup,
        Model model){
   var productos = productoService.constanteDerivada(precioInf,precioSup);
        model.addAttribute("productos", productos);
         model.addAttribute("precioInf", precioInf);
          model.addAttribute("precioSup", precioSup);
        return "/constantes/listado";
}
@PostMapping("/constantejpql")
public String constanteJPQL(@RequestParam() double precioInf,
        @RequestParam double precioSup,
        Model model){
   var productos = productoService.constanteJPQL(precioInf,precioSup);
        model.addAttribute("productos", productos);
         model.addAttribute("precioInf", precioInf);
          model.addAttribute("precioSup", precioSup);
        return "/constantes/listado";
}
@PostMapping("/constantesql")
public String constanteSQL(@RequestParam() double precioInf,
        @RequestParam double precioSup,
        Model model){
   var productos = productoService.constanteSQL(precioInf,precioSup);
        model.addAttribute("productos", productos);
         model.addAttribute("precioInf", precioInf);
          model.addAttribute("precioSup", precioSup);
        return "/constantes/listado";
}
@PostMapping("/constanteExistencias")
public String constanteExistencias(
        @RequestParam int existenciasMin,
        Model model) {

    var lista = productoService.productosConExistencias(existenciasMin);

    model.addAttribute("productos", lista);
    return "/constantes/listado";
}
}
