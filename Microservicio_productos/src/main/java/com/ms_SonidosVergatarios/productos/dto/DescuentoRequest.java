package com.ms_SonidosVergatarios.productos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record DescuentoRequest(
        @NotBlank(message = "Falta la referencia.") @Size(max = 64) String referencia,
        @Valid @NotEmpty(message = "Indica al menos un producto.") List<DescuentoItem> items) {
}
