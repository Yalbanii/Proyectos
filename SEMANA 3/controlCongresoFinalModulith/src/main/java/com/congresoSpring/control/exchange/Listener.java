package com.congresoSpring.control.exchange;

import com.congresoSpring.control.events.AddPointsEvent;
import com.congresoSpring.control.participants.Participant;
import com.congresoSpring.control.participants.ParticipantRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;


public class Listener {

    private final ParticipantRepository participantRepository;
    private final CertificateRepository certificateRecordRepository; // INYECCION NUEVA

    // Constructor para inyección de dependencias
    public Listener(ParticipantRepository participantRepository,
                               CertificateRepository certificateRecordRepository) {
        this.participantRepository = participantRepository;
        this.certificateRecordRepository = certificateRecordRepository; // ASIGNACION
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

            // 3. Modificar el estado en la entidad Participant y guardar
            participant.setCertificateReached(true);
            participantRepository.save(participant);

            // CREAR Y GUARDAR EL REGISTRO DEL CERTIFICADO, PARA LA BDD
            CertificateRecord record = new CertificateRecord(
                    null, // certificateId se genera autom.
                    participant.getParticipantId(),
                    true, // reached
                    LocalDateTime.now()
            );

            certificateRecordRepository.save(record); //GUARDAR EL REGISTRO DEL CERTIFICADO

            System.out.println("🎓 El Participante " + participant.getParticipantId() + " desbloqueó su CERTIFICADO. REGISTRO CREADO.");

        } else if (currentCertificatePoints < 25) {
            // Guardar el incremento de puntos, aunque no haya desbloqueado el certificado
            // Esto debe suceder SIEMPRE si hay un cambio, sin importar el IF.
            participantRepository.save(participant);
        }
    }


}
