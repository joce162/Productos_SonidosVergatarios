package com.ms_SonidosVergatarios.productos.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Comprobante de una operación de inventario identificada por una referencia única.
 * Hace idempotente el descuento: repetir la misma referencia no vuelve a restar stock.
 */
@Entity
@Table(name = "descuentos_stock")
public class DescuentoStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String referencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDescuento estado = EstadoDescuento.APLICADO;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn = Instant.now();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "descuento_lineas", joinColumns = @JoinColumn(name = "descuento_id"))
    private List<DescuentoLinea> lineas = new ArrayList<>();

    public Long getId() { return id; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public EstadoDescuento getEstado() { return estado; }
    public void setEstado(EstadoDescuento estado) { this.estado = estado; }
    public Instant getCreadoEn() { return creadoEn; }
    public List<DescuentoLinea> getLineas() { return lineas; }
}
