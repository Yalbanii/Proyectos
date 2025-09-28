package com.mongodb.crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongo.crud.model.Jugador;
import com.mongo.crud.repository.JugadoresRepository;
import com.mongo.crud.rest.JugadorRest;
import com.mongo.crud.service.JugadorServiceImpl;
import com.mongo.crud.service.JugadoresService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @ExtendWith(MockitoExtension.class)
    public class MockRestTest {

        @Mock
        private JugadoresRepository jugadoresRepository;

        @InjectMocks
        private JugadorServiceImpl jugadorService;


        @Mock
        private JugadoresService jugadoresService;

        @InjectMocks
        private JugadorRest jugadorRest;

        private MockMvc mockMvc;
        private ObjectMapper objectMapper;
        private Jugador jugador;
        private Jugador otroJugador;

        @BeforeEach
        void setUp() {
            mockMvc = MockMvcBuilders.standaloneSetup(jugadorRest).build(); //Solo crea al jugadorRest
            objectMapper = new ObjectMapper();
            jugador = new Jugador("53", "Lionel Messi", 37, "Argentina", "Inter Miami", 20);
            otroJugador = new Jugador("17", "Cristiano Ronaldo", 39, "Portugal", "Al-Nassr", 25);
        }

        @DisplayName("Mock de crear Jugador")
        @Test
        void testCreateJugador() throws Exception {
            //Como debe comportarse el mock de Service
            when(jugadoresService.save(any(Jugador.class))).thenReturn(jugador);

            //Simula peticion de HTTP
            mockMvc.perform(post("/api/jugadores") //Mock de peticiones HTTP
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(jugador)))
                    .andExpect(status().isOk()) //Estado de respuesta
                    .andExpect(jsonPath("$.nombre").value("Lionel Messi")); //En cuerpo de JSON
        }

        @DisplayName("Mock de obtener todos los Jugadores")
        @Test
        void testGetAllJugadores() throws Exception {
            List<Jugador> jugadoresList = Arrays.asList(jugador, otroJugador);
            when(jugadoresService.findAll()).thenReturn(jugadoresList);

            mockMvc.perform(get("/api/jugadores"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nombre").value("Lionel Messi"))
                    .andExpect(jsonPath("$[1].nombre").value("Cristiano Ronaldo"));
        }

        @DisplayName("Mock de obtener Jugadores por ID")
        @Test
        void testGetJugadorByIdExistente() throws Exception {
            when(jugadoresService.findById("53")).thenReturn(Optional.of(jugador));

            mockMvc.perform(get("/api/jugadores/53"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Lionel Messi"));
        }

        @DisplayName("Mock de obtener Jugadores por ID que no exista")
        @Test
        void testGetJugadorByIdNoExistente() throws Exception {
            // Cuando se busque por un ID que no existe, el servicio lanzará la excepción
            when(jugadoresService.findById("3")).thenThrow(new IllegalArgumentException("El jugador con el id 3 no existe."));

            // Ahora, en lugar de esperar 200 OK, esperamos un 404 NOT FOUND
            mockMvc.perform(get("/api/jugadores/3"))
                    .andExpect(status().isNotFound()); // Corregido para esperar un 404
        }

        @DisplayName("Mock de actualizar Jugador")
        @Test
        void testUpdateJugadorExitoso() throws Exception {
            Jugador jugadorActualizado = new Jugador("53", "Lionel Andrés Messi", 37, "Argentina", "Inter Miami", 20);
            when(jugadoresService.update("53", jugador)).thenReturn(jugadorActualizado);

            mockMvc.perform(put("/api/jugadores/53")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(jugador)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Lionel Andrés Messi"));
        }

        @DisplayName("Mock de no poder actualizar Jugador")
        @Test
        void testUpdateJugadorNoExistente() throws Exception {
            when(jugadoresService.update(anyString(), any(Jugador.class))).thenThrow(new RuntimeException());

            mockMvc.perform(put("/api/jugadores/3")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new Jugador())))
                    .andExpect(status().isNotFound());
        }

        @DisplayName("Mock de eliminar Jugador")
        @Test
        void testDeleteJugadorExitoso() throws Exception {
            when(jugadoresService.deleteById("53")).thenReturn(jugador);

            mockMvc.perform(delete("/api/jugadores/53"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Lionel Messi"));
        }

        @DisplayName("Mock de no poder eliminar Jugador")
        @Test
        void testDeleteJugadorNoExistente() throws Exception {
            when(jugadoresService.deleteById("3")).thenReturn(null);

            mockMvc.perform(delete("/api/jugadores/3"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(""));
        }
    }