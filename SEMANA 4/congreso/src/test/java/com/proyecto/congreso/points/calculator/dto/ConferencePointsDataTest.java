package com.proyecto.congreso.points.calculator.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConferencePointsDataTest {
    private static final String CONFERENCIA_ID = "C001";
    private static final String TITULO = "Ética en la IA";
    private static final Integer PUNTOS = 5;

    // ----------- TITULOS DE TESTS DESCRIPTIVOS -----------
    @Test
    void allArgsConstructor_shouldCreateObjectWithAllFieldsSet() {
        // Given
        ConferencePointsData data = new ConferencePointsData(
                CONFERENCIA_ID,
                "Lunes",
                "01-03",
                "09:00",
                "10:00",
                "1.0",
                "Sede Central",
                "Keynote",
                TITULO,
                "Dr. Smith",
                PUNTOS
        );

        // Then
        assertNotNull(data, "El objeto no debe ser nulo.");
        assertEquals(CONFERENCIA_ID, data.getConferenciaId(), "El ID de la conferencia no coincide.");
        assertEquals(TITULO, data.getTitulo(), "El titulo no coincide.");
        assertEquals(PUNTOS, data.getPuntos(), "Los puntos no coinciden.");
        assertEquals("Lunes", data.getDia(), "El dia no coincide.");
        // Verificación de otro campo de String para mayor cobertura
        assertEquals("Sede Central", data.getSede(), "La sede no coincide.");
    }

    @Test
    void noArgsConstructor_andSetters_shouldSetAndRetrieveValues() {
        // Given
        ConferencePointsData data = new ConferencePointsData();

        // When
        data.setConferenciaId(CONFERENCIA_ID);
        data.setTitulo(TITULO);
        data.setPuntos(PUNTOS);
        data.setDia("Martes");

        // Then
        assertEquals(CONFERENCIA_ID, data.getConferenciaId(), "Getter/Setter de ID fallo.");
        assertEquals(TITULO, data.getTitulo(), "Getter/Setter de Título fallo.");
        assertEquals(PUNTOS, data.getPuntos(), "Getter/Setter de Puntos fallo.");
        assertEquals("Martes", data.getDia(), "Getter/Setter de Día fallo.");
    }

    @Test
    void shouldHandleNullValuesGracefully() {
        // Given
        ConferencePointsData data = new ConferencePointsData();

        // When
        data.setTitulo(null);
        data.setPuntos(null);

        // Then
        assertNull(data.getTitulo(), "El titulo debe ser nulo.");
        assertNull(data.getPuntos(), "Los puntos deben ser nulos.");
    }

}
