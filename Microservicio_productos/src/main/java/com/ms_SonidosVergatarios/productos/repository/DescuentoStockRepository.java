package com.ms_SonidosVergatarios.productos.repository;

import com.ms_SonidosVergatarios.productos.model.DescuentoStock;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescuentoStockRepository extends JpaRepository<DescuentoStock, Long> {

    Optional<DescuentoStock> findByReferencia(String referencia);
}
