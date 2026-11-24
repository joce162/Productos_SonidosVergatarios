package com.ms_SonidosVergatarios.productos.service;

import com.ms_SonidosVergatarios.productos.dto.ProductoRequest;
import com.ms_SonidosVergatarios.productos.dto.ProductoResponse;
import com.ms_SonidosVergatarios.productos.exception.ApiException;
import com.ms_SonidosVergatarios.productos.model.Producto;
import com.ms_SonidosVergatarios.productos.repository.ProductoRepository;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Reglas de negocio del catálogo: nombres únicos, bajas lógicas y datos normalizados. */
@Service
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return repo.findByActivoTrueOrderByIdAsc().stream().map(ProductoResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtener(Long id) {
        return ProductoResponse.desde(buscar(id));
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest r) {
        String clave = normalizar(r.nombre());
        if (repo.existsByNombreClaveAndActivoTrue(clave)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un producto con ese nombre.", "nombre");
        }
        String codigo = vacioANulo(r.codigo());
        if (codigo != null && repo.existsByCodigo(codigo)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un producto con ese código.", "codigo");
        }
        Producto p = new Producto();
        copiar(p, r, clave, codigo);
        return ProductoResponse.desde(repo.save(p));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest r) {
        Producto p = buscar(id);
        String clave = normalizar(r.nombre());
        if (repo.existsByNombreClaveAndActivoTrueAndIdNot(clave, id)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un producto con ese nombre.", "nombre");
        }
        String codigo = vacioANulo(r.codigo());
        if (codigo != null && repo.existsByCodigoAndIdNot(codigo, id)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un producto con ese código.", "codigo");
        }
        copiar(p, r, clave, codigo);
        return ProductoResponse.desde(repo.save(p));
    }

    /** Baja lógica. */
    @Transactional
    public void eliminar(Long id) {
        Producto p = buscar(id);
        p.setActivo(false);
        repo.save(p);
    }

    private Producto buscar(Long id) {
        return repo.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "El producto no existe."));
    }

    /** Copia solo los campos permitidos: el cliente no puede tocar id, nombreClave ni activo. */
    private void copiar(Producto p, ProductoRequest r, String clave, String codigo) {
        p.setNombre(r.nombre().trim());
        p.setNombreClave(clave);
        p.setCodigo(codigo);
        p.setCategoria(r.categoria().trim());
        p.setMarca(vacioANulo(r.marca()));
        p.setModelo(vacioANulo(r.modelo()));
        p.setDescripcion(vacioANulo(r.descripcion()));
        p.setPrecio(r.precio());
        p.setStock(r.stock());
        p.setImagen(r.imagen().trim());
    }

    static String normalizar(String texto) {
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        return sinTildes.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    private static String vacioANulo(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}
