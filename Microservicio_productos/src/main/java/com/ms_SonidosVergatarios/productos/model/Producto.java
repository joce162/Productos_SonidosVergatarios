package com.ms_SonidosVergatarios.productos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "productos", indexes = @Index(name = "idx_producto_nombre_clave", columnList = "nombre_clave"))
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 30)
    private String codigo;

    @Column(nullable = false, length = 120)
    private String nombre;

    /** Nombre normalizado (sin tildes, minúsculas) para detectar duplicados. Campo interno: no se expone. */
    @Column(name = "nombre_clave", nullable = false, length = 120)
    private String nombreClave;

    @Column(nullable = false, length = 60)
    private String categoria;

    @Column(length = 60)
    private String marca;

    @Column(length = 60)
    private String modelo;

    @Column(length = 500)
    private String descripcion;

    /** Precio en pesos chilenos (entero). */
    @Column(nullable = false)
    private int precio;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, length = 1000)
    private String imagen;

    /** Baja lógica: un producto eliminado deja de listarse pero conserva su historial. */
    @Column(nullable = false)
    private boolean activo = true;

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNombreClave() { return nombreClave; }
    public void setNombreClave(String nombreClave) { this.nombreClave = nombreClave; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
