package com.ms_SonidosVergatarios.productos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** Línea de un descuento de stock: guarda nombre y precio al momento de la compra. */
@Embeddable
public class DescuentoLinea {

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(name = "precio_unitario", nullable = false)
    private int precioUnitario;

    @Column(nullable = false)
    private int cantidad;

    protected DescuentoLinea() { }

    public DescuentoLinea(Long productoId, String nombre, int precioUnitario, int cantidad) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public Long getProductoId() { return productoId; }
    public String getNombre() { return nombre; }
    public int getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }
}
