package com.proyecto.congreso.points.assistance.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AssistanceResponseTest {
    private static final String ID = "R-123";
    private static final Long PASS_ID = 456L;
    private static final Long PARTICIPANT_ID = 789L;
    private static final String CONFERENCIA_ID = "CONF-XYZ";
    private static final String TITULO = "Desarrollo Ágil";
    private static final Integer PUNTOS = 10;
    private static final LocalDateTime FECHA = LocalDateTime.of(2025, 1, 1, 10, 0);
    private static final String STATUS = "PENDIENTE";

    @Test
    void allArgsConstructor_shouldCreateObjectWithAllFieldsSet() {
        // When
        AssistanceResponse response = new AssistanceResponse(
                ID, PASS_ID, PARTICIPANT_ID, CONFERENCIA_ID, TITULO, PUNTOS, FECHA, STATUS
        );

        // Then
        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals(PARTICIPANT_ID, response.getParticipantId());
        assertEquals(TITULO, response.getTituloConferencia());
        assertEquals(PUNTOS, response.getPuntosOtorgados());
    }

    @Test
    void noArgsConstructor_andSetters_shouldSetAndRetrieveValues() {
        // Given
        AssistanceResponse response = new AssistanceResponse();

        // When
        response.setId(ID);
        response.setFechaAsistencia(FECHA);
        response.setStatus(STATUS);

        // Then
        assertEquals(ID, response.getId(), "Getter/Setter de ID fallo.");
        assertEquals(FECHA, response.getFechaAsistencia(), "Getter/Setter de Fecha fallo.");
        assertEquals(STATUS, response.getStatus(), "Getter/Setter de Status fallo.");
        assertNull(response.getPassId(), "Otros campos no seteados deben ser nulos.");
    }

}

