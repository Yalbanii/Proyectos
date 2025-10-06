package com.proyecto.congreso.points.assistance.dto;

import org.junit.jupiter.api.Test;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

class AsistenciaPointsDataTest {

    private static final String ASISTENCIA_ID = "ASIST-789";
    private static final Long PASS_ID = 456L;
    private static final String CONFERENCIA_ID = "CONF-ABC";
    private static final Integer POINTS_AWARDED = 25;
    private static final Integer CURRENT_BALANCE = 75;
    private static final Integer NEW_BALANCE = CURRENT_BALANCE + POINTS_AWARDED;

    // ----------- TITULOS DE TESTS DESCRIPTIVOS -----------
    @Test
    void allArgsConstructor_shouldCreateObjectWithAllFieldsSet() {
        // When
        AsistenciaPointsData data = new AsistenciaPointsData(
                ASISTENCIA_ID, PASS_ID, CONFERENCIA_ID, POINTS_AWARDED, CURRENT_BALANCE, NEW_BALANCE
        );

        // Then
        assertNotNull(data, "El objeto no debe ser nulo.");
        assertEquals(ASISTENCIA_ID, data.getAsistenciaId());
        assertEquals(PASS_ID, data.getPassId());
        assertEquals(CONFERENCIA_ID, data.getConferenciaId());
        assertEquals(POINTS_AWARDED, data.getPointsAwarded());
        assertEquals(CURRENT_BALANCE, data.getCurrentBalance());
        assertEquals(NEW_BALANCE, data.getNewBalance(), "El nuevo balance debe ser 100.");
    }


    @Test
    void secondaryConstructor_shouldCalculateNewBalanceCorrectly() {
        // When
        AsistenciaPointsData data = new AsistenciaPointsData(
                ASISTENCIA_ID, PASS_ID, CONFERENCIA_ID, POINTS_AWARDED, CURRENT_BALANCE
        );

        // Then
        assertNotNull(data, "El objeto no debe ser nulo.");
        assertEquals(ASISTENCIA_ID, data.getAsistenciaId());
        assertEquals(CURRENT_BALANCE, data.getCurrentBalance(), "El balance actual debe ser 75.");

        assertEquals(NEW_BALANCE, data.getNewBalance(), "El nuevo balance debe calcularse como actual + awarded (75 + 25 = 100).");
    }

    @Test
    void noArgsConstructor_andSetters_shouldSetAndRetrieveValues() {
        // Given
        AsistenciaPointsData data = new AsistenciaPointsData();

        // When
        data.setAsistenciaId(ASISTENCIA_ID);
        data.setCurrentBalance(10);
        data.setPointsAwarded(5);
        data.setNewBalance(15);

        // Then
        assertEquals(ASISTENCIA_ID, data.getAsistenciaId(), "Getter/Setter de ID fallo.");
        assertEquals(10, data.getCurrentBalance(), "Getter/Setter de CurrentBalance fallo.");
        assertEquals(5, data.getPointsAwarded(), "Getter/Setter de PointsAwarded fallo.");
        assertEquals(15, data.getNewBalance(), "Getter/Setter de NewBalance fallo.");
    }


    @Test
    void shouldImplementSerializable() {
        // Then
        assertTrue(Serializable.class.isAssignableFrom(AsistenciaPointsData.class),
                "La clase debe implementar la interfaz Serializable.");
        assertEquals(1L, AsistenciaPointsData.serialVersionUID,
                "El serialVersionUID debe ser 1L.");
    }
}
