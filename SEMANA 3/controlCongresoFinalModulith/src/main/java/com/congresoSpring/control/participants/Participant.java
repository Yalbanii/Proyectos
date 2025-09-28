package com.congresoSpring.control.participants;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "participantes")
public class Participant {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long participantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String nacionality;

    @Column(nullable = false)
    private Integer totalPoints;// Este va a variar

    @Column(nullable = false)
    private Integer age;

    @Column
    private String area;

    @Column
    private boolean certificateReached = false;

    @Column
    private Integer certificateReachedPoints;

    @Column
    private boolean specialEventAccess = false;

    @Column
    private Integer specialEventAccessPoints;

    public Participant(String name, String lastName, String nacionality, Integer totalPoints, Integer age, String area, Integer certificateReachedPoints, Integer specialEventAccessPoints) {
    }

    public void usarPuntos(int puntos) {
        if (puntos <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos debe ser positiva");
        }
        if (totalPoints < puntos) {
            throw new IllegalStateException("Puntos insuficientes. Disponible: " + totalPoints + ", Solicitado: " + puntos);
        }
        this.totalPoints -= puntos;
    }

    public void acumularPuntos(Integer puntos) {
        if (puntos <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos debe ser positiva");
        }
        this.totalPoints += puntos;
    }

    public boolean certificateReached() {
        if (totalPoints >= certificateReachedPoints){
            System.out.println("Desbloqueaste Logro: Certificado.");
        }
            return certificateReached = true;
    }
        public boolean specialEventAccess() {
            if (totalPoints >= specialEventAccessPoints){
                System.out.println("Desbloqueaste Logro: Acceso a Eventos Especiales.");
            }
                return specialEventAccess = true;
        }

    }
