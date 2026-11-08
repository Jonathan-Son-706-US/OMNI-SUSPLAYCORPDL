package com.pedidos.capasP.service;
import com.pedidos.capasP.models.DetallePedido;
import com.pedidos.capasP.repositories.DetallePedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService{
    private final DetallePedidoRepository detallePedidoRepository;

    public DetallePedidoServiceImpl(DetallePedidoRepository detallePedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
    }

    @Override
    public DetallePedido guardarDetalle(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }

    @Override
    public List<DetallePedido> listarDetalles() {
        return detallePedidoRepository.findAll();
    }

    @Override
    public DetallePedido BuscarPorId(Integer id) {
        return detallePedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontró ningún detalle de pedido con el ID: " + id));
    }
}
