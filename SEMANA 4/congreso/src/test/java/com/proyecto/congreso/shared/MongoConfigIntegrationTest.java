package com.proyecto.congreso.shared;

import com.proyecto.congreso.points.exchange.repository.ExchangeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

// @SpringBootTest para cargar el contexto completo de Spring.
@SpringBootTest
@ActiveProfiles("test-mongo")
public class MongoConfigIntegrationTest {

    // Inyectar una dependencia clave para verificar que la configuracion se cargo
    @Autowired
    private MongoTemplate mongoTemplate;

    // Inyectar un repositorio que esta listado en la configuracion
    @Autowired
    private ExchangeRepository exchangeRepository;

     //Confirma que el MongoTemplate se ha inicializado correctamente.
    @Test
    void mongoTemplate_shouldBeInitialized() {
        assertNotNull(mongoTemplate, "El MongoTemplate debe estar disponible en el contexto de Spring.");
    }

    @Test
    void mongoRepositories_shouldBeScannedAndInjected() {
        assertNotNull(exchangeRepository, "El repositorio de MongoDB debe haber sido encontrado e inyectado.");
    }
}