package com.ms_SonidosVergatarios.productos.controller;

import com.ms_SonidosVergatarios.productos.dto.DescuentoRequest;
import com.ms_SonidosVergatarios.productos.dto.DescuentoResponse;
import com.ms_SonidosVergatarios.productos.service.InventarioService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints internos para el microservicio de carrito. No se publican en el Gateway
 * y exigen la cabecera X-Internal-Key (ver ClaveInternaFilter).
 */
@Hidden
@RestController
@RequestMapping("/internal/stock")
public class InventarioInternoController {

    private final InventarioService inventario;

    public InventarioInternoController(InventarioService inventario) {
        this.inventario = inventario;
    }

    @PostMapping("/descontar")
    public DescuentoResponse descontar(@Valid @RequestBody DescuentoRequest r) {
        return inventario.descontar(r);
    }

    @PostMapping("/reponer/{referencia}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reponer(@PathVariable String referencia) {
        inventario.reponer(referencia);
    }
}
