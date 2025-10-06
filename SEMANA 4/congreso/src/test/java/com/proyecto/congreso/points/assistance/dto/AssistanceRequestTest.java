package com.proyecto.congreso.points.assistance.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AssistanceRequestTest {

    private static final Long PASS_ID = 100L;
    private static final String CONFERENCIA_ID = "CONF-456";

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    // ----------- TITULOS DE TESTS DESCRIPTIVOS -----------
    @Test
    void allArgsConstructor_shouldCreateObjectWithAllFieldsSet() {
        // When
        AssistanceRequest request = new AssistanceRequest(PASS_ID, CONFERENCIA_ID);

        // Then
        assertNotNull(request, "El objeto no debe ser nulo.");
        assertEquals(PASS_ID, request.getPassId(), "El Pass ID debe coincidir.");
        assertEquals(CONFERENCIA_ID, request.getConferenciaId(), "El Conferencia ID debe coincidir.");
    }

    @Test
    void noArgsConstructor_andSetters_shouldSetAndRetrieveValues() {
        // Given
        AssistanceRequest request = new AssistanceRequest();

        // When
        request.setPassId(PASS_ID);
        request.setConferenciaId(CONFERENCIA_ID);

        // Then
        assertEquals(PASS_ID, request.getPassId(), "Getter/Setter de Pass ID falló.");
        assertEquals(CONFERENCIA_ID, request.getConferenciaId(), "Getter/Setter de Conferencia ID fallo.");
        assertNotNull(request, "El objeto no debe ser nulo.");
    }

    @Test
    void validation_shouldPassWhenAllFieldsArePresent() {
        // Given
        AssistanceRequest request = new AssistanceRequest(PASS_ID, CONFERENCIA_ID);

        // When
        Set<ConstraintViolation<AssistanceRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "No debe haber violaciones si ambos campos son validos.");
    }

    @Test
    void validation_shouldFailWhenPassIdIsNull() {
        // Given
        AssistanceRequest request = new AssistanceRequest(null, CONFERENCIA_ID);

        // When
        Set<ConstraintViolation<AssistanceRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Debe haber una violacion para Pass ID nulo.");
        assertEquals(1, violations.size());

        ConstraintViolation<AssistanceRequest> violation = violations.iterator().next();
        assertEquals("Pass ID es requerido", violation.getMessage());
        assertEquals("passId", violation.getPropertyPath().toString());
    }

    @Test
    void validation_shouldFailWhenConferenciaIdIsNull() {
        // Given
        AssistanceRequest request = new AssistanceRequest(PASS_ID, null);

        // When
        Set<ConstraintViolation<AssistanceRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Debe haber una violacion para Conferencia ID nulo.");
        assertEquals(1, violations.size());

        ConstraintViolation<AssistanceRequest> violation = violations.iterator().next();
        assertEquals("Conferencia ID es requerido", violation.getMessage());
        assertEquals("conferenciaId", violation.getPropertyPath().toString());
    }

    @Test
    void validation_shouldFailWhenBothFieldsAreNull() {
        // Given
        AssistanceRequest request = new AssistanceRequest(null, null);

        // When
        Set<ConstraintViolation<AssistanceRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Debe haber dos violaciones.");
        assertEquals(2, violations.size());
    }
}






