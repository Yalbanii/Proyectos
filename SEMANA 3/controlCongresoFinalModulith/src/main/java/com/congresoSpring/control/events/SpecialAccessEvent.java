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
public final class SpecialAccessEvent {
    private Long participantId;
    private Long specialEventId;
    private LocalDateTime localDateTime;

    private final ParticipantRepository participantRepository;

    //LISTENER
    public SpecialAccessEvent(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @EventListener
    public void grantVipAccess(AddPointsEvent event) {

        Participant participant = participantRepository.findById(event.getParticipantId())
                .orElse(null); // Usamos null o una excepción (preferiblemente)

        if (participant == null) return;

        // 1. Acumular los puntos especiales (si aplica, p.ej. si el congreso lo requiere)
        // 🚨 IMPORTANTE: Necesitas una propiedad que guarde los puntos especiales.
        // Asumiremos que los puntos ganados SIEMPRE suman a los puntos especiales.
        int currentSpecialPoints = participant.getSpecialEventAccessPoints() + event.getPointsEarned();
        participant.setSpecialEventAccessPoints(currentSpecialPoints);

        // 2. VERIFICAR la condición de los 30 puntos
        if (currentSpecialPoints >= 30 && !participant.isSpecialEventAccess()) {

            // 3. Modificar y guardar SOLO si la condición se cumple por primera vez.
            participant.setSpecialEventAccess(true);
            participantRepository.save(participant);

            System.out.println("👑 VIP Listener: Participante " + participant.getParticipantId() + " desbloqueó ACCESO VIP. Total Puntos Especiales: " + currentSpecialPoints);

        } else if (currentSpecialPoints < 30) {
            // Guardar el incremento de puntos especiales, aunque no haya desbloqueado el VIP
            participantRepository.save(participant);
        }
        // Si ya tiene el acceso (!isSpecialEventAccess es false), no se hace nada.
    }
}

