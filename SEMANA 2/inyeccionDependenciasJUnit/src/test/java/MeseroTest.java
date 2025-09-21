import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.variable.Mesero;
import org.variable.MeseroServicio;

import static org.junit.jupiter.api.Assertions.*;

public class MeseroTest {


	@Test
	void testNullAndNotNull() {

        // set up
        Mesero mesero = new Mesero(1L, "Juan");

		String str1 = null;
		String str2 = "Juan";

		assertNull(mesero.checkNull(str1), "Objeto debería ser null");
		assertNotNull(mesero.checkNull(str2), "Object should not be null");

	}
    @DisplayName("Esperamos que el mesero este creado")
    @Test
    public void testEquals(){
        Mesero esperado = new Mesero(2L, "Prueba");
        MeseroServicio meseroServicio = new MeseroServicio();
        final Mesero resultado = meseroServicio.crearMesero(2L, "Prueba");
        Assertions.assertEquals(esperado, resultado, "Los nombres son iguales");
        //Assertions.assertTrue(true);
    //    Assertions.assertFalse(false);

    }
    @DisplayName("Esperamos que el mesero no este creado")
    @Test
    public void testNotEquals(){
        Mesero esperado = new Mesero(1L, "Juan");
        MeseroServicio meseroServicio = new MeseroServicio();
        final Mesero resultado = meseroServicio.crearMesero(2L, "Prueba");
        Assertions.assertNotEquals(esperado, resultado, "Los nombres son diferentes");

    }

    @DisplayName("Esperamos que ese mesero no exista")
    @Test
    public void testNotExists(){
        MeseroServicio meseroServicio = new MeseroServicio();
        final Mesero resultado = meseroServicio.obtenerMesero(1L);
        Assertions.assertEquals(null, resultado, "Ese mesero no existe");

    }
    @DisplayName("Esperamos que el mesero con ID 1 si exista")
    @Test
    public void testExists(){
        Mesero esperado = new Mesero(1L, "Juan");
        MeseroServicio meseroServicio = new MeseroServicio();
        meseroServicio.crearMesero(1L, "Juan");
        final Mesero resultado = meseroServicio.obtenerMesero(1L);
        Assertions.assertEquals(esperado, resultado, "Ese mesero si existe.");

    }

}
