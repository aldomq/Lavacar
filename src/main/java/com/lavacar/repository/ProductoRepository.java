package com.lavacar.repository;

import com.lavacar.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("""
        SELECT p
        FROM Producto p
        LEFT JOIN p.categoria c
        WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
          AND (:categoriaId IS NULL OR c.idCategoria = :categoriaId)
    """)
    List<Producto> filtrarProductos(@Param("nombre") String nombre, @Param("categoriaId") Long categoriaId);
}
