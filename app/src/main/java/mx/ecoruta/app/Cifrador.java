package mx.ecoruta.app;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Cifra las notas personales con AES-256-GCM antes de subirlas a la nube.
 * En Firebase solo queda texto revuelto: base64(nonce + texto cifrado + etiqueta).
 * Si alguien cambia el texto cifrado, descifrar() falla (la etiqueta lo detecta).
 */
public class Cifrador {

    private static final int NONCE = 12;
    private static final int ETIQUETA_BITS = 128;

    private final SecretKey llave;
    private final SecureRandom azar = new SecureRandom();

    public Cifrador(SecretKey llave) {
        this.llave = llave;
    }

    public String cifrar(String texto) throws GeneralSecurityException {
        byte[] nonce = new byte[NONCE];
        azar.nextBytes(nonce);
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, llave, new GCMParameterSpec(ETIQUETA_BITS, nonce));
        byte[] cifrado = c.doFinal(texto.getBytes(StandardCharsets.UTF_8));
        ByteBuffer todo = ByteBuffer.allocate(NONCE + cifrado.length).put(nonce).put(cifrado);
        return Base64.getEncoder().encodeToString(todo.array());
    }

    public String descifrar(String base64) throws GeneralSecurityException {
        byte[] todo;
        try {
            todo = Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException e) {
            throw new GeneralSecurityException("Texto cifrado no válido", e);
        }
        if (todo.length <= NONCE) throw new GeneralSecurityException("Texto cifrado muy corto");
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        c.init(Cipher.DECRYPT_MODE, llave, new GCMParameterSpec(ETIQUETA_BITS, todo, 0, NONCE));
        byte[] claro = c.doFinal(todo, NONCE, todo.length - NONCE);
        return new String(claro, StandardCharsets.UTF_8);
    }
}
