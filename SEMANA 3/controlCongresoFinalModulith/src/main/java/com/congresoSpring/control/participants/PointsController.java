package com.congresoSpring.control.participants;

import com.congresoSpring.control.events.AddPointsEvent;
import com.congresoSpring.control.exchange.CsvReaderService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class PointsController {

    private final PointsParticipantService pointsParticipantService;
    private final ApplicationEventPublisher eventPublisher;
    private final CsvReaderService assistanceService;

    public PointsController(PointsParticipantService pointsParticipantService, ApplicationEventPublisher eventPublisher, CsvReaderService assistanceService) {
        this.pointsParticipantService = pointsParticipantService;
        this.eventPublisher = eventPublisher;
        this.assistanceService = assistanceService;
    }

    @PostMapping("/participants")
    public ResponseEntity<Participant> crearParticipante(
            @RequestParam String name,
            @RequestParam String lastName,
            @RequestParam String nacionality,
            @RequestParam Integer totalPoints,// Este va a variar
            @RequestParam Integer age,
            @RequestParam String area,
            //  @RequestParam boolean certificateReached = false;
            @RequestParam Integer certificateReachedPoints,
            //  @RequestParam boolean specialEventAccess = false;
            @RequestParam Integer specialEventAccessPoints) {
        Participant participant = pointsParticipantService.crearParticipante(name, lastName, nacionality, totalPoints, age, area, certificateReachedPoints, specialEventAccessPoints);
        return ResponseEntity.ok(participant);
    }

    @GetMapping("/participants/{participantId}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Long participantId) {
        Participant participant = pointsParticipantService.getParticipantById(participantId);
        // .orElseThrow(() -> new IllegalArgumentException("Participante no encontrado: " + participantId));
        return ResponseEntity.ok(participant);
    }

    @GetMapping("/participants/certificado")
    public ResponseEntity<List<Participant>> obtenerParticipantesConCertificado() {
        List<Participant> participant = pointsParticipantService.obtenerParticipantesConCertificado();
        return ResponseEntity.ok(participant);
    }

    @GetMapping("/participants/acceso")
    public ResponseEntity<List<Participant>> obtenerParticipantesConAccesoEspecial() {
        List<Participant> participant = pointsParticipantService.obtenerParticipantesConAccesoEspecial();
        return ResponseEntity.ok(participant);
    }

    @PostMapping("/participants/{participantId}/add")
    public ResponseEntity<Void> acumularPuntos(
            @PathVariable Long participantId,
            @RequestParam int puntos) {

        pointsParticipantService.acumularPuntos(participantId, puntos);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/participants/{participantId}/use")
    public ResponseEntity<Void> usarPuntos(
            @PathVariable Long participantId,
            @RequestParam int puntos) {

        pointsParticipantService.usarPuntos(participantId, puntos);
        return ResponseEntity.ok().build();

    }

    // 💡 Endpoint: /points/participants/{id}/attend?conference
    @PostMapping("/participants/{participantId}/attend")
    public ResponseEntity<Void> registerAttendance(
            @PathVariable Long participantId,
            @RequestParam Long conferenceId) {
        Integer points = assistanceService.getPuntos(conferenceId);

        if (points == 0) {
            return ResponseEntity.badRequest().build(); // Conferencia no válida
        }

        // 2. Crear y disparar el evento
        AddPointsEvent event = new AddPointsEvent(
                // Argumento 1 (Object source): Generalmente 'this' (el objeto que dispara el evento)
                this,
                // Argumento 2 (Long participantId): El ID del path
                participantId,
                // Argumento 3 (Long idConferencia): El ID del RequestParam
                conferenceId,
                // Argumento 4 (Integer pointsEarned): Los puntos obtenidos
                points
        );
        eventPublisher.publishEvent(event);
        return ResponseEntity.ok().build();
    }

}
