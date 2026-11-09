package com.pedidos.capasP.service;
import com.pedidos.capasP.models.Pedido;
import java.util.List;
import com.pedidos.capasP.ProductoDTO.ItemPedidoDTO;

public interface PedidoService {
    List<Pedido> listarPedidos();
    Pedido buscarPorId(Integer id);
    Pedido guardarPedido(Pedido pedido);
    Pedido crearPedidoConValidacionStock(Pedido pedido, List<ItemPedidoDTO> items);
    boolean eliminarPedido(Integer id);
    List<Pedido> buscarPorEstado(String estado);
}

