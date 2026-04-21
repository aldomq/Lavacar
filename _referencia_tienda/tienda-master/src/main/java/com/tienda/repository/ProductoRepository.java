package com.tienda.repository;

import com.tienda.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query; 
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer>{
    public List<Producto> findByActivoTrue();
    //concsulta Dervidada
    public List<Producto> findByPrecioBetweenOrderByPrecioAsc(double precioInf, double precioSup);
     //concsulta JPQL
    @Query(value="SELECT p FROM Producto p WHERE p.precio BETWEEN :precioInf and :precioSup ORDER BY p.precio ASC")
    public List<Producto> consultaJPQL(double precioInf, double precioSup);
    
         //concsulta SQL
    @Query(nativeQuery=true,
            value="SELECT * FROM producto p WHERE p.precio BETWEEN :precioInf and :precioSup ORDER BY p.precio ASC")
    public List<Producto> consultaSQL(double precioInf, double precioSup);
    
    //Consulta de existencias
@Query("SELECT p FROM Producto p WHERE p.existencias >= :existenciasMin")
List<Producto> productosConExistencias(int existenciasMin);
}
