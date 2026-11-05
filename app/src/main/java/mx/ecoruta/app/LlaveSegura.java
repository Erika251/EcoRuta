package mx.ecoruta.app;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Guarda la llave de cifrado en la "caja fuerte" del teléfono (Android Keystore).
 * La llave nunca sale de ahí: ni la app ni otra persona la pueden copiar.
 */
public final class LlaveSegura {

    private static final String ALIAS = "ecoruta_notas";

    private LlaveSegura() {
    }

    public static SecretKey obtener() throws GeneralSecurityException {
        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            if (ks.containsAlias(ALIAS)) {
                return ((KeyStore.SecretKeyEntry) ks.getEntry(ALIAS, null)).getSecretKey();
            }
            KeyGenerator gen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            gen.init(new KeyGenParameterSpec.Builder(ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .setRandomizedEncryptionRequired(false)
                    .build());
            return gen.generateKey();
        } catch (java.io.IOException e) {
            throw new GeneralSecurityException("No se pudo abrir el almacén de llaves", e);
        }
    }
}
