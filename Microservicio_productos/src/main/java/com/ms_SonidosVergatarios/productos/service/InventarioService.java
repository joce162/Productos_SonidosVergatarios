package com.ms_SonidosVergatarios.productos.service;

import com.ms_SonidosVergatarios.productos.dto.DescuentoItem;
import com.ms_SonidosVergatarios.productos.dto.DescuentoLineaResponse;
import com.ms_SonidosVergatarios.productos.dto.DescuentoRequest;
import com.ms_SonidosVergatarios.productos.dto.DescuentoResponse;
import com.ms_SonidosVergatarios.productos.exception.ApiException;
import com.ms_SonidosVergatarios.productos.model.DescuentoLinea;
import com.ms_SonidosVergatarios.productos.model.DescuentoStock;
import com.ms_SonidosVergatarios.productos.model.EstadoDescuento;
import com.ms_SonidosVergatarios.productos.model.Producto;
import com.ms_SonidosVergatarios.productos.repository.DescuentoStockRepository;
import com.ms_SonidosVergatarios.productos.repository.ProductoRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Operaciones de inventario que usa el microservicio de carrito al confirmar una compra.
 * Todo ocurre en una sola transacción local: o se descuenta todo o no se descuenta nada.
 */
@Service
public class InventarioService {

    private final ProductoRepository productos;
    private final DescuentoStockRepository descuentos;

    public InventarioService(ProductoRepository productos, DescuentoStockRepository descuentos) {
        this.productos = productos;
        this.descuentos = descuentos;
    }

    @Transactional
    public DescuentoResponse descontar(DescuentoRequest r) {
        Optional<DescuentoStock> previo = descuentos.findByReferencia(r.referencia());
        if (previo.isPresent()) {
            if (previo.get().getEstado() == EstadoDescuento.REVERTIDO) {
                throw new ApiException(HttpStatus.CONFLICT, "Esa operación ya fue revertida.");
            }
            return responder(previo.get()); // reintento: devuelve el comprobante sin descontar otra vez
        }

        // Une líneas repetidas y ordena por id (el mismo orden de bloqueo evita interbloqueos).
        Map<Long, Integer> pedido = new TreeMap<>();
        for (DescuentoItem item : r.items()) {
            pedido.merge(item.productoId(), item.cantidad(), Integer::sum);
        }
        Map<Long, Producto> porId = productos.bloquearPorIds(pedido.keySet()).stream()
                .collect(Collectors.toMap(Producto::getId, Function.identity()));

        // Primero se valida todo; recién después se modifica el stock.
        for (Map.Entry<Long, Integer> e : pedido.entrySet()) {
            Producto p = porId.get(e.getKey());
            if (p == null || !p.isActivo()) {
                throw new ApiException(HttpStatus.CONFLICT,
                        "Un producto del carrito ya no está disponible (id " + e.getKey() + ").");
            }
            if (p.getStock() < e.getValue()) {
                throw new ApiException(HttpStatus.CONFLICT,
                        "Stock insuficiente de " + p.getNombre() + " (disponible: " + p.getStock() + ").");
            }
        }

        DescuentoStock d = new DescuentoStock();
        d.setReferencia(r.referencia());
        for (Map.Entry<Long, Integer> e : pedido.entrySet()) {
            Producto p = porId.get(e.getKey());
            p.setStock(p.getStock() - e.getValue());
            d.getLineas().add(new DescuentoLinea(p.getId(), p.getNombre(), p.getPrecio(), e.getValue()));
        }
        return responder(descuentos.save(d));
    }

    /** Compensación: devuelve el stock de una operación aplicada. Repetirla no tiene efecto. */
    @Transactional
    public void reponer(String referencia) {
        DescuentoStock d = descuentos.findByReferencia(referencia).orElse(null);
        if (d == null || d.getEstado() == EstadoDescuento.REVERTIDO) {
            return;
        }
        List<Long> ids = d.getLineas().stream().map(DescuentoLinea::getProductoId).sorted().toList();
        Map<Long, Producto> porId = productos.bloquearPorIds(ids).stream()
                .collect(Collectors.toMap(Producto::getId, Function.identity()));
        for (DescuentoLinea l : d.getLineas()) {
            Producto p = porId.get(l.getProductoId());
            if (p != null) {
                p.setStock(p.getStock() + l.getCantidad());
            }
        }
        d.setEstado(EstadoDescuento.REVERTIDO);
    }

    private DescuentoResponse responder(DescuentoStock d) {
        List<DescuentoLineaResponse> lineas = d.getLineas().stream()
                .map(l -> new DescuentoLineaResponse(l.getProductoId(), l.getNombre(), l.getPrecioUnitario(), l.getCantidad()))
                .toList();
        long subtotal = lineas.stream().mapToLong(l -> (long) l.precioUnitario() * l.cantidad()).sum();
        return new DescuentoResponse(d.getReferencia(), lineas, subtotal);
    }
}
