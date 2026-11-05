package mx.ecoruta.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/** Pruebas unitarias: revisan que la app no acepte datos malos. */
public class ValidadorTest {

    @Test
    public void aceptaCorreosValidos() {
        assertNull(Validador.correo("ana.lopez@escuela.edu.mx"));
    }

    @Test
    public void rechazaCorreosMalos() {
        assertNotNull(Validador.correo(""));
        assertNotNull(Validador.correo("ana@"));
        assertNotNull(Validador.correo("ana escuela.mx"));
        assertNotNull(Validador.correo(null));
    }

    @Test
    public void contrasenaSegura() {
        assertNotNull(Validador.contrasena("Corta1"));
        assertNotNull(Validador.contrasena("sinmayuscula1"));
        assertNotNull(Validador.contrasena("SINMINUSCULA1"));
        assertNotNull(Validador.contrasena("SinNumeros"));
        assertNull(Validador.contrasena("EcoRuta2026"));
    }

    @Test
    public void cantidadEntre1y1000() {
        assertNull(Validador.cantidad("5"));
        assertNotNull(Validador.cantidad("0"));
        assertNotNull(Validador.cantidad("1001"));
        assertNotNull(Validador.cantidad("abc"));
        assertNotNull(Validador.cantidad(""));
    }

    @Test
    public void limpiaEtiquetasYRecorta() {
        assertEquals("scripthola/script", Validador.limpiar(" <script>hola</script>\n"));
        StringBuilder largo = new StringBuilder();
        for (int i = 0; i < 500; i++) largo.append('a');
        assertEquals(Validador.MAX_NOTA, Validador.limpiar(largo.toString()).length());
    }
}
