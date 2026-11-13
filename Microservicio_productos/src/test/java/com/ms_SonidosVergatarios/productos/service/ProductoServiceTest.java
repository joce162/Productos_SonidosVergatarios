package com.ms_SonidosVergatarios.productos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import com.ms_SonidosVergatarios.productos.dto.DescuentoItem;
import com.ms_SonidosVergatarios.productos.dto.DescuentoRequest;
import com.ms_SonidosVergatarios.productos.dto.DescuentoResponse;
import com.ms_SonidosVergatarios.productos.dto.ProductoRequest;
import com.ms_SonidosVergatarios.productos.exception.ApiException;
import com.ms_SonidosVergatarios.productos.model.DescuentoStock;
import com.ms_SonidosVergatarios.productos.model.Producto;
import com.ms_SonidosVergatarios.productos.repository.DescuentoStockRepository;
import com.ms_SonidosVergatarios.productos.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductoServiceTest {

    private ProductoRepository repo;
    private DescuentoStockRepository descuentos;
    private ProductoService productos;
    private InventarioService inventario;

    @BeforeEach
    void preparar() {
        repo = mock(ProductoRepository.class);
        descuentos = mock(DescuentoStockRepository.class);
        productos = new ProductoService(repo);
        inventario = new InventarioService(repo, descuentos);
    }

    private ProductoRequest request(String nombre) {
        return new ProductoRequest(nombre, null, "Guitarras", "Yamaha", null, null, 100000, 5, "https://x.cl/a.jpg");
    }

    private Producto producto(String nombre, int stock) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setPrecio(1000);
        p.setStock(stock);
        return p;
    }

    @Test
    void normalizaElNombreParaDetectarDuplicados() {
        assertEquals("guitarra acustica", ProductoService.normalizar("  Guitarra   Acústica "));
    }

    @Test
    void crearConNombreDuplicadoDevuelve409() {
        when(repo.existsByNombreClaveAndActivoTrue("guitarra acustica")).thenReturn(true);

        ApiException e = assertThrows(ApiException.class, () -> productos.crear(request("Guitarra Acústica")));

        assertEquals(HttpStatus.CONFLICT, e.getEstado());
        verify(repo, never()).save(any());
    }

    @Test
    void crearGuardaElProductoNormalizado() {
        when(repo.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        var respuesta = productos.crear(request("  Bajo Eléctrico "));

        assertEquals("Bajo Eléctrico", respuesta.nombre());
        assertEquals(5, respuesta.stock());
    }

    private Producto productoConId(long id, int stock) {
        Producto p = mock(Producto.class);
        when(p.getId()).thenReturn(id);
        when(p.isActivo()).thenReturn(true);
        when(p.getStock()).thenReturn(stock);
        when(p.getNombre()).thenReturn("Platillo");
        return p;
    }

    @Test
    void descontarSinStockSuficienteDevuelve409YNoModificaNada() {
        Producto p = productoConId(1L, 1);
        when(repo.bloquearPorIds(anyCollection())).thenReturn(List.of(p));

        DescuentoRequest pedido = new DescuentoRequest("r1", List.of(new DescuentoItem(1L, 5)));
        ApiException e = assertThrows(ApiException.class, () -> inventario.descontar(pedido));

        assertEquals(HttpStatus.CONFLICT, e.getEstado());
        verify(p, never()).setStock(anyInt());
        verify(descuentos, never()).save(any());
    }

    @Test
    void descontarConStockRestaLaCantidadYGuardaElComprobante() {
        Producto p = productoConId(1L, 5);
        when(repo.bloquearPorIds(anyCollection())).thenReturn(List.of(p));
        when(descuentos.save(any(DescuentoStock.class))).thenAnswer(inv -> inv.getArgument(0));

        DescuentoResponse r = inventario.descontar(new DescuentoRequest("r3", List.of(new DescuentoItem(1L, 2))));

        verify(p).setStock(3);
        assertEquals(1, r.lineas().size());
        assertEquals(2, r.lineas().get(0).cantidad());
    }

    @Test
    void descontarReintentoDevuelveElComprobanteSinRestarOtraVez() {
        DescuentoStock previo = new DescuentoStock();
        previo.setReferencia("r2");
        when(descuentos.findByReferencia("r2")).thenReturn(Optional.of(previo));

        DescuentoResponse r = inventario.descontar(new DescuentoRequest("r2", List.of(new DescuentoItem(1L, 1))));

        assertEquals("r2", r.referencia());
        verify(repo, never()).bloquearPorIds(anyCollection());
    }
}
