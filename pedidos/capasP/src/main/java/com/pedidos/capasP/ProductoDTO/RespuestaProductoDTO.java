package com.pedidos.capasP.ProductoDTO;

public class RespuestaProductoDTO {
    private String mensaje;
    private ProductoDTO producto;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }
}
