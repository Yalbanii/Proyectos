package com.proyecto.congreso.points.service;

import com.proyecto.congreso.participantes.model.Participant;
import com.proyecto.congreso.participantes.repository.ParticipantRepository;
import com.proyecto.congreso.pases.events.PassAdquiredEvent;
import com.proyecto.congreso.pases.model.Pass;
import com.proyecto.congreso.pases.repository.PassRepository;
import com.proyecto.congreso.pases.service.PassServiceImpl;
import com.proyecto.congreso.points.exchange.events.ExchangeRequestedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassServiceImplTest {
    private static final Long PASS_ID = 1L;
    private static final Long PARTICIPANT_ID = 10L;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private PassRepository passRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    // InjectMocks crea una instancia de la clase y le inyecta los mocks
    @InjectMocks
    private PassServiceImpl passService;

    private Pass testPass;
    private Participant testParticipant;

    @BeforeEach
    void setUp() {
        testPass = new Pass();
        testPass.setPassId(PASS_ID);
        testPass.setParticipantId(PARTICIPANT_ID);
        testPass.setPointsBalance(0);
        testPass.setStatus(Pass.PassStatus.ACTIVE);
        testPass.setPassType(Pass.PassType.GENERAL);

        testParticipant = new Participant();
        testParticipant.setParticipantId(PARTICIPANT_ID);
    }

    // ----------- TITULOS DE TESTS DESCRIPTIVOS -----------
    @Test
    void shouldCreatePassSuccessfullyAndPublishEvent() {
        // Given
        Pass passToCreate = new Pass();
        passToCreate.setParticipantId(PARTICIPANT_ID);

        when(participantRepository.findById(PARTICIPANT_ID)).thenReturn(Optional.of(testParticipant));
        when(passRepository.save(any(Pass.class))).thenReturn(testPass);

        // When
        Pass createdPass = passService.createPass(passToCreate);

        // Then
        assertNotNull(createdPass);
        assertEquals(Pass.PassStatus.ACTIVE, createdPass.getStatus(), "Status should be set to ACTIVE.");
        assertEquals(0, createdPass.getPointsBalance(), "PointsBalance should default to 0.");

        verify(participantRepository).findById(PARTICIPANT_ID);
        verify(passRepository).save(any(Pass.class));
        verify(eventPublisher).publishEvent(any(PassAdquiredEvent.class));
    }

    @Test
    void createPass_shouldThrowExceptionIfParticipantNotFound() {
        // Given
        Pass passToCreate = new Pass();
        passToCreate.setParticipantId(PARTICIPANT_ID);

        when(participantRepository.findById(PARTICIPANT_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> passService.createPass(passToCreate));

        verify(passRepository, never()).save(any(Pass.class));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void createPass_shouldThrowExceptionIfInitialBalanceIsNegative() {
        // Given
        Pass passToCreate = new Pass();
        passToCreate.setParticipantId(PARTICIPANT_ID);
        passToCreate.setPointsBalance(-50);

        when(participantRepository.findById(PARTICIPANT_ID)).thenReturn(Optional.of(testParticipant));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> passService.createPass(passToCreate));

        verify(passRepository, never()).save(any(Pass.class));
    }


    @Test
    void shouldGetPassByIdSuccessfully() {
        // Given
        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(testPass));

        // When
        Pass foundPass = passService.getPassById(PASS_ID);

        // Then
        assertNotNull(foundPass);
        assertEquals(PASS_ID, foundPass.getPassId());
        verify(passRepository).findById(PASS_ID);
    }

    @Test
    void getPassById_shouldThrowExceptionIfNotFound() {
        // Given
        when(passRepository.findById(PASS_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> passService.getPassById(PASS_ID));
        verify(passRepository).findById(PASS_ID);
    }


    @Test
    void shouldGetAllPassesSuccessfully() {
        // Given
        List<Pass> expectedList = Arrays.asList(testPass, new Pass());
        when(passRepository.findAll()).thenReturn(expectedList);

        // When
        List<Pass> actualList = passService.getAllPass();

        // Then
        assertEquals(2, actualList.size());
        verify(passRepository).findAll();
    }


    @Test
    void shouldUpdatePassTypeSuccessfully() {
        // Given
        Pass updateData = new Pass();
        updateData.setPassType(Pass.PassType.ALL_INCLUDED);

        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(testPass)); // Simula encontrar el existente
        when(passRepository.save(any(Pass.class))).thenReturn(testPass); // Simula guardar el actualizado

        // When
        Pass updatedPass = passService.updatePass(PASS_ID, updateData);

        // Then
        assertEquals(Pass.PassType.ALL_INCLUDED, updatedPass.getPassType());

        verify(passRepository).findById(PASS_ID);
        verify(passRepository).save(testPass);
    }

    @Test
    void shouldSoftDeletePassSuccessfullyWhenBalanceIsZero() {
        // Given
        testPass.setPointsBalance(0);
        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(testPass));

        // When
        passService.deletePass(PASS_ID);

        // Then
        assertEquals(Pass.PassStatus.CLOSED, testPass.getStatus(), "Status should be CLOSED.");
        verify(passRepository).findById(PASS_ID);
        verify(passRepository).save(testPass);
    }

    @Test
    void deletePass_shouldThrowExceptionWhenBalanceIsNotZero() {
        // Given
        testPass.setPointsBalance(50);
        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(testPass));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> passService.deletePass(PASS_ID));

        verify(passRepository, never()).save(any(Pass.class));
    }

    @Test
    void shouldActivatePassSuccessfully() {
        // Given
        testPass.setStatus(Pass.PassStatus.CLOSED);
        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(testPass));
        when(passRepository.save(any(Pass.class))).thenReturn(testPass);

        // When
        Pass activatedPass = passService.activatePass(PASS_ID);

        // Then
        assertEquals(Pass.PassStatus.ACTIVE, activatedPass.getStatus());
        verify(passRepository).save(testPass);
    }


    @Test
    void shouldClosePassSuccessfullyWhenBalanceIsZero() {
        // Given
        testPass.setPointsBalance(0);
        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(testPass));
        when(passRepository.save(any(Pass.class))).thenReturn(testPass);

        // When
        Pass closedPass = passService.closePass(PASS_ID);

        // Then
        assertEquals(Pass.PassStatus.CLOSED, closedPass.getStatus());
        verify(passRepository).save(testPass);
    }

    @Test
    void closePass_shouldThrowExceptionWhenBalanceIsNotZero() {
        // Given
        testPass.setPointsBalance(10);
        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(testPass));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> passService.closePass(PASS_ID));

        verify(passRepository, never()).save(any(Pass.class));
    }


    @Test
    void shouldGetPassByParticipantIdSuccessfully() {
        // Given
        List<Pass> expectedList = Collections.singletonList(testPass);
        when(passRepository.findByParticipantId(PARTICIPANT_ID)).thenReturn(expectedList);

        // When
        List<Pass> actualList = passService.getPassByParticipantId(PARTICIPANT_ID);

        // Then
        assertFalse(actualList.isEmpty());
        verify(passRepository).findByParticipantId(PARTICIPANT_ID);
    }

    @Test
    void shouldGetActivePassByParticipantIdSuccessfully() {
        // Given
        List<Pass> expectedList = Collections.singletonList(testPass);
        when(passRepository.findActivePassByParticipantId(PARTICIPANT_ID)).thenReturn(expectedList);

        // When
        List<Pass> actualList = passService.getActivePassByParticipantId(PARTICIPANT_ID);

        // Then
        assertFalse(actualList.isEmpty());
        verify(passRepository).findActivePassByParticipantId(PARTICIPANT_ID);
    }

    @Test
    void shouldGetPassByStatusSuccessfully() {
        // Given
        Pass.PassStatus status = Pass.PassStatus.ACTIVE;
        List<Pass> expectedList = Collections.singletonList(testPass);
        when(passRepository.findByStatus(status)).thenReturn(expectedList);

        // When
        List<Pass> actualList = passService.getPassByStatus(status);

        // Then
        assertFalse(actualList.isEmpty());
        verify(passRepository).findByStatus(status);
    }

    @Test
    void shouldGetPassByTypeSuccessfully() {
        // Given
        Pass.PassType type = Pass.PassType.GENERAL;
        List<Pass> expectedList = Collections.singletonList(testPass);
        when(passRepository.findByPassType(type)).thenReturn(expectedList);

        // When
        List<Pass> actualList = passService.getPassByType(type);

        // Then
        assertFalse(actualList.isEmpty());
        verify(passRepository).findByPassType(type);
    }

    @Test
    void shouldCountPassByParticipantIdSuccessfully() {
        // Given
        long expectedCount = 5L;
        when(passRepository.countByParticipantId(PARTICIPANT_ID)).thenReturn(expectedCount);

        // When
        long actualCount = passService.countPassByParticipantId(PARTICIPANT_ID);

        // Then
        assertEquals(expectedCount, actualCount);
        verify(passRepository).countByParticipantId(PARTICIPANT_ID);
    }

    @Test
    void shouldExistByPassIdSuccessfully() {
        // Given
        when(passRepository.existsByPassId(PASS_ID)).thenReturn(true);

        // When
        boolean exists = passService.existsByPassId(PASS_ID);

        // Then
        assertTrue(exists);
        verify(passRepository).existsByPassId(PASS_ID);
    }


    @Test
    void shouldPublishExchangeRequestedEvent() {
        // Given
        String FREEBIE_ID = "F001";

        // When
        passService.startExchange(PASS_ID, FREEBIE_ID);

        // Then
        verify(eventPublisher).publishEvent(any(ExchangeRequestedEvent.class));

    }
}
