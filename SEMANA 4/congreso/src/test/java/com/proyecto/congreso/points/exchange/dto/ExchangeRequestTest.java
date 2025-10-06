package com.proyecto.congreso.points.exchange.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRequestTest {
    private static final Long PASS_ID = 101L;
    private static final String FREEBIE_ID = "F005";

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        // Inicializa el validador para simular la validación de Spring (@Valid)
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ----------- TITULOS DE TESTS DESCRIPTIVOS -----------
    @Test
    void allArgsConstructor_shouldCreateValidObject() {
        // Given
        ExchangeRequest request = new ExchangeRequest(PASS_ID, FREEBIE_ID);

        // Then
        assertNotNull(request);
        assertEquals(PASS_ID, request.getPassId());
        assertEquals(FREEBIE_ID, request.getFreebieId());
    }

    @Test
    void noArgsConstructor_andSetters_shouldWorkCorrectly() {
        // Given
        ExchangeRequest request = new ExchangeRequest();

        // When
        request.setPassId(PASS_ID);
        request.setFreebieId(FREEBIE_ID);

        // Then
        assertEquals(PASS_ID, request.getPassId());
        assertEquals(FREEBIE_ID, request.getFreebieId());
    }


    @Test
    void validation_shouldPass_whenAllFieldsArePresent() {
        // Given
        ExchangeRequest request = new ExchangeRequest(PASS_ID, FREEBIE_ID);

        // When
        Set<?> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validacion.");
    }

    @Test
    void validation_shouldFail_whenPassIdIsNull() {
        // Given
        ExchangeRequest request = new ExchangeRequest(null, FREEBIE_ID);

        // When
        Set<?> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Debe fallar si Pass ID es nulo.");
        assertEquals(1, violations.size(), "Debe haber exactamente 1 violacion.");
    }

    @Test
    void validation_shouldFail_whenFreebieIdIsNull() {
        // Given
        ExchangeRequest request = new ExchangeRequest(PASS_ID, null);

        // When
        Set<?> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Debe fallar si Freebie ID es nulo.");
        assertEquals(1, violations.size(), "Debe haber exactamente 1 violacion.");
    }

    @Test
    void validation_shouldFail_whenBothFieldsAreNull() {
        // Given
        ExchangeRequest request = new ExchangeRequest(null, null);

        // When
        Set<?> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Debe fallar si ambos campos son nulos.");
        assertEquals(2, violations.size(), "Debe haber exactamente 2 violaciones.");
    }
}
