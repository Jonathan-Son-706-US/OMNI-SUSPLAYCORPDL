package com.pedidos.capasP.Controllers;
import com.pedidos.capasP.ProductoDTO.PedidoConsultaDTO;
import com.pedidos.capasP.models.Pedido;
import com.pedidos.capasP.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/Pedido")
@RestController
public class PedidoControllers {
    private final PedidoService pedidoService;

    public PedidoControllers(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> Listar(){
        return pedidoService.listarPedidos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            Pedido pedido = pedidoService.buscarPorId(id);
            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            String mensajeLimpio = e.getMessage().replace("Error al procesar el pedido: ", "");
            return ResponseEntity.status(404).body(Map.of("Problema", mensajeLimpio));
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> buscarPorEstado(@PathVariable String estado) {
        try {
            List<Pedido> pedidos = pedidoService.buscarPorEstado(estado);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("Problema", e.getMessage()));
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearPedido(@RequestBody PedidoConsultaDTO request) {
        try {
            Pedido nuevoPedido = pedidoService.crearPedidoConValidacionStock(
                    request.getPedido(),
                    request.getItems()
            );
            return ResponseEntity.status(201).body(nuevoPedido);
        } catch (Exception e) {
            String mensajeLimpio = e.getMessage().replace("Error al procesar el pedido: ","");
            return ResponseEntity.status(400).body(Map.of("Problema", mensajeLimpio));
        }
    }
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable Integer id) {
        try {
            boolean resultado = pedidoService.eliminarPedido(id);
            if (resultado) {
                return ResponseEntity.ok(Map.of("mensaje", "Pedido cancelado exitosamente y los productos han sido devueltos"));
            }
            return ResponseEntity.status(404).body(Map.of("Problema", "No se encontró el pedido para cancelar."));
        } catch (Exception e) {
            String mensajeLimpio = e.getMessage();
            return ResponseEntity.status(400).body(Map.of("Problema", mensajeLimpio));
        }
    }
}
