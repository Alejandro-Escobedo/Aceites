package com.inventario.aceites;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String codigo;
    private String descripcion;
    private String grado;

    //  ESTE ES SOLO PARA MOSTRAR EN PANTALLA (NO BD)
    @Transient
    private Double stockSistema;

    // getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getGrado() { return grado; }
    public void setGrado(String grado) { this.grado = grado; }

    public Double getStockSistema() { return stockSistema; }
    public void setStockSistema(Double stockSistema) { this.stockSistema = stockSistema; }
}