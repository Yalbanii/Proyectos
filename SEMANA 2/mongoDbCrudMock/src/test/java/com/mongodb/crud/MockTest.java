package com.mongodb.crud;

import com.mongo.crud.model.Jugador;
import com.mongo.crud.repository.JugadoresRepository;
import com.mongo.crud.service.JugadorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

//SpringBootTest(classes= MvcTestingExampleApplication.class)
@ExtendWith(MockitoExtension.class) //Para iniciar las pruebas unitarias
public class MockTest {

    @Autowired
    ApplicationContext context;

    @Mock
    private JugadoresRepository jugadoresRepository; //Nuestro Repositorio
//No queremos que llame realmente a crear Jugador

    @InjectMocks //Tiene como dependencia los MocKs que ya implementamos
    private JugadorServiceImpl jugadorService;
    //instancia el mock


    @Test
    void testNull() {
        Jugador jugador = new Jugador("13", "Robert Lewandowski", 37, "polaco", "FCB", 27);

        String str1 = null;
        String str2 = "Robert Lewandowski";

        assertNull(jugador.checkNull(str1), "Objeto debería ser null");
    }

    @Test
    void testNotNull() {
        Jugador jugador = new Jugador("13", "Robert Lewandowski", 37, "polaco", "FCB", 27);

        String str1 = null;
        String str2 = "Robert Lewandowski";

        assertNotNull(jugador.checkNull(str2), "Object should not be null");

    }

    @DisplayName("Crear Jugador")
    @Test
    public void usuarioPrueba() {
        Jugador esperado = new Jugador("13", "Robert Lewandowski", 37, "polaco", "FCB", 27);
        Mockito.when(jugadoresRepository.save(esperado)).thenReturn(esperado);
        final Jugador resultado = jugadoresRepository.save(esperado);
        Assertions.assertEquals(esperado.getId(), resultado.getId());
        Assertions.assertEquals(esperado, resultado);
    }


    @DisplayName("Esperamos que ese jugador no exista")
    @Test
    public void testNotExists() {
        JugadorServiceImpl jugadoresService = new JugadorServiceImpl();
        final Optional<Jugador> resultado = jugadoresRepository.findById(Mockito.anyString());
        Assertions.assertNotEquals(null, resultado, "Ese jugador no existe");
    }

    @DisplayName("Esperamos que el jugador se pueda encontrar con ID")
    @Test
    public void testExists() {
        Jugador esperado = new Jugador("13", "Robert Lewandowski", 37, "polaco", "FCB", 27);
        Mockito.when(jugadoresRepository.save(esperado)).thenReturn(esperado);
        final Jugador resultado = jugadoresRepository.save(esperado);
        Assertions.assertEquals(esperado.getId(), resultado.getId());
        Mockito.verify(jugadoresRepository).save(resultado);
    }

    @DisplayName("Esperamos que el jugador con ID 13 si exista")
    @Test
    public void testFind() {
        Optional<Jugador> mockitoSimulado = Optional.of(new Jugador("13", "Robert Lewandowski", 37, "polaco", "FCB", 27));
        Optional<Jugador> esperado = Optional.of(new Jugador("13", "Robert Lewandowski", 37, "polaco", "FCB", 27));

        Mockito.when(jugadoresRepository.findById("13")).thenReturn(mockitoSimulado);
        final Optional<Jugador> resultado = jugadoresRepository.findById("13");
        Assertions.assertEquals(esperado, resultado);
        Mockito.verify(jugadoresRepository).findById("13");

    }

    @Test
    public void testSave(){
        Jugador esperado = new Jugador("13", "Robert Lewandowski", 37, "polaco", "FCB", 27);
        assertNotNull(esperado);

    }
    @DisplayName("Esperamos que el jugador se pueda actualizar")
    @Test
    public void testUpdate() {
        Jugador esperado = new Jugador("13", "Robert Lewandowski", 37, "polaco", "FCB", 27);
        Optional<Jugador> existingJugador = jugadoresRepository.findById(esperado.getId());
        if (existingJugador.isPresent()) {
            Jugador jugadorToUpdate = existingJugador.get();
            jugadorToUpdate.setNombre(esperado.getNombre());
            jugadorToUpdate.setEdad(esperado.getEdad());
            jugadorToUpdate.setNacionalidad(esperado.getNacionalidad());
            jugadorToUpdate.setClub(esperado.getClub());
            jugadorToUpdate.setGolAnio(esperado.getGolAnio());
            Mockito.when(jugadoresRepository.save(jugadorToUpdate)).thenReturn(esperado);
            Assertions.assertNotEquals(esperado, jugadorToUpdate);
        }

    }

    @DisplayName("Esperamos que no se puedan obtener todos los jugadores.")
    @Test
    public void testFindAll() {
        List<Jugador> resultado = jugadorService.findAll();
        Assertions.assertNotEquals(null, resultado, "No hay jugadores");
    }

    @DisplayName("Esperamos que se puedan obtener todos los jugadores.")
    @Test
    public void testFindAllOk() {
        List<Jugador> resultado = jugadorService.findAll();
        Assertions.assertTrue(resultado != null, "Si hay jugadores");
    }

}

















