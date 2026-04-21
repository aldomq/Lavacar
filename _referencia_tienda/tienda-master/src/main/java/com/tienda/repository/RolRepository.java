package com.tienda.repository;

import com.tienda.domain.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {
 //Se utiliza para crear un usuario por defecto
    public Optional<Rol> findByRol(String rol);

}
