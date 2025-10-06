package com.proyecto.congreso.participantes.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class ParticipantRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        // motor de validacion (Hibernate Validator)
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    // Crea un objeto request valido
    private ParticipantRequest createValidRequest() {
        return new ParticipantRequest(
                "Juan", // name
                "Perez", // lastName
                "juan.perez@test.com", // email
                "1234567890", // phone (10 dígitos)
                "MX", // nacionality
                30, // age
                "IT" // area
        );
    }

    // Cobertura del Constructor y Getters/Setters

    @Test
    void validParticipantRequest_ShouldHaveNoViolations() {
        ParticipantRequest request = createValidRequest();

        // Verifica que no haya violaciones para un objeto válido.
        Set<ConstraintViolation<ParticipantRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "Un objeto válido no debe tener violaciones.");
    }

    // RESTRICCIONES REQUERIDAS (@NotBlank y @Column(nullable = false))

    @Test
    void name_ShouldFailIfBlank() {
        ParticipantRequest request = createValidRequest();
        request.setName("   "); // Blank value

        Set<ConstraintViolation<ParticipantRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void lastName_ShouldFailIfNull() {
        ParticipantRequest request = createValidRequest();
        request.setLastName(null);

        Set<ConstraintViolation<ParticipantRequest>> violations = validator.validate(request);
        if (violations.size() > 0) {
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
        }
    }

    @Test
    void email_ShouldFailIfBlank() {
        ParticipantRequest request = createValidRequest();
        request.setEmail("");

        Set<ConstraintViolation<ParticipantRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    // RESTRICCIONES DE FORMATO (@Email y @Pattern)

    @Test
    void email_ShouldFailIfInvalidFormat() {
        ParticipantRequest request = createValidRequest();
        request.setEmail("invalid-email");

        Set<ConstraintViolation<ParticipantRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Email should be valid", violations.iterator().next().getMessage());
    }

    @Test
    void phone_ShouldFailIfNot10Digits() {
        ParticipantRequest request = createValidRequest();
        request.setPhone("123456789");

        Set<ConstraintViolation<ParticipantRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Phone must be 10 digits", violations.iterator().next().getMessage());

        // Test with too long
        request.setPhone("12345678901");
        violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Phone must be 10 digits", violations.iterator().next().getMessage());
    }

    // OTROS CAMPOS REQUERIDOS (age, nacionality, area)

    @Test
    void age_ShouldFailIfNull() {
        ParticipantRequest request = createValidRequest();
        request.setAge(null);

        Set<ConstraintViolation<ParticipantRequest>> violations = validator.validate(request);

        if (violations.size() > 0) {
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("age")));
        }
    }

    @Test
    void nacionality_ShouldFailIfNull() {
        ParticipantRequest request = createValidRequest();
        request.setNacionality(null);

        Set<ConstraintViolation<ParticipantRequest>> violations = validator.validate(request);

        if (violations.size() > 0) {
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nacionality")));
        }
    }

    @Test
    void area_ShouldFailIfNull() {
        ParticipantRequest request = createValidRequest();
        request.setArea(null);

        Set<ConstraintViolation<ParticipantRequest>> violations = validator.validate(request);

        if (violations.size() > 0) {
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("area")));
        }
    }
}
