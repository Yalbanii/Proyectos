package com.congresoSpring.control.exchange;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "CONFERENCIAS")
public class Conferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conferenciaId;

    @Column(name = "DIA")
    private Integer dia;

    @Column(name = "FECHA")
    private Long fecha;

    @Column(name = "HORA_INICIO")
    private Long horaInicio;

    @Column(name = "HORA_FIN")
    private Long horaFin;

    @Column(name = "DURACION")
    private Long duracion;

    @Column(name = "SEDE")
    private String sede;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "TITULO")
    private String titulo;

    @Column(name = "PONENTE")
    private String ponente;

    @Column(name = "PUNTOS")
    private Integer puntos;
}

