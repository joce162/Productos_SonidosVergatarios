package com.ms_SonidosVergatarios.productos.repository;

import com.ms_SonidosVergatarios.productos.model.Producto;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrueOrderByIdAsc();

    Optional<Producto> findByIdAndActivoTrue(Long id);

    boolean existsByNombreClaveAndActivoTrue(String nombreClave);

    boolean existsByNombreClaveAndActivoTrueAndIdNot(String nombreClave, Long id);

    boolean existsByCodigoAndIdNot(String codigo, Long id);

    boolean existsByCodigo(String codigo);

    /** Bloquea las filas en orden de id para que dos compras simultáneas no se crucen. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Producto p where p.id in :ids order by p.id")
    List<Producto> bloquearPorIds(@Param("ids") Collection<Long> ids);
}
