package com.proyecto.congreso.points.service;
import com.proyecto.congreso.participantes.model.Participant;
import com.proyecto.congreso.participantes.repository.ParticipantRepository;
import com.proyecto.congreso.pases.events.CertificateEvent;
import com.proyecto.congreso.pases.model.Certificate;
import com.proyecto.congreso.pases.model.Pass;
import com.proyecto.congreso.pases.repository.CertificateRepository;
import com.proyecto.congreso.pases.repository.PassRepository;
import com.proyecto.congreso.pases.service.PassPointsEventHandler;
import com.proyecto.congreso.points.assistance.events.AssistanceRegisteredEvent;
import com.proyecto.congreso.points.calculator.repository.FreebieRepository;
import com.proyecto.congreso.points.exchange.events.ExchangeFailedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassPointsEventHandlerTest {

    private static final Long PASS_ID = 1L;
    private static final Long PARTICIPANT_ID = 10L;
    private static final String FREEBIE_ID = "F001";
    private static final Integer POINTS_CERTIFICATE = 25;
    private static final Integer POINTS_SPECIAL_ACCESS = 30;

    @Mock private PassRepository passRepository;
    @Mock private ParticipantRepository participantRepository;
    @Mock private CertificateRepository certificateRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    // FreebieRepository no se usa en la lógica, pero se mockea por RequiredArgsConstructor
    @Mock private FreebieRepository freebieRepository;

    @InjectMocks
    private PassPointsEventHandler eventHandler;

    private Pass activePass;
    private Participant testParticipant;

    @BeforeEach
    void setUp() {
        activePass = new Pass();
        activePass.setPassId(PASS_ID);
        activePass.setParticipantId(PARTICIPANT_ID);
        activePass.setStatus(Pass.PassStatus.ACTIVE);
        activePass.setPointsBalance(10);
        activePass.setPointsCertificate(POINTS_CERTIFICATE);
        activePass.setPointsSpecialAccess(POINTS_SPECIAL_ACCESS);

        testParticipant = new Participant();
        testParticipant.setParticipantId(PARTICIPANT_ID);
        testParticipant.setName("Jane");
        testParticipant.setLastName("Doe");
        testParticipant.setEmail("jane.doe@example.com");

        try (MockedStatic<Certificate> mockedCertificate = mockStatic(Certificate.class)) {
            mockedCertificate.when(() -> Certificate.create(any(), any(), any(), any(), any()))
                    .thenAnswer(invocation -> new Certificate());
        } catch (Exception e) {
            // Ignorar excepción si Mockito no soporta static mocking fácilmente
        }
    }

    // ----------- TITULOS DE TESTS DESCRIPTIVOS -----------
    @Test
    void handleAssistanceRegistered_shouldSumPointsSuccessfully() {

        ArgumentCaptor<Pass> passCaptor = ArgumentCaptor.forClass(Pass.class);
        AssistanceRegisteredEvent event = mock(AssistanceRegisteredEvent.class);
        Integer pointsToAdd = 5;

        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(activePass));


        when(passRepository.save(any(Pass.class))).thenReturn(activePass);

        // When
        when(event.getPassId()).thenReturn(PASS_ID);
        when(event.getAmountPoints()).thenReturn(pointsToAdd);
        eventHandler.handleAssistanceRegistered(event);

        verify(passRepository).save(passCaptor.capture());

        Pass savedPass = passCaptor.getValue();

        assertEquals(15, savedPass.getPointsBalance(), "El balance debe ser 10 + 5 = 15.");
        assertEquals(Pass.PointsMovementAdd.ADD, savedPass.getPointsAdd(), "Debe establecer el movimiento.");

        verify(passRepository).findById(PASS_ID);
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void handleAssistanceRegistered_shouldNotSumPointsIfPassIsNotActive() {
        // Given
        activePass.setStatus(Pass.PassStatus.CLOSED);
        AssistanceRegisteredEvent event = new AssistanceRegisteredEvent(PASS_ID, 5);

        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(activePass));

        // When
        eventHandler.handleAssistanceRegistered(event);

        // Then
        assertEquals(10, activePass.getPointsBalance(), "El balance no debe cambiar.");
        verify(passRepository, never()).save(any(Pass.class));
    }

    @Test
    void handleAssistanceRegistered_shouldReachCertificateAchievement() {
        ArgumentCaptor<Pass> passCaptor = ArgumentCaptor.forClass(Pass.class);

        // Given
        Integer pointsToAdd = 15;
        activePass.setPointsBalance(10);

        AssistanceRegisteredEvent event = mock(AssistanceRegisteredEvent.class);
        when(event.getPassId()).thenReturn(PASS_ID);
        when(event.getAmountPoints()).thenReturn(pointsToAdd);

        // Configuración del Mock
        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(activePass));
        when(passRepository.save(any(Pass.class))).thenReturn(activePass);

        // When
        eventHandler.handleAssistanceRegistered(event);

        // Then
        verify(passRepository).save(passCaptor.capture());
        Pass savedPass = passCaptor.getValue();

        assertEquals(POINTS_CERTIFICATE, savedPass.getPointsBalance());
        assertEquals(Pass.CertificateStatus.REACHED, savedPass.getCertificateStatus());

        verify(passRepository).findById(PASS_ID);
        verify(eventPublisher).publishEvent(any(CertificateEvent.class));
        verify(eventPublisher, never()).publishEvent(any(ExchangeFailedEvent.class));
    }

    @Test
    void handleAssistanceRegistered_shouldNotPublishCertificateEventIfAlreadyReached() {
        ArgumentCaptor<Pass> passCaptor = ArgumentCaptor.forClass(Pass.class);

        // Given
        activePass.setCertificateStatus(Pass.CertificateStatus.REACHED);
        activePass.setPointsBalance(POINTS_CERTIFICATE); // 25 puntos

        Integer pointsToAdd = 5;

        AssistanceRegisteredEvent event = mock(AssistanceRegisteredEvent.class);
        when(event.getPassId()).thenReturn(PASS_ID);
        when(event.getAmountPoints()).thenReturn(pointsToAdd);

        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(activePass));
        when(passRepository.save(any(Pass.class))).thenReturn(activePass);

        // When
        eventHandler.handleAssistanceRegistered(event);

        // Then
        verify(passRepository).save(passCaptor.capture());
        Pass savedPass = passCaptor.getValue();

        assertEquals(POINTS_CERTIFICATE + 5, savedPass.getPointsBalance(),
                "El balance final debe ser 30 (25 + 5).");

        assertEquals(Pass.AccessStatus.REACHED, savedPass.getAccessStatus(),
                "El AccessStatus debe ser REACHED, ya que se alcanzaron 30 puntos.");

        verify(eventPublisher, never()).publishEvent(any(CertificateEvent.class));
    }

    @Test
    void shouldReachBothCertificateAndSpecialAccessAchievements() {
        ArgumentCaptor<Pass> passCaptor = ArgumentCaptor.forClass(Pass.class);

        // Given
        Integer pointsToAdd = 20;
        activePass.setPointsBalance(10);

        AssistanceRegisteredEvent event = mock(AssistanceRegisteredEvent.class);
        when(event.getPassId()).thenReturn(PASS_ID);
        when(event.getAmountPoints()).thenReturn(pointsToAdd);

        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(activePass));
        when(passRepository.save(any(Pass.class))).thenReturn(activePass);

        // When
        eventHandler.handleAssistanceRegistered(event);

        // Then
        verify(passRepository).save(passCaptor.capture());
        Pass savedPass = passCaptor.getValue();

        // Asersiones de Estado
        assertEquals(POINTS_SPECIAL_ACCESS, savedPass.getPointsBalance(), "El balance final debe ser 30.");
        assertEquals(Pass.AccessStatus.REACHED, savedPass.getAccessStatus(), "AccessStatus debe ser REACHED.");
        assertEquals(Pass.CertificateStatus.REACHED, savedPass.getCertificateStatus(), "CertificateStatus tambien debe ser REACHED."); // NUEVA VERIFICACIÓN

        // Asersiones de Eventos
        verify(eventPublisher).publishEvent(any(CertificateEvent.class));
    }

    @Test
    void handleCertificateAchieved_shouldCreateCertificateSuccessfully() {
        // Given
        CertificateEvent event = new CertificateEvent(PASS_ID);
        Certificate mockSavedCertificate = new Certificate();
        mockSavedCertificate.setCertificateId(99L);

        when(certificateRepository.existsByPassId(PASS_ID)).thenReturn(false);
        when(passRepository.findById(PASS_ID)).thenReturn(Optional.of(activePass));
        when(participantRepository.findById(PARTICIPANT_ID)).thenReturn(Optional.of(testParticipant));
        when(certificateRepository.save(any(Certificate.class))).thenReturn(mockSavedCertificate);

        // When
        eventHandler.handleCertificateAchieved(event);

        // Then
        verify(certificateRepository).existsByPassId(PASS_ID);
        verify(passRepository).findById(PASS_ID);
        verify(participantRepository).findById(PARTICIPANT_ID);

        verify(certificateRepository).save(any(Certificate.class));
    }

    @Test
    void handleCertificateAchieved_shouldReturnIfCertificateAlreadyExists() {
        // Given
        CertificateEvent event = new CertificateEvent(PASS_ID);
        when(certificateRepository.existsByPassId(PASS_ID)).thenReturn(true);

        // When
        eventHandler.handleCertificateAchieved(event);

        // Then
        verify(certificateRepository).existsByPassId(PASS_ID);
        verify(passRepository, never()).findById(anyLong());
        verify(certificateRepository, never()).save(any(Certificate.class));
    }

    @Test
    void handleCertificateAchieved_shouldHandlePassNotFoundException() {
        // Given
        CertificateEvent event = new CertificateEvent(PASS_ID);
        when(certificateRepository.existsByPassId(PASS_ID)).thenReturn(false);
        when(passRepository.findById(PASS_ID)).thenReturn(Optional.empty());

        // When
        eventHandler.handleCertificateAchieved(event);

        // Then
        verify(passRepository).findById(PASS_ID);
        verify(participantRepository, never()).findById(anyLong());
        verify(certificateRepository, never()).save(any(Certificate.class));
    }
}
