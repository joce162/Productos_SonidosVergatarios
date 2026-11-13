package com.ms_SonidosVergatarios.productos.controller;

import com.ms_SonidosVergatarios.productos.dto.ProductoRequest;
import com.ms_SonidosVergatarios.productos.dto.ProductoResponse;
import com.ms_SonidosVergatarios.productos.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** GET es público; POST, PUT y DELETE exigen rol ADMIN (regla definida en SecurityConfig). */
@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos")
public class ProductosController {

    private final ProductoService servicio;

    public ProductosController(ProductoService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Lista el catálogo (público)")
    @GetMapping
    public List<ProductoResponse> listar() {
        return servicio.listar();
    }

    @Operation(summary = "Obtiene un producto por id (público)")
    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @Operation(summary = "Crea un producto (ADMIN)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponse crear(@Valid @RequestBody ProductoRequest r) {
        return servicio.crear(r);
    }

    @Operation(summary = "Modifica un producto (ADMIN)")
    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest r) {
        return servicio.actualizar(id, r);
    }

    @Operation(summary = "Elimina un producto del catálogo (ADMIN, baja lógica)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}
