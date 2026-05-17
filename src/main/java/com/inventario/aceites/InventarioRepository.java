package com.inventario.aceites;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventarioRepository
        extends JpaRepository<Inventario, Integer> {

    // ULTIMO INVENTARIO DEL PRODUCTO
    Optional<Inventario> findTopByProducto_IdOrderByFechaDesc(
            Integer productoId
    );

    // ULTIMO INVENTARIO SIN STOCK FISICO
    Optional<Inventario> findTopByProducto_IdAndStockFisicoIsNullOrderByFechaDesc(
            Integer productoId
    );

    // BUSCAR POR FECHA
    List<Inventario> findByFecha(
            LocalDate fecha
    );
}