package mx.ecoruta.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/** Revisa la lista de municipios que usa la pantalla de calidad del aire. */
public class MunicipiosMichoacanTest {

    @Test
    public void sonLos113MunicipiosSinRepetir() {
        assertEquals(113, MunicipiosMichoacan.TODOS.length);
        Set<String> nombres = new HashSet<>();
        for (MunicipiosMichoacan.Municipio m : MunicipiosMichoacan.TODOS) nombres.add(m.nombre);
        assertEquals(113, nombres.size());
    }

    @Test
    public void todasLasCoordenadasEstanEnMichoacan() {
        for (MunicipiosMichoacan.Municipio m : MunicipiosMichoacan.TODOS) {
            assertTrue(m.nombre, m.latitud > 17.8 && m.latitud < 20.5);
            assertTrue(m.nombre, m.longitud > -103.9 && m.longitud < -100.0);
            // Cada municipio arma una dirección válida para la API
            assertTrue(CalidadAire.url(m.latitud, m.longitud).startsWith("https://"));
        }
    }

    @Test
    public void moreliaAparecePrimero() {
        assertEquals("Morelia", MunicipiosMichoacan.TODOS[MunicipiosMichoacan.posicionDe("Morelia")].nombre);
    }
}
