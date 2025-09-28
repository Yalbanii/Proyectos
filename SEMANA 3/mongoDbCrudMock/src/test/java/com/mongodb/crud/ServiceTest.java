package com.mongodb.crud;
import com.mongo.crud.model.Jugador;
import com.mongo.crud.repository.JugadoresRepository;
import com.mongo.crud.service.JugadorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
    public class ServiceTest {
    @Mock
    private JugadoresRepository jugadoresRepository;

    @InjectMocks
    private JugadorServiceImpl jugadorService;

    private Jugador jugador;

    @BeforeEach
    void setUp() {
        jugador = new Jugador("53", "Lionel Messi", 36, "Argentina", "Inter Miami", 20);
    }

    @DisplayName("Mock de poder crear al Jugador")
    @Test
    void testSaveJugador() {
        when(jugadoresRepository.save(any(Jugador.class))).thenReturn(jugador);

        Jugador savedJugador = jugadorService.save(jugador);

        assertNotNull(savedJugador);
        assertEquals("Lionel Messi", savedJugador.getNombre());
        verify(jugadoresRepository, times(1)).save(jugador);
    }

    @DisplayName("Mock de poder encontrar al Jugador")
    @Test
    void testFindAll() {
        when(jugadoresRepository.findAll()).thenReturn(Collections.singletonList(jugador));

        List<Jugador> jugadores = jugadorService.findAll();

        assertNotNull(jugadores);
        assertEquals(1, jugadores.size());
        assertEquals("Lionel Messi", jugadores.get(0).getNombre());
        verify(jugadoresRepository, times(1)).findAll();
    }

    @DisplayName("Mock de poder encontrar al Jugador por su ID")
    @Test
    void testFindByIdWhenJugadorExists() {
        when(jugadoresRepository.findById("53")).thenReturn(Optional.of(jugador));

        Optional<Jugador> foundJugador = jugadorService.findById("53");

        assertTrue(foundJugador.isPresent());
        assertEquals("Lionel Messi", foundJugador.get().getNombre());
        verify(jugadoresRepository, times(1)).findById("53");
    }

    @DisplayName("Mock de no poder encontrar al Jugador")
    @Test
    void testFindByIdWhenJugadorDoesNotExist() {
        when(jugadoresRepository.findById("2")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> jugadorService.findById("2"));
        verify(jugadoresRepository, times(1)).findById("2");
    }

    @DisplayName("Mock de poder actualizar al Jugador")
    @Test
    void testUpdateWhenJugadorExists() {
        Jugador jugadorUpdateData = new Jugador("53", "Leo Messi", 37, "Argentina", "FC Barcelona", 20);
        when(jugadoresRepository.findById("53")).thenReturn(Optional.of(jugador));
        when(jugadoresRepository.save(any(Jugador.class))).thenReturn(jugadorUpdateData);

        Jugador updatedJugador = jugadorService.update("53", jugadorUpdateData);

        assertNotNull(updatedJugador);
        assertEquals("Leo Messi", updatedJugador.getNombre());
        assertEquals(37, updatedJugador.getEdad());
        verify(jugadoresRepository, times(1)).findById("53");
        verify(jugadoresRepository, times(1)).save(any(Jugador.class));
    }

    @DisplayName("Mock de no poder actualizar al Jugador")
    @Test
    void testUpdateWhenJugadorDoesNotExist() {
        when(jugadoresRepository.findById("2")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> jugadorService.update("2", jugador));
        verify(jugadoresRepository, times(1)).findById("2");
        verify(jugadoresRepository, never()).save(any(Jugador.class));
    }

    @DisplayName("Mock de no poder eliminar al Jugador")
    @Test
    void testDeleteByIdWhenJugadorExists() {
        when(jugadoresRepository.findById("53")).thenReturn(Optional.of(jugador));
        doNothing().when(jugadoresRepository).deleteById("53");

        Jugador deletedJugador = jugadorService.deleteById("53");

        assertNotNull(deletedJugador);
        assertEquals("Lionel Messi", deletedJugador.getNombre());
        verify(jugadoresRepository, times(1)).findById("53");
        verify(jugadoresRepository, times(1)).deleteById("53");
    }

    @DisplayName("Mock de poder eliminar al Jugador")
    @Test
    void testDeleteByIdWhenJugadorDoesNotExist() {
        when(jugadoresRepository.findById("2")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> jugadorService.deleteById("2"));
        verify(jugadoresRepository, times(1)).findById("2");
        verify(jugadoresRepository, never()).deleteById(anyString());
    }

    @DisplayName("Mock para el método que siempre devuelve 'false'")
    @Test
    void testExistsByName() {
        assertFalse(jugadorService.existsByName("Lionel Messi"));
    }
}