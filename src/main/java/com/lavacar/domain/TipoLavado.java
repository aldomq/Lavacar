package com.lavacar.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo_lavado")
public class TipoLavado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoLavado;

    private String nombre;
    private String descripcion;
    private double precio;
    private Integer duracionMinutos;
    private Boolean activo = true;

    public Long getIdTipoLavado() {
        return idTipoLavado;
    }

    public void setIdTipoLavado(Long idTipoLavado) {
        this.idTipoLavado = idTipoLavado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
