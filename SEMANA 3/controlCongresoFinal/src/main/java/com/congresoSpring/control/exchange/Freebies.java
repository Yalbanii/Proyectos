package com.congresoSpring.control.exchange;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Freebies {
    @Id
    @Column(name = "ID_FREEBIE")
    private Long freebieId;

    @Column(name = "ARTICULO")
    private String articulo;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "STOCK_INICIAL")
    private Integer stockInicial;

    @Column(name = "COSTO")
    private Integer costo;

    @Column(name = "ACTUAL")
    private Integer stockActual;

}
