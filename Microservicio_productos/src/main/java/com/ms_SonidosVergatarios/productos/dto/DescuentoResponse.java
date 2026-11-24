package com.ms_SonidosVergatarios.productos.dto;

import java.util.List;

public record DescuentoResponse(String referencia, List<DescuentoLineaResponse> lineas, long subtotal) {
}
