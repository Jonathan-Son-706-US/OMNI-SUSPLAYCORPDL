package com.pedidos.capasP.service;
import com.pedidos.capasP.ProductoDTO.ConsumoStock;
import com.pedidos.capasP.ProductoDTO.ProductoDTO;
import com.pedidos.capasP.ProductoDTO.RespuestaProductoDTO;
import com.pedidos.capasP.models.DetallePedido;
import com.pedidos.capasP.models.Pedido;
import com.pedidos.capasP.repositories.PedidoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
@Transactional
public class PedidoServiceImpl implements PedidoService{
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoService detallePedidoService;
    private final RestTemplate restTemplate;
    @org.springframework.beans.factory.annotation.Value("${productos.url}")
    private String productosUrl;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, DetallePedidoService detallePedidoService, RestTemplate restTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoService = detallePedidoService;
        this.restTemplate = restTemplate;
    }

    public List<Pedido> buscarPorEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new RuntimeException("Debe proporcionar un estado válido");
        }
        return pedidoRepository.findByEstado(estado.toUpperCase());
    }

    @Override
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido buscarPorId(Integer id) {

        if (id == null) {
            throw new RuntimeException("No trae un id de pedido");
        }
        return pedidoRepository.findById(id).orElseThrow(()-> new RuntimeException("No se encontró ningún pedido con el ID: " + id));
    }

    @Override
    public Pedido guardarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido crearPedidoConValidacionStock(Pedido pedido, Integer productoId, Integer cantidadComprada){
        String urlProducto = productosUrl + "/Productos/" + productoId;
        String urlConsumoStock = productosUrl + "/Productos/Consumo/" + productoId;
        try {
            ResponseEntity<RespuestaProductoDTO> respuesta = restTemplate.getForEntity(urlProducto, RespuestaProductoDTO.class);

            if (!respuesta.getStatusCode().is2xxSuccessful() || respuesta.getBody() == null || respuesta.getBody().getProducto() == null) {
                throw new RuntimeException("El producto con ID " + productoId + " no existe o el servicio de productos no respondió.");
            }

            ProductoDTO producto = respuesta.getBody().getProducto();

            if (producto.getStock_producto() < cantidadComprada) {
                throw new RuntimeException("Stock insuficiente. Stock actual en bodega: " + producto.getStock_producto());
            }

            Double precioUnitario = producto.getPrecio_producto();
            Double totalCalculado = precioUnitario * cantidadComprada;
            pedido.setTotal(totalCalculado);
            pedido.setEstado("ACTIVO"); //no me quedo de otra que forzar activo cuando se cree un pedido sino me rompe la logica general del eliminar pedido
            Pedido pedidoGuardado = pedidoRepository.save(pedido);

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedidoGuardado);
            detalle.setProducto_ID(productoId);
            detalle.setCantidad(cantidadComprada);
            detalle.setPrecio_unitario(precioUnitario);
            detallePedidoService.guardarDetalle(detalle);
            restTemplate.put(urlConsumoStock, new ConsumoStock(cantidadComprada));
            return pedidoGuardado;
        } catch (Exception e) {
            String mensajeOriginal = e.getMessage();
            if (mensajeOriginal.contains("El producto está inactivo")) {
                throw new RuntimeException("El producto seleccionado se encuentra inactivo.");
            }
            if (mensajeOriginal.contains("ConstraintViolationImpl") || mensajeOriginal.contains("Validation failed")) {
                throw new RuntimeException("Formato de estado no válido. El estado admitido por defecto es ACTIVO.");
            }
            if (mensajeOriginal.contains("Stock insuficiente")) {
                throw new RuntimeException(mensajeOriginal);
            }
            throw new RuntimeException(mensajeOriginal);        }
    }

    @Override
    public boolean eliminarPedido(Integer id) {
        if (id == null) {
            throw new RuntimeException("No trae un id de pedido");
        }
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();

            if ("CANCELADO".equals(pedido.getEstado())) {
                throw new RuntimeException("El pedido con ID " + id + " ya se encuentra cancelado.");
            }
            pedido.setEstado("CANCELADO");
            pedidoRepository.save(pedido);
            List<DetallePedido> detalles = detallePedidoService.listarDetalles();
            for (DetallePedido det : detalles) {
                if (det.getPedido() != null && det.getPedido().getId_pedido().equals(id)) {
                    String urlDevolver = productosUrl + "/Productos/DevolverStock/" + det.getProducto_ID();
                    Map<String, Integer> bodyDevolver = Map.of("cantidad_en_stock", det.getCantidad());
                    restTemplate.put(urlDevolver, bodyDevolver);
                }
            }
            return true;
        } else {
            throw new RuntimeException("No se encontró ningún pedido con el ID: " + id);
        }
    }
}
