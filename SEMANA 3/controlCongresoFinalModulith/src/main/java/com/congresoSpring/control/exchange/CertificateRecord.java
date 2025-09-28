package com.congresoSpring.control.exchange;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CERTIFICADOS")

public class CertificateRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    @Column(name = "ID_PARTICIPANTE")
    private Long participantId;

    @Column(name = "REACHED")
    private boolean reached;

    private LocalDateTime localDateTime;
}
