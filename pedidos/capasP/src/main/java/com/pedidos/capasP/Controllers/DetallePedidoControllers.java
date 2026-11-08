package com.pedidos.capasP.Controllers;
import com.pedidos.capasP.models.DetallePedido;
import com.pedidos.capasP.service.DetallePedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@RequestMapping("/DetallePedido")
@RestController
public class DetallePedidoControllers {
    private final DetallePedidoService detallePedidoService;

    public DetallePedidoControllers(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }
    @GetMapping
    public List<DetallePedido> listarDetalles() {
        return detallePedidoService.listarDetalles();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            DetallePedido detalle = detallePedidoService.BuscarPorId(id);
            return ResponseEntity.ok(detalle);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("Problema", e.getMessage()));
        }
    }
}
