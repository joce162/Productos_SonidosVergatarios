package com.ms_SonidosVergatarios.productos.exception;

import org.springframework.http.HttpStatus;

/** Error de negocio conocido: se convierte en una respuesta HTTP con mensaje JSON. */
public class ApiException extends RuntimeException {

    private final HttpStatus estado;
    private final String campo;

    public ApiException(HttpStatus estado, String mensaje) {
        this(estado, mensaje, null);
    }

    /** @param campo campo del formulario al que se asocia el error (opcional). */
    public ApiException(HttpStatus estado, String mensaje, String campo) {
        super(mensaje);
        this.estado = estado;
        this.campo = campo;
    }

    public HttpStatus getEstado() { return estado; }
    public String getCampo() { return campo; }
}
