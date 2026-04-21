package com.lavacar.controller;

import com.lavacar.domain.CarritoItem;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("carritoCantidad")
    public int carritoCantidad(HttpSession session) {
        Object carrito = session.getAttribute("carrito");
        if (!(carrito instanceof List<?> lista)) {
            return 0;
        }

        int total = 0;
        for (Object item : lista) {
            if (item instanceof CarritoItem carritoItem) {
                total += carritoItem.getCantidad();
            }
        }
        return total;
    }
}
