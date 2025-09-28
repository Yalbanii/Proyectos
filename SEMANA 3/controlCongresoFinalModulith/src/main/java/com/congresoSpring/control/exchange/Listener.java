package com.congresoSpring.control.exchange;

import com.congresoSpring.control.events.AddPointsEvent;
import com.congresoSpring.control.participants.Participant;
import com.congresoSpring.control.participants.ParticipantRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;


public class Listener {

    private final ParticipantRepository participantRepository;
    private final CertificateRepository certificateRecordRepository; // 💡 INYECCIÓN NUEVA

    // Constructor para inyección de dependencias
    public Listener(ParticipantRepository participantRepository,
                               CertificateRepository certificateRecordRepository) {
        this.participantRepository = participantRepository;
        this.certificateRecordRepository = certificateRecordRepository; // 💡 ASIGNACIÓN
    }

    @Transactional
    @EventListener
    public void checkCertificateReached(AddPointsEvent event) {

        Participant participant = participantRepository.findById(event.getParticipantId())
                .orElse(null);

        if (participant == null) return;

        // 1. Acumular los puntos para certificado
        int currentCertificatePoints = participant.getCertificateReachedPoints() + event.getPointsEarned();
        participant.setCertificateReachedPoints(currentCertificatePoints);

        // 2. VERIFICAR la condición de los 25 puntos
        if (currentCertificatePoints >= 25 && !participant.isCertificateReached()) {

            // 3. Modificar el flag en la entidad Participant y guardar
            participant.setCertificateReached(true);
            participantRepository.save(participant);

            // 💡 LÓGICA CLAVE: CREAR Y GUARDAR EL REGISTRO DEL CERTIFICADO
            CertificateRecord record = new CertificateRecord(
                    null, // certificateId (Lo genera @GeneratedValue)
                    participant.getParticipantId(),
                    true, // reached
                    LocalDateTime.now() // localDateTime
            );

            certificateRecordRepository.save(record); // 💡 GUARDAR EL REGISTRO DEL CERTIFICADO

            System.out.println("🎓 Certificate Listener: Participante " + participant.getParticipantId() + " desbloqueó su CERTIFICADO. REGISTRO CREADO.");

        } else if (currentCertificatePoints < 25) {
            // Guardar el incremento de puntos, aunque no haya desbloqueado el certificado
            // 💡 IMPORTANTE: Esto debe suceder SIEMPRE si hay un cambio, sin importar el IF.
            // Si la lógica es solo de puntos, se puede poner el save fuera del IF/ELSE.
            participantRepository.save(participant);
        }
    }


}
