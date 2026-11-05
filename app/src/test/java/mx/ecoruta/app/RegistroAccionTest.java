package mx.ecoruta.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import javax.crypto.KeyGenerator;

/**
 * Prueba de integración: valida, limpia, cifra y arma los datos que se mandan a la nube,
 * igual que lo hace la pantalla "Registrar acción".
 */
public class RegistroAccionTest {

    @Test
    public void laNotaViajaCifradaYSeRecupera() throws Exception {
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(256);
        Cifrador cifrador = new Cifrador(gen.generateKey());

        assertNull(Validador.cantidad("20"));
        String nota = Validador.limpiar("<b>Cerré la llave</b>");
        Accion accion = new Accion(null, TipoAccion.AGUA, 20, 1_700_000_000_000L, cifrador.cifrar(nota));
        Map<String, Object> enviado = accion.aMapa();

        assertEquals("AGUA", enviado.get("tipo"));
        assertEquals(20, enviado.get("cantidad"));
        assertFalse(enviado.get("notaCifrada").toString().contains("llave"));
        assertEquals("bCerré la llave/b", cifrador.descifrar((String) enviado.get("notaCifrada")));
        assertEquals(20, accion.puntos());
    }

    @Test
    public void sinNotaNoSeMandaElCampo() {
        Accion a = new Accion(null, TipoAccion.ENERGIA, 2, 0, null);
        assertFalse(a.aMapa().containsKey("notaCifrada"));
    }

    @Test
    public void sumaPuntosDeVariasAcciones() {
        int total = Accion.totalPuntos(Arrays.asList(
                new Accion("a", TipoAccion.RECICLAJE, 3, 0, null),       // 30
                new Accion("b", TipoAccion.REFORESTACION, 1, 0, null),   // 50
                new Accion("c", TipoAccion.TRANSPORTE, 5, 0, null)));    // 10
        assertEquals(90, total);
    }

    @Test
    public void tipoDesconocidoNoSeAcepta() {
        assertNull(TipoAccion.desdeNombre("HACKEO"));
        assertTrue(TipoAccion.desdeNombre("AGUA") == TipoAccion.AGUA);
    }
}
