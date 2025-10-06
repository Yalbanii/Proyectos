package com.proyecto.congreso.participantes.service;
import com.proyecto.congreso.participantes.model.Participant;
import com.proyecto.congreso.participantes.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Permite usar las anotaciones @Mock y @InjectMocks
@ExtendWith(MockitoExtension.class)
public class ParticipantServiceImplTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ParticipantServiceImpl participantService;

    private Participant participanteActivo;
    private final Long ID_PRUEBA = 1L;
    private final String EMAIL_PRUEBA = "test@ejemplo.com";
    private final String EMAIL_EXISTENTE = "existente@ejemplo.com";

    @BeforeEach
    void setUp() {
        participanteActivo = new Participant();
        participanteActivo.setParticipantId(ID_PRUEBA);
        participanteActivo.setName("MockName");
        participanteActivo.setEmail(EMAIL_PRUEBA);
        participanteActivo.setStatus(Participant.ParticipantStatus.ACTIVE);
    }

    // ----------- TITULOS DE TESTS DESCRIPTIVOS -----------
    @Test
    void createParticipant_DebeLanzarExcepcion_CuandoElEmailYaExiste() {
        Participant participanteConEmailExistente = new Participant();
        participanteConEmailExistente.setEmail(EMAIL_EXISTENTE);

        when(participantRepository.existsByEmail(EMAIL_EXISTENTE)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            participantService.createParticipant(participanteConEmailExistente);
        }, "Debe lanzar IllegalArgumentException si el email existe.");

        verify(participantRepository, never()).save(any(Participant.class));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void getParticipantById_DebeDevolverParticipante_CuandoExiste() {
        // Mock: findById debe devolver un Optional con el participante
        when(participantRepository.findById(ID_PRUEBA)).thenReturn(Optional.of(participanteActivo));

        Participant resultado = participantService.getParticipantById(ID_PRUEBA);

        assertNotNull(resultado);
        assertEquals(ID_PRUEBA, resultado.getParticipantId());
    }

    @Test
    void getParticipantById_DebeLanzarExcepcion_CuandoNoExiste() {
        when(participantRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            participantService.getParticipantById(99L);
        }, "Debe lanzar IllegalArgumentException si no encuentra el participante.");
    }

    @Test
    void updateParticipant_DebeActualizarYGuardar_CuandoElEmailNoCambia() {
        Participant datosActualizados = new Participant();
        datosActualizados.setEmail(EMAIL_PRUEBA);
        datosActualizados.setName("NuevoNombre");

        when(participantRepository.findById(ID_PRUEBA)).thenReturn(Optional.of(participanteActivo));
        when(participantRepository.save(any(Participant.class))).thenReturn(participanteActivo);

        Participant resultado = participantService.updateParticipant(ID_PRUEBA, datosActualizados);

        assertEquals("NuevoNombre", resultado.getName());
        verify(participantRepository, never()).existsByEmail(anyString());
        verify(participantRepository, times(1)).save(participanteActivo);
    }

    @Test
    void updateParticipant_DebeActualizarYGuardar_CuandoElEmailCambiaYNoExiste() {
        // Arrange
        Participant datosActualizados = new Participant();
        datosActualizados.setEmail("nuevo.email@ejemplo.com"); // El email CAMBIA
        datosActualizados.setName("NuevoNombre");

        // Mock:
        when(participantRepository.findById(ID_PRUEBA)).thenReturn(Optional.of(participanteActivo));
        // Mock: existsByEmail debe devolver false para el nuevo email
        when(participantRepository.existsByEmail("nuevo.email@ejemplo.com")).thenReturn(false);
        when(participantRepository.save(any(Participant.class))).thenReturn(participanteActivo);

        // Act
        Participant resultado = participantService.updateParticipant(ID_PRUEBA, datosActualizados);

        // Assert
        assertEquals("nuevo.email@ejemplo.com", resultado.getEmail());
        verify(participantRepository, times(1)).existsByEmail("nuevo.email@ejemplo.com");
    }

    @Test
    void updateParticipant_DebeLanzarExcepcion_CuandoElEmailCambiaYYaExiste() {
        // Arrange
        Participant datosActualizados = new Participant();
        datosActualizados.setEmail(EMAIL_EXISTENTE);

        // Mock:
        when(participantRepository.findById(ID_PRUEBA)).thenReturn(Optional.of(participanteActivo));
        when(participantRepository.existsByEmail(EMAIL_EXISTENTE)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            participantService.updateParticipant(ID_PRUEBA, datosActualizados);
        }, "Debe lanzar IllegalArgumentException si el nuevo email ya existe.");

        verify(participantRepository, never()).save(any(Participant.class));
    }

    @Test
    void getAllParticipants_DebeDevolverLista() {
        when(participantRepository.findAll()).thenReturn(Collections.singletonList(participanteActivo));

        List<Participant> resultado = participantService.getAllParticipants();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(participantRepository, times(1)).findAll();
    }

    @Test
    void getParticipantByStatus_DebeDevolverListaFiltrada() {
        when(participantRepository.findByStatus(Participant.ParticipantStatus.ACTIVE))
                .thenReturn(Collections.singletonList(participanteActivo));

        List<Participant> resultado = participantService.getParticipantByStatus(Participant.ParticipantStatus.ACTIVE);

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(Participant.ParticipantStatus.ACTIVE, resultado.get(0).getStatus());
        verify(participantRepository, times(1)).findByStatus(Participant.ParticipantStatus.ACTIVE);
    }

    @Test
    void existsByEmail_DebeDevolverResultadoDelRepositorio() {
        when(participantRepository.existsByEmail(EMAIL_PRUEBA)).thenReturn(true);

        // Act & Assert
        assertTrue(participantService.existsByEmail(EMAIL_PRUEBA));
        verify(participantRepository, times(1)).existsByEmail(EMAIL_PRUEBA);
    }

    @Test
    void deleteParticipant_DebeCambiarEstadoAInactiveYSeguirElSoftDelete() {
        when(participantRepository.findById(ID_PRUEBA)).thenReturn(Optional.of(participanteActivo));
        when(participantRepository.save(any(Participant.class))).thenReturn(participanteActivo);

        participantService.deleteParticipant(ID_PRUEBA);

        // Verifica que el estado fue cambiado ANTES de guardarse
        assertEquals(Participant.ParticipantStatus.INACTIVE, participanteActivo.getStatus());
        verify(participantRepository, times(1)).save(participanteActivo);
    }

    @Test
    void activateParticipant_DebeCambiarEstadoAActiveYGuardar() {
        participanteActivo.setStatus(Participant.ParticipantStatus.INACTIVE);

        when(participantRepository.findById(ID_PRUEBA)).thenReturn(Optional.of(participanteActivo));
        when(participantRepository.save(any(Participant.class))).thenReturn(participanteActivo);

        Participant resultado = participantService.activateParticipant(ID_PRUEBA);

        assertEquals(Participant.ParticipantStatus.ACTIVE, resultado.getStatus());
        verify(participantRepository, times(1)).save(participanteActivo);
    }

    @Test
    void deactivateParticipant_DebeCambiarEstadoAInactiveYGuardar() {
        when(participantRepository.findById(ID_PRUEBA)).thenReturn(Optional.of(participanteActivo));
        when(participantRepository.save(any(Participant.class))).thenReturn(participanteActivo);

        Participant resultado = participantService.deactivateParticipant(ID_PRUEBA);

        assertEquals(Participant.ParticipantStatus.INACTIVE, resultado.getStatus());
        verify(participantRepository, times(1)).save(participanteActivo);
    }
}
