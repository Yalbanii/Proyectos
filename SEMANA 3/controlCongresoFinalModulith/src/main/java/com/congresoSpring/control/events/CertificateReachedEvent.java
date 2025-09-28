package com.congresoSpring.control.events;

import com.congresoSpring.control.participants.Participant;
import com.congresoSpring.control.participants.ParticipantRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

//Se alcanco el puntaje para obtener certificado
@AllArgsConstructor
@Data
public final class CertificateReachedEvent {
    private Long participantId;
    private Long certificateId;
    private LocalDateTime localDateTime;

    private final ParticipantRepository participantRepository;

    public CertificateReachedEvent(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

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

            // 3. Modificar y guardar SOLO si la condición se cumple por primera vez.
            participant.setCertificateReached(true);
            participantRepository.save(participant);

            System.out.println("🎓 Certificate Listener: Participante " + participant.getParticipantId() + " desbloqueó su CERTIFICADO. Total Puntos Certificado: " + currentCertificatePoints);

        } else if (currentCertificatePoints < 25) {
            // Guardar el incremento de puntos, aunque no haya desbloqueado el certificado
            participantRepository.save(participant);
        }
    }
}

