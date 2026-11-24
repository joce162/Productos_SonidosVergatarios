package com.ms_SonidosVergatarios.productos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DescuentoItem(
        @NotNull(message = "Falta el producto.") Long productoId,
        @NotNull(message = "Falta la cantidad.") @Positive(message = "La cantidad debe ser mayor que cero.") Integer cantidad) {
}
