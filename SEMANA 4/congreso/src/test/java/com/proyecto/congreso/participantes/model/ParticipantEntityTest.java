package com.proyecto.congreso.participantes.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantEntityTest {
    private final Long ID_PRUEBA = 1L;
    private final String NOMBRE_PRUEBA = "Sofia";
    private final String APELLIDO_PRUEBA = "Hernandez";
    private final String EMAIL_PRUEBA = "sofia.h@congreso.org";
    private final String TELEFONO_PRUEBA = "5512345678"; // 10 dígitos
    private final String NACIONALIDAD_PRUEBA = "MX";
    private final Integer EDAD_PRUEBA = 28;
    private final String AREA_PRUEBA = "Finanzas";


    // ----------- TITULOS DE TESTS DESCRIPTIVOS -----------
    @Test
    void noArgsConstructor_DebeCrearParticipanteConEstadoActivoPorDefecto() {
        Participant participante = new Participant();

        assertNotNull(participante, "El objeto no debe ser nulo.");
        assertEquals(Participant.ParticipantStatus.ACTIVE, participante.getStatus(),
                "El estado por defecto debe ser ACTIVE.");
        assertNull(participante.getParticipantId(), "El ID debe ser nulo.");
        assertNull(participante.getName(), "El Nombre debe ser nulo.");

        participante.setName(NOMBRE_PRUEBA);
        assertEquals(NOMBRE_PRUEBA, participante.getName());
    }

    @Test
    void allArgsConstructor_DebeCrearParticipanteConTodosLosValores() {
        LocalDateTime fechaCreacion = LocalDateTime.now().minusHours(1);
        LocalDateTime fechaActualizacion = LocalDateTime.now();

        Participant participante = new Participant(
                ID_PRUEBA,
                NOMBRE_PRUEBA,
                APELLIDO_PRUEBA,
                EMAIL_PRUEBA,
                TELEFONO_PRUEBA,
                NACIONALIDAD_PRUEBA,
                EDAD_PRUEBA,
                AREA_PRUEBA,
                Participant.ParticipantStatus.INACTIVE,
                fechaCreacion,
                fechaActualizacion
        );

        assertEquals(ID_PRUEBA, participante.getParticipantId(), "El ID debe coincidir.");
        assertEquals(NOMBRE_PRUEBA, participante.getName(), "El Nombre debe coincidir.");
        assertEquals(APELLIDO_PRUEBA, participante.getLastName(), "El Apellido debe coincidir.");
        assertEquals(EMAIL_PRUEBA, participante.getEmail(), "El Email debe coincidir.");
        assertEquals(TELEFONO_PRUEBA, participante.getPhone(), "El Teléfono debe coincidir.");
        assertEquals(NACIONALIDAD_PRUEBA, participante.getNacionality(), "La Nacionalidad debe coincidir.");
        assertEquals(EDAD_PRUEBA, participante.getAge(), "La Edad debe coincidir.");
        assertEquals(AREA_PRUEBA, participante.getArea(), "El Área debe coincidir.");
        assertEquals(Participant.ParticipantStatus.INACTIVE, participante.getStatus(), "El Estado debe coincidir.");
        assertTrue(participante.getCreatedAt().isEqual(fechaCreacion) || participante.getCreatedAt().isBefore(fechaCreacion));
        assertTrue(participante.getUpdatedAt().isEqual(fechaActualizacion) || participante.getUpdatedAt().isBefore(fechaActualizacion));
    }


    @Test
    void setters_DebenPermitirModificarTodosLosCampos() {
        Participant participante = new Participant();

        participante.setParticipantId(ID_PRUEBA);
        participante.setName(NOMBRE_PRUEBA);
        participante.setLastName(APELLIDO_PRUEBA);
        participante.setEmail(EMAIL_PRUEBA);
        participante.setPhone(TELEFONO_PRUEBA);
        participante.setNacionality(NACIONALIDAD_PRUEBA);
        participante.setAge(EDAD_PRUEBA);
        participante.setArea(AREA_PRUEBA);
        participante.setStatus(Participant.ParticipantStatus.INACTIVE);

        LocalDateTime ahora = LocalDateTime.now();
        participante.setCreatedAt(ahora);
        participante.setUpdatedAt(ahora.plusMinutes(5));

        assertEquals(ID_PRUEBA, participante.getParticipantId());
        assertEquals(TELEFONO_PRUEBA, participante.getPhone());
        assertEquals(Participant.ParticipantStatus.INACTIVE, participante.getStatus());
        assertEquals(ahora, participante.getCreatedAt());
        assertEquals(ahora.plusMinutes(5), participante.getUpdatedAt());
    }

    @Test
    void participantStatusEnum_DebeContenerLosValoresCorrectos() {
        assertEquals(2, Participant.ParticipantStatus.values().length, "El enum debe tener 2 valores.");
        assertEquals("ACTIVE", Participant.ParticipantStatus.ACTIVE.name());
        assertEquals("INACTIVE", Participant.ParticipantStatus.INACTIVE.name());
    }
}
