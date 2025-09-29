package com.congresoSpring.control.notification;

import com.congresoSpring.control.events.AddPointsEvent;
import com.congresoSpring.control.events.CertificateReachedEvent;
import com.congresoSpring.control.events.ExchangePointsEvent;
import com.congresoSpring.control.events.SpecialAccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @EventListener
    public void notifyPointEarned(AddPointsEvent event) {
        // Logic to send notification to the user
        System.out.println("✅ El participante " + event.getParticipantId() + " entro a la conferencia " + event.getIdConferencia() +
                " y obtuvo " + event.getPointsEarned() + " a las " + event.getTimestamp());
    }

    @EventListener
    public void notifyExchangePoints(ExchangePointsEvent event) {
        // Logic to send notification to the user
        System.out.println("✅ Participante " + event.getParticipantId()+ ", tu total de puntos despues de este intercambio, es: " + event.getPointsTotal());    }

    @EventListener
    public void notifyCertificate(CertificateReachedEvent event) {
        // Logic to send notification to the user
        System.out.println("✅ El participante " + event.getParticipantId() +
                " ha alcanzado 25 puntos y desbloqueó su 🎓 certificado a las " + event.getLocalDateTime() + ". ¡FELICIDADES! 📩");
    }

    @EventListener
    public void notifySpecialAccess(SpecialAccessEvent event) {
        // Logic to send notification to the user
        System.out.println("✅ El participante " + event.getParticipantId() +
                " ha alcanzado 30 puntos y desbloqueó su acceso VIP a los Eventos Especiales. ¡FELICIDADES! 📩");
    }
}
