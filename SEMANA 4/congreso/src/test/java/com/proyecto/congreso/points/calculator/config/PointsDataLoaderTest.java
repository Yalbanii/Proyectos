package com.proyecto.congreso.points.calculator.config;

import com.proyecto.congreso.points.calculator.model.FreebiePointsData;
import com.proyecto.congreso.points.calculator.events.ConferenceDataImportedEvent;
import com.proyecto.congreso.points.calculator.events.FreebieDataImportedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PointsDataLoaderTest {

    private static final int EXPECTED_FREEBIE_COUNT = 10;
    private static final int EXPECTED_CONFERENCE_COUNT = 43;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private PointsDataLoader pointsDataLoader;

    // ----------- TITULOS DE TESTS DESCRIPTIVOS -----------
    @Test
    void run_shouldPublishCorrectNumberOfFreebieEvents() throws Exception {
        // When
        pointsDataLoader.run();

        // Then
        verify(events, times(EXPECTED_FREEBIE_COUNT)).publishEvent(any(FreebieDataImportedEvent.class));
    }

    @Test
    void run_shouldPublishCorrectNumberOfConferenceEvents() throws Exception {
        // When
        pointsDataLoader.run();

        // Then
        verify(events, times(EXPECTED_CONFERENCE_COUNT)).publishEvent(any(ConferenceDataImportedEvent.class));
    }

    @Test
    void run_shouldPublishCorrectDataForFirstFreebie() throws Exception {
        // Given
        ArgumentCaptor<FreebieDataImportedEvent> captor = ArgumentCaptor.forClass(FreebieDataImportedEvent.class);

        // When
        pointsDataLoader.run();

        // Then
        verify(events, times(EXPECTED_FREEBIE_COUNT)).publishEvent(captor.capture());
        List<FreebieDataImportedEvent> publishedEvents = captor.getAllValues();

        FreebiePointsData firstData = publishedEvents.get(0).data();

        assertEquals("1", firstData.getFreebieId());
        assertEquals("Libreta Reciclada", firstData.getArticulo());
        assertEquals(10, firstData.getCosto());
    }

}

