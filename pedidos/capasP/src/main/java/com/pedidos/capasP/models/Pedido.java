package com.pedidos.capasP.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pedido;
    private String usuario;
    private Double total ;
    @Pattern(regexp = "ACTIVO|CANCELADO",
            message = "El estado debe ser estrictamente: ACTIVO, CANCELADO ")
    private String estado;
    //Problemas de fecha al crear parcheado con importacion de Creationtimestamp que aparecia null al crear siempre
    @CreationTimestamp
        @Column(name = "fecha_emision", updatable = false)
        private LocalDateTime fecha_emision;

    public Integer getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(Integer id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(LocalDateTime fecha_emision) {
        this.fecha_emision = fecha_emision;
    }
}
