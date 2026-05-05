package com.inventario.aceites;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(name = "stock_sistema")
    private Double stockSistema;

    @Column(name = "stock_fisico")
    private Double stockFisico;

    private Double diferencia;
    private String estado;
    private LocalDate fecha;

    // getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Double getStockSistema() { return stockSistema; }
    public void setStockSistema(Double stockSistema) { this.stockSistema = stockSistema; }

    public Double getStockFisico() { return stockFisico; }
    public void setStockFisico(Double stockFisico) { this.stockFisico = stockFisico; }

    public Double getDiferencia() { return diferencia; }
    public void setDiferencia(Double diferencia) { this.diferencia = diferencia; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}