package com.pedidos.capasP.ProductoDTO;

public class ConsumoStock {
    private Integer cantidad_en_stock;

    public ConsumoStock(Integer cantidad_en_stock) {
        this.cantidad_en_stock = cantidad_en_stock;
    }
    public Integer getCantidad_en_stock() {
        return cantidad_en_stock;
    }
    public void setCantidad_en_stock(Integer cantidad_en_stock) {
        this.cantidad_en_stock = cantidad_en_stock;
    }
}
