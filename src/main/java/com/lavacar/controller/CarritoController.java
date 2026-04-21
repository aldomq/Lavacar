package com.lavacar.controller;

import com.lavacar.domain.CarritoItem;
import com.lavacar.domain.Producto;
import com.lavacar.domain.Usuario;
import com.lavacar.repository.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private static final String CARRITO_SESSION_KEY = "carrito";

    private final ProductoRepository productoRepository;

    public CarritoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public String raiz() {
        return "redirect:/carrito/listado";
    }

    @GetMapping("/listado")
    public String verCarrito(@RequestParam(required = false) String exito,
                             HttpSession session,
                             Model model) {
        if (!esUsuario(session)) {
            return "redirect:/";
        }

        List<CarritoItem> carrito = obtenerCarrito(session);
        model.addAttribute("carrito", carrito);
        model.addAttribute("totalCarrito", calcularTotal(carrito));
        model.addAttribute("cantidadItems", calcularCantidad(carrito));
        model.addAttribute("compraExitosa", exito != null);
        return "carrito/listado";
    }

    @PostMapping("/agregar/{id}")
    public String agregar(@PathVariable Long id,
                          @RequestParam(defaultValue = "1") int cantidad,
                          HttpSession session) {
        if (!esUsuario(session)) {
            return "redirect:/";
        }

        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            return "redirect:/producto/listado";
        }

        int cantidadFinal = Math.max(1, cantidad);
        List<CarritoItem> carrito = obtenerCarrito(session);

        for (CarritoItem item : carrito) {
            if (item.getProductoId().equals(producto.getIdProducto())) {
                item.setCantidad(item.getCantidad() + cantidadFinal);
                session.setAttribute(CARRITO_SESSION_KEY, carrito);
                return "redirect:/producto/listado";
            }
        }

        carrito.add(new CarritoItem(
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getImagenUrl(),
                producto.getPrecio(),
                cantidadFinal
        ));

        session.setAttribute(CARRITO_SESSION_KEY, carrito);
        return "redirect:/producto/listado";
    }

    @GetMapping("/modificar/{id}")
    public String modificarVista(@PathVariable Long id, HttpSession session, Model model) {
        if (!esUsuario(session)) {
            return "redirect:/";
        }

        CarritoItem item = buscarItem(obtenerCarrito(session), id);
        if (item == null) {
            return "redirect:/carrito/listado";
        }

        model.addAttribute("item", item);
        return "carrito/modifica";
    }

    @PostMapping("/actualizar")
    public String actualizarDesdeVista(@RequestParam Long productoId,
                                       @RequestParam int cantidad,
                                       HttpSession session) {
        return actualizar(productoId, cantidad, session);
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam int cantidad,
                             HttpSession session) {
        if (!esUsuario(session)) {
            return "redirect:/";
        }

        List<CarritoItem> carrito = obtenerCarrito(session);
        if (cantidad <= 0) {
            carrito.removeIf(item -> item.getProductoId().equals(id));
        } else {
            for (CarritoItem item : carrito) {
                if (item.getProductoId().equals(id)) {
                    item.setCantidad(cantidad);
                    break;
                }
            }
        }

        session.setAttribute(CARRITO_SESSION_KEY, carrito);
        return "redirect:/carrito";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (!esUsuario(session)) {
            return "redirect:/";
        }

        List<CarritoItem> carrito = obtenerCarrito(session);
        carrito.removeIf(item -> item.getProductoId().equals(id));
        session.setAttribute(CARRITO_SESSION_KEY, carrito);
        return "redirect:/carrito/listado";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        if (!esUsuario(session)) {
            return "redirect:/";
        }

        List<CarritoItem> carrito = obtenerCarrito(session);
        if (carrito.isEmpty()) {
            return "redirect:/carrito/listado";
        }

        model.addAttribute("carrito", carrito);
        model.addAttribute("totalCarrito", calcularTotal(carrito));
        return "carrito/checkout";
    }

    @PostMapping("/finalizar")
    public String finalizar(@RequestParam String metodoPago,
                            @RequestParam(required = false) String paypalCorreo,
                            @RequestParam(required = false) String nombreTitular,
                            @RequestParam(required = false) String numeroTarjeta,
                            HttpSession session) {
        if (!esUsuario(session)) {
            return "redirect:/";
        }

        List<CarritoItem> carrito = obtenerCarrito(session);
        if (carrito.isEmpty()) {
            return "redirect:/carrito/listado";
        }

        boolean pagoPaypal = "PAYPAL".equalsIgnoreCase(metodoPago) && paypalCorreo != null && !paypalCorreo.isBlank();
        boolean pagoTarjeta = "TARJETA".equalsIgnoreCase(metodoPago)
                && nombreTitular != null && !nombreTitular.isBlank()
                && numeroTarjeta != null && !numeroTarjeta.isBlank();

        if (!pagoPaypal && !pagoTarjeta) {
            return "redirect:/carrito/checkout";
        }

        session.setAttribute(CARRITO_SESSION_KEY, new ArrayList<CarritoItem>());
        return "redirect:/carrito/listado?exito=1";
    }

    @SuppressWarnings("unchecked")
    private List<CarritoItem> obtenerCarrito(HttpSession session) {
        Object carrito = session.getAttribute(CARRITO_SESSION_KEY);
        if (carrito instanceof List<?>) {
            return (List<CarritoItem>) carrito;
        }
        List<CarritoItem> nuevoCarrito = new ArrayList<>();
        session.setAttribute(CARRITO_SESSION_KEY, nuevoCarrito);
        return nuevoCarrito;
    }

    private double calcularTotal(List<CarritoItem> carrito) {
        return carrito.stream().mapToDouble(CarritoItem::getSubtotal).sum();
    }

    private CarritoItem buscarItem(List<CarritoItem> carrito, Long productoId) {
        for (CarritoItem item : carrito) {
            if (item.getProductoId().equals(productoId)) {
                return item;
            }
        }
        return null;
    }

    private int calcularCantidad(List<CarritoItem> carrito) {
        return carrito.stream().mapToInt(CarritoItem::getCantidad).sum();
    }

    private boolean esUsuario(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        return usuario != null && "USER".equalsIgnoreCase(usuario.getRol());
    }
}
