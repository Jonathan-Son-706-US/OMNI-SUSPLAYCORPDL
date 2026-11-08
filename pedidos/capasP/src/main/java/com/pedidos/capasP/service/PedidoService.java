package com.pedidos.capasP.service;
import com.pedidos.capasP.models.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    List<Pedido> listarPedidos();
    Pedido buscarPorId(Integer id);;
    Pedido guardarPedido(Pedido pedido);
    Pedido crearPedidoConValidacionStock(Pedido pedido, Integer productoId, Integer cantidadComprada);
    boolean eliminarPedido(Integer id);
    List<Pedido> buscarPorEstado(String estado);
}

