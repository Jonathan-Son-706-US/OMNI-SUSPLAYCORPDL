package com.pedidos.capasP.service;
import com.pedidos.capasP.models.DetallePedido;
import java.util.List;
import java.util.Optional;

public interface DetallePedidoService {
    DetallePedido guardarDetalle(DetallePedido detallepedido);
    List<DetallePedido> listarDetalles();
    DetallePedido BuscarPorId(Integer id);
}
