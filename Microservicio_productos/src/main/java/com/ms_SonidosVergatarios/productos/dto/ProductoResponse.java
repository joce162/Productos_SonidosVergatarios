package com.ms_SonidosVergatarios.productos.dto;

import com.ms_SonidosVergatarios.productos.model.Producto;

/** Datos del producto que ve el frontend (sin campos internos como nombreClave o activo). */
public record ProductoResponse(Long id, String codigo, String nombre, String categoria, String marca,
                               String modelo, String descripcion, int precio, int stock, String imagen) {

    public static ProductoResponse desde(Producto p) {
        return new ProductoResponse(p.getId(), p.getCodigo(), p.getNombre(), p.getCategoria(), p.getMarca(),
                p.getModelo(), p.getDescripcion(), p.getPrecio(), p.getStock(), p.getImagen());
    }
}
