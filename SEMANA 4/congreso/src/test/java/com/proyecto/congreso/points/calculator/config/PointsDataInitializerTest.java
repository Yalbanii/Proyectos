package com.proyecto.congreso.points.calculator.config;

import com.proyecto.congreso.points.calculator.dto.ConferencePointsData;
import com.proyecto.congreso.points.calculator.model.Conferencia;
import com.proyecto.congreso.points.calculator.model.FreebiePointsData;
import com.proyecto.congreso.points.calculator.model.Freebies;
import com.proyecto.congreso.points.calculator.repository.ConferenceRepository;
import com.proyecto.congreso.points.calculator.repository.FreebieRepository;
import com.proyecto.congreso.points.calculator.events.ConferenceDataImportedEvent;
import com.proyecto.congreso.points.calculator.events.FreebieDataImportedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PointsDataInitializerTest {
    // Simula las dependencias de los repositorios
    @Mock
    private FreebieRepository freebieRepository;
    @Mock
    private ConferenceRepository conferenceRepository;

    // Inyecta los mocks en la instancia real del servicio
    @InjectMocks
    private PointsDataInitializer pointsDataInitializer;

    private final String FREEBIE_ID = "10";
    private final String CONFERENCE_ID = "5";

    private FreebiePointsData crearDatosFreebie() {
        return new FreebiePointsData(
                FREEBIE_ID, "Taza", "Taza de cerámica", 50, 10, 50
        );
    }

    private ConferencePointsData crearDatosConferencia() {
        return new ConferencePointsData(
                CONFERENCE_ID, "Lunes", "2025-10-20", "10:00", "11:00", "60 min", "Sala A",
                "Plenaria", "Introducción a Modulith", "Dr. Pérez", 15
        );
    }

    // ----------- TITULOS DE TESTS DESCRIPTIVOS -----------
    @Test
    void handleFreebieDataImport_DebeGuardar_SiElFreebieNoExiste() {
        // Arrange
        FreebiePointsData datosFreebie = crearDatosFreebie();
        FreebieDataImportedEvent evento = new FreebieDataImportedEvent(datosFreebie);

        when(freebieRepository.findById(FREEBIE_ID)).thenReturn(Optional.empty());
        when(freebieRepository.save(any(Freebies.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        pointsDataInitializer.handleFreebieDataImport(evento);

        // Assert
        verify(freebieRepository, times(1)).save(any(Freebies.class));
        verify(freebieRepository, times(1)).findById(FREEBIE_ID);
    }

    @Test
    void handleFreebieDataImport_DebeSaltarYNoGuardar_SiElFreebieYaExiste() {
        FreebiePointsData datosFreebie = crearDatosFreebie();
        FreebieDataImportedEvent evento = new FreebieDataImportedEvent(datosFreebie);
        Freebies freebieExistente = new Freebies();

        when(freebieRepository.findById(FREEBIE_ID)).thenReturn(Optional.of(freebieExistente));

        // Act
        pointsDataInitializer.handleFreebieDataImport(evento);

        // Assert
        verify(freebieRepository, times(1)).findById(FREEBIE_ID);
        verify(freebieRepository, never()).save(any(Freebies.class));
    }


    @Test
    void handleConferenceDataImport_DebeGuardar_SiLaConferenciaNoExiste() {
        // Arrange
        ConferencePointsData datosConferencia = crearDatosConferencia();
        ConferenceDataImportedEvent evento = new ConferenceDataImportedEvent(datosConferencia);

        // Mocks: findById devuelve vacío (no existe)
        when(conferenceRepository.findById(CONFERENCE_ID)).thenReturn(Optional.empty());
        when(conferenceRepository.save(any(Conferencia.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        pointsDataInitializer.handleConferenceDataImport(evento);

        // Assert
        verify(conferenceRepository, times(1)).save(any(Conferencia.class));
        verify(conferenceRepository, times(1)).findById(CONFERENCE_ID);
    }

    @Test
    void handleConferenceDataImport_DebeSaltarYNoGuardar_SiLaConferenciaYaExiste() {
        ConferencePointsData datosConferencia = crearDatosConferencia();
        ConferenceDataImportedEvent evento = new ConferenceDataImportedEvent(datosConferencia);
        Conferencia conferenciaExistente = new Conferencia();

        when(conferenceRepository.findById(CONFERENCE_ID)).thenReturn(Optional.of(conferenciaExistente));

        pointsDataInitializer.handleConferenceDataImport(evento);

        verify(conferenceRepository, times(1)).findById(CONFERENCE_ID);
        verify(conferenceRepository, never()).save(any(Conferencia.class));
    }
}

