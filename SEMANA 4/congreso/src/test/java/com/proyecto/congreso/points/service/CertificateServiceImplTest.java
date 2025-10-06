package com.proyecto.congreso.points.service;

import com.proyecto.congreso.pases.model.Certificate;
import com.proyecto.congreso.pases.repository.CertificateRepository;
import com.proyecto.congreso.pases.service.CertificateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceImplTest {
    @Mock // Simula el repositorio (la dependencia)
    private CertificateRepository certificateRepository;

    @InjectMocks // Inyecta los mocks en la clase a probar
    private CertificateServiceImpl certificateService;

    private Certificate testCertificate1;
    private Long PASS_ID = 10L;

    @BeforeEach
    void setUp() {
        testCertificate1 = new Certificate();
        testCertificate1.setCertificateId(1L);
        testCertificate1.setPassId(PASS_ID);
    }

    // ----------- TITULOS DE TESTS DESCRIPTIVOS -----------
    @Test
    void shouldGetAllCertificatesSuccessfully() {
        // Given
        Certificate testCertificate2 = new Certificate();
        testCertificate2.setCertificateId(2L);
        List<Certificate> expectedCertificates = Arrays.asList(testCertificate1, testCertificate2);

        when(certificateRepository.findAll()).thenReturn(expectedCertificates);

        // When
        List<Certificate> actualCertificates = certificateService.getAllCertificate();

        // Then
        assertNotNull(actualCertificates, "The returned list should not be null.");
        assertEquals(2, actualCertificates.size(), "The list size should match the mock data.");

        verify(certificateRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoCertificatesExist() {
        // Given
        when(certificateRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Certificate> actualCertificates = certificateService.getAllCertificate();

        // Then
        assertNotNull(actualCertificates, "The returned list should not be null.");
        assertTrue(actualCertificates.isEmpty(), "The list should be empty.");

        verify(certificateRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnTrueWhenCertificateExistsForGivenPassId() {
        // Given
        when(certificateRepository.existsByPassId(PASS_ID)).thenReturn(true);

        // When
        boolean exists = certificateService.existsByPassId(PASS_ID);

        // Then
        assertTrue(exists, "Should return true because a certificate with the Pass ID exists.");

        verify(certificateRepository, times(1)).existsByPassId(PASS_ID);
    }

    @Test
    void shouldReturnFalseWhenCertificateDoesNotExistForGivenPassId() {
        // Given
        Long NON_EXISTENT_PASS_ID = 99L;
        when(certificateRepository.existsByPassId(NON_EXISTENT_PASS_ID)).thenReturn(false);

        // When
        boolean exists = certificateService.existsByPassId(NON_EXISTENT_PASS_ID);

        // Then
        assertFalse(exists, "Should return false because no certificate with the Pass ID exists.");

        verify(certificateRepository, times(1)).existsByPassId(NON_EXISTENT_PASS_ID);
    }
}
