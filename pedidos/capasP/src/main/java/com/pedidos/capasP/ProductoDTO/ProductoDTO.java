package com.pedidos.capasP.ProductoDTO;

public class ProductoDTO {
    private Integer id_productos;
    private String nombre_producto;
    private Double precio_producto;
    private Integer stock_producto;

    public Integer getId_productos() {
        return id_productos;
    }

    public void setId_productos(Integer id_productos) {
        this.id_productos = id_productos;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public Double getPrecio_producto() {
        return precio_producto;
    }

    public void setPrecio_producto(Double precio_producto) {
        this.precio_producto = precio_producto;
    }

    public Integer getStock_producto() {
        return stock_producto;
    }

    public void setStock_producto(Integer stock_producto) {
        this.stock_producto = stock_producto;
    }
}
