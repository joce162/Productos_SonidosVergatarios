package com.ms_SonidosVergatarios.productos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/** Autentica las llamadas entre microservicios a /internal/** mediante la cabecera X-Internal-Key. */
public class ClaveInternaFilter extends OncePerRequestFilter {

    private final byte[] clave;

    public ClaveInternaFilter(String clave) {
        this.clave = clave.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/internal/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String enviada = req.getHeader("X-Internal-Key");
        if (enviada != null && MessageDigest.isEqual(enviada.getBytes(StandardCharsets.UTF_8), clave)) {
            SecurityContext contexto = SecurityContextHolder.createEmptyContext();
            contexto.setAuthentication(new UsernamePasswordAuthenticationToken(
                    "servicio-interno", null, List.of(new SimpleGrantedAuthority("ROLE_SERVICIO"))));
            SecurityContextHolder.setContext(contexto);
            chain.doFilter(req, res);
            return;
        }
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"status\":403,\"mensaje\":\"Clave de servicio inválida.\"}");
    }
}
