package com.congresoSpring.control.events;

import org.springframework.context.ApplicationEvent;

//Acumulacion de puntos

public final class AddPointsEvent extends ApplicationEvent {
    private Long participantId;
    private Long idConferencia;
    private Integer pointsEarned;

    public AddPointsEvent(Object source, Long participantId, Long idConferencia, Integer pointsEarned) {
        super(source);
        this.participantId = participantId;
        this.idConferencia = idConferencia;
        this.pointsEarned = pointsEarned;
    }


    public Long getParticipantId() {
        return participantId;
    }

    public Long getIdConferencia() {
        return idConferencia;
    }

    public Integer getPointsEarned() {
        return pointsEarned;
    }
}


