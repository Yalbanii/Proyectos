package com.congresoSpring.control.exchange;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "EVENTOS_ESPECIALES")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specialEventId;

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

}
