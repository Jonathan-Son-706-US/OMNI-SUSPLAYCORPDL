package com.pedidos.capasP.ProductoDTO;
import com.pedidos.capasP.models.Pedido;
import java.util.List;
public class PedidoConsultaDTO {

    private Pedido pedido;
    private List<ItemPedidoDTO> items;

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<ItemPedidoDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoDTO> items) {
        this.items = items;
    }
}
