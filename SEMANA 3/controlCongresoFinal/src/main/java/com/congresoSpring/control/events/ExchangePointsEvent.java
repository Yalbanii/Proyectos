package com.congresoSpring.control.events;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

//Intercambio de puntos por freebies
//Derecho a pedir constancia al finalizar
//Acceso a eventos especiales

@AllArgsConstructor
@Data
public final class ExchangePointsEvent {
    private final Long participantId;
    private Long pointsTotal;
    private Boolean specialEventsAccess;
    private Boolean certificateReached;
    private LocalDateTime entryTime;

}
