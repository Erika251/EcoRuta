package mx.ecoruta.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.junit.Test;

/** Pruebas unitarias de la lectura de la API de calidad del aire. */
public class CalidadAireTest {

    @Test
    public void leeLaRespuestaDeLaApi() throws Exception {
        CalidadAire a = CalidadAire.desdeJson("{\"current\":{\"european_aqi\":45,\"pm2_5\":18.3}}");
        assertEquals(45, a.indice);
        assertEquals(18.3, a.pm25, 0.001);
        assertEquals("Moderada", a.nivel());
    }

    @Test
    public void nivelesDelIndice() {
        assertEquals("Buena", new CalidadAire(10, 0).nivel());
        assertEquals("Aceptable", new CalidadAire(30, 0).nivel());
        assertEquals("Mala", new CalidadAire(70, 0).nivel());
        assertEquals("Muy mala", new CalidadAire(95, 0).nivel());
    }

    @Test(expected = JSONException.class)
    public void fallaConRespuestaRara() throws Exception {
        CalidadAire.desdeJson("[]");
    }

    @Test
    public void laDireccionUsaHttps() {
        assertTrue(CalidadAire.url(19.4326, -99.1332).startsWith("https://"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaCoordenadasImposibles() {
        CalidadAire.url(100, 0);
    }
}
