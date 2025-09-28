package com.congresoSpring.control.participants;

import com.congresoSpring.control.events.AddPointsEvent;
import com.congresoSpring.control.events.CertificateReachedEvent;
import com.congresoSpring.control.events.SpecialAccessEvent;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PointsParticipantService {

    private final ParticipantRepository participantRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PointsParticipantService(ParticipantRepository participantRepository, ApplicationEventPublisher eventPublisher){
        this.participantRepository = participantRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Participant crearParticipante(String name,String lastName,String nacionality, Integer totalPoints,Integer  age,String  area, Integer  certificateReachedPoints,Integer specialEventAccessPoints) {
        Participant participant = new Participant(name,lastName,nacionality, totalPoints, age, area, certificateReachedPoints, specialEventAccessPoints);
        return participantRepository.save(participant);
    }

    @Transactional
    public void acumularPuntos(Long participantId, Integer puntos) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participante no encontrado: " + participantId));

        participant.acumularPuntos(Math.toIntExact(puntos));
        participantRepository.save(participant);
        System.out.println("Participante " + participant.getParticipantId()+ ", tu total de puntos al dia de hoy, es: " + participant.getTotalPoints());

//        eventPublisher.publishEvent(new (participant.getParticipantId(), participant.getName(), participant.getTotalPoints()));
    }

    @Transactional
    public void usarPuntos(Long participantId, int puntos) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participante no encontrado: " + participantId));

        participant.usarPuntos(puntos);
        participantRepository.save(participant); //update
        System.out.println("Participante " + participant.getParticipantId()+ ", tu total de puntos despues de este intercambio, es: " + participant.getTotalPoints());

    }

    public Participant getParticipantById(Long participantId) {
        return participantRepository.findById(participantId).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + participantId));
    }

    public List<Participant> obtenerParticipantesConCertificado() {
        return participantRepository.findAll().stream()
                .filter(Participant::certificateReached)
                .toList();
    }


    public List<Participant> obtenerParticipantesConAccesoEspecial() {
        return participantRepository.findAll().stream()
                .filter(Participant::specialEventAccess)
                .toList();
    }



    @EventListener
    public void addPointsEvent(AddPointsEvent event) {
        Participant participant = participantRepository.findById(event.getParticipantId())
                .orElseThrow(() -> new RuntimeException("Participante no encontrado con ID: " + event.getParticipantId()));
        participant.acumularPuntos(event.getPointsEarned());
        participantRepository.save(participant);
        System.out.println("✅ El participante " + event.getParticipantId() + " entro a la conferencia " + event.getIdConferencia() +
                " y obtuvo " + event.getPointsEarned() + " a las " + event.getTimestamp());
    }


    @EventListener
    public void certificateReachedEvent(CertificateReachedEvent event) {

        Participant participant = participantRepository.findById(event.getParticipantId())
                .orElseThrow(() -> new RuntimeException("Participante no encontrado con ID: " + event.getParticipantId()));
        if (participant.getCertificateReachedPoints() >= 25 && !participant.isCertificateReached()) {
            participant.setCertificateReached(true);
            participantRepository.save(participant);
            //UPDATE
            System.out.println("✅ El participante " + event.getParticipantId() +
                    " ha alcanzado " + participant.getCertificateReachedPoints() + " puntos y desbloqueó su certificado a las " + event.getLocalDateTime() + ". ¡FELICIDADES!");
        }
    }
    @EventListener
    public void specialAccessEvent(SpecialAccessEvent event) {
        Participant participant = participantRepository.findById(event.getParticipantId())
                .orElseThrow(() -> new RuntimeException("Participante no encontrado con ID: " + event.getParticipantId()));

        if (participant.getSpecialEventAccessPoints() >= 30 && !participant.isSpecialEventAccess()) {
            participant.setSpecialEventAccess(true);
        participantRepository.save(participant);
        System.out.println("✅ El participante " + event.getParticipantId() + " desbloqueó su acceso VIP a los Eventos Especiales a las " + event.getLocalDateTime() + ". ¡FELICIDADES!");
            System.out.println("✅ El participante " + event.getParticipantId() +
                    " ha alcanzado " + participant.getSpecialEventAccessPoints() + " puntos y desbloqueó su acceso VIP a los Eventos Especiales. ¡FELICIDADES!");
        }
    }

}
