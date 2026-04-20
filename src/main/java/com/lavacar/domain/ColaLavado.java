package com.lavacar.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "cola_lavado")
public class ColaLavado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idColaLavado;

    private String clienteNombre;
    private String clienteContacto;
    private String placa;
    private String vehiculo;
    private String estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime inicioProceso;
    private LocalDateTime finEstimada;
    private String observaciones;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_lavado")
    private TipoLavado tipoLavado;

    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (estado == null || estado.isBlank()) {
            estado = "PENDIENTE";
        }
    }

    public Long getIdColaLavado() {
        return idColaLavado;
    }

    public void setIdColaLavado(Long idColaLavado) {
        this.idColaLavado = idColaLavado;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteContacto() {
        return clienteContacto;
    }

    public void setClienteContacto(String clienteContacto) {
        this.clienteContacto = clienteContacto;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getInicioProceso() {
        return inicioProceso;
    }

    public void setInicioProceso(LocalDateTime inicioProceso) {
        this.inicioProceso = inicioProceso;
    }

    public LocalDateTime getFinEstimada() {
        return finEstimada;
    }

    public void setFinEstimada(LocalDateTime finEstimada) {
        this.finEstimada = finEstimada;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public TipoLavado getTipoLavado() {
        return tipoLavado;
    }

    public void setTipoLavado(TipoLavado tipoLavado) {
        this.tipoLavado = tipoLavado;
    }
}
