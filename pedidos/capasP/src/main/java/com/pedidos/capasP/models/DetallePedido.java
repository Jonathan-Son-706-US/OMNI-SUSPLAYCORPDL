package com.pedidos.capasP.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Collection;
@Entity
@Table (name="detallepedidos")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_detalle;
    private Integer cantidad;
    private Double precio_unitario;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Pedido_ID", referencedColumnName = "id_pedido")
    @JsonIgnore
    private Pedido pedido;
    private Integer Producto_ID;

    public Integer getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(Integer id_detalle) {
        this.id_detalle = id_detalle;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(Double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Integer getProducto_ID() {
        return Producto_ID;
    }

    public void setProducto_ID(Integer producto_ID) {
        Producto_ID = producto_ID;
    }
}
