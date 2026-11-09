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
import com.pedidos.capasP.ProductoDTO.ItemPedidoDTO;
import java.util.ArrayList;


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
    public Pedido crearPedidoConValidacionStock(Pedido pedido, List<ItemPedidoDTO> items){
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("El pedido debe contener al menos un producto.");
        }
        if (pedido == null || pedido.getUsuario() == null || pedido.getUsuario().isBlank()) {
            throw new RuntimeException("Debe indicar el usuario del pedido.");
        }
        try {
            double totalGeneral = 0.0;
            List<ProductoDTO> productos = new ArrayList<>();

            for (int i = 0; i < items.size(); i++) {
                ItemPedidoDTO item = items.get(i);

                if (item.getProductoId() == null || item.getCantidad() == null || item.getCantidad() <= 0) {
                    throw new RuntimeException("Cada ítem debe tener un producto válido y cantidad mayor a 0.");
                }
                for (int j = 0; j < i; j++) {
                    if (items.get(j).getProductoId().equals(item.getProductoId())) {
                        throw new RuntimeException("El producto " + item.getProductoId() + " está repetido en el pedido.");
                    }
                }

                String urlProducto = productosUrl + "/Productos/" + item.getProductoId();
                ResponseEntity<RespuestaProductoDTO> respuesta = restTemplate.getForEntity(urlProducto, RespuestaProductoDTO.class);

                if (!respuesta.getStatusCode().is2xxSuccessful() || respuesta.getBody() == null || respuesta.getBody().getProducto() == null) {
                    throw new RuntimeException("El producto con ID " + item.getProductoId() + " no existe o el servicio de productos no respondió.");
                }

                ProductoDTO producto = respuesta.getBody().getProducto();

                if (producto.getStock_producto() < item.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre_producto() +
                            " (Stock actual: " + producto.getStock_producto() + ")");
                }

                productos.add(producto);
                totalGeneral += producto.getPrecio_producto() * item.getCantidad();
            }

            pedido.setTotal(totalGeneral);
            pedido.setEstado("ACTIVO");
            Pedido pedidoGuardado = pedidoRepository.save(pedido);

            List<DetallePedido> detallesCreados = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                ItemPedidoDTO item = items.get(i);

                DetallePedido detalle = new DetallePedido();
                detalle.setPedido(pedidoGuardado);
                detalle.setProducto_ID(item.getProductoId());
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecio_unitario(productos.get(i).getPrecio_producto());
                detallesCreados.add(detallePedidoService.guardarDetalle(detalle));

                String urlConsumoStock = productosUrl + "/Productos/Consumo/" + item.getProductoId();
                restTemplate.put(urlConsumoStock, new ConsumoStock(item.getCantidad()));
            }
            pedidoGuardado.setDetalles(detallesCreados);
            return pedidoGuardado;

        } catch (Exception e) {
            String mensajeOriginal = e.getMessage();
            if (mensajeOriginal == null) {
                mensajeOriginal = "Error al procesar el pedido.";
            }
            if (mensajeOriginal.contains("El producto está inactivo")) {
                throw new RuntimeException("Uno de los productos seleccionados se encuentra inactivo.");
            }
            if (mensajeOriginal.startsWith("404")) {
                throw new RuntimeException("Uno de los productos del pedido no existe.");
            }
            throw new RuntimeException(mensajeOriginal);
        }
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
            if (pedido.getDetalles() != null) {
                for (DetallePedido det : pedido.getDetalles()) {
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
