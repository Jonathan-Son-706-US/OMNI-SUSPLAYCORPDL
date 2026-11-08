package com.pedidos.capasP.Controllers;
import com.pedidos.capasP.models.Pedido;
public class PedidoConsultaDTO {
    private Pedido pedido;
    private Integer productoId;
    private Integer cantidad;

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
