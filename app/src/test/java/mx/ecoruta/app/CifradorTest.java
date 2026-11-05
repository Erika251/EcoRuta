package mx.ecoruta.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.security.GeneralSecurityException;

import javax.crypto.KeyGenerator;

/** Pruebas unitarias del cifrado AES-256-GCM de las notas. */
public class CifradorTest {

    private Cifrador cifrador;

    @Before
    public void crearLlave() throws Exception {
        // En la prueba la llave se crea en memoria; en el teléfono vive en el Android Keystore
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(256);
        cifrador = new Cifrador(gen.generateKey());
    }

    @Test
    public void cifraYDescifraLaMismaNota() throws Exception {
        String nota = "Llevé 3 kg de PET al centro de acopio";
        String cifrada = cifrador.cifrar(nota);
        assertFalse(cifrada.contains("PET"));
        assertEquals(nota, cifrador.descifrar(cifrada));
    }

    @Test
    public void cadaCifradoEsDistinto() throws Exception {
        assertNotEquals(cifrador.cifrar("hola"), cifrador.cifrar("hola"));
    }

    @Test
    public void detectaTextoAlterado() throws Exception {
        String cifrada = cifrador.cifrar("original");
        String alterada = cifrada.substring(0, cifrada.length() - 4) + "AAAA";
        try {
            cifrador.descifrar(alterada);
            fail("Debió detectar que el texto fue alterado");
        } catch (GeneralSecurityException esperado) {
            // correcto
        }
    }

    @Test(expected = GeneralSecurityException.class)
    public void rechazaBasura() throws Exception {
        cifrador.descifrar("esto no es base64 !!!");
    }
}
