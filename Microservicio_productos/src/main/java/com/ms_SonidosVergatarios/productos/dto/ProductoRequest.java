package com.ms_SonidosVergatarios.productos.dto;

import jakarta.validation.constraints.*;

public record ProductoRequest(
        @NotBlank(message = "Ingresa el nombre.")
        @Size(min = 3, max = 120, message = "El nombre debe tener entre 3 y 120 caracteres.")
        String nombre,

        @Size(max = 30, message = "El código admite hasta 30 caracteres.")
        String codigo,

        @NotBlank(message = "Ingresa la categoría.")
        @Size(min = 3, max = 60, message = "La categoría debe tener entre 3 y 60 caracteres.")
        String categoria,

        @Size(max = 60, message = "La marca admite hasta 60 caracteres.") String marca,
        @Size(max = 60, message = "El modelo admite hasta 60 caracteres.") String modelo,
        @Size(max = 500, message = "La descripción admite hasta 500 caracteres.") String descripcion,

        @NotNull(message = "Ingresa el precio.")
        @Positive(message = "El precio debe ser mayor que cero.")
        Integer precio,

        @NotNull(message = "Ingresa el stock.")
        @PositiveOrZero(message = "El stock debe ser un entero mayor o igual a cero.")
        Integer stock,

        @NotBlank(message = "Ingresa la URL de la imagen.")
        @Size(max = 1000, message = "La URL es demasiado larga.")
        @Pattern(regexp = "^https?://\\S+$", message = "Ingresa una URL de imagen http o https.")
        String imagen) {
}
