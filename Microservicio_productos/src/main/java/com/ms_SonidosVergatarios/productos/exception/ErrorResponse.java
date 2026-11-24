package com.ms_SonidosVergatarios.productos.exception;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(int status, String error, String mensaje, Map<String, String> errores, Instant timestamp) {
}
