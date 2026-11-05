package mx.ecoruta.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

/**
 * Pantalla para entrar o crear una cuenta (Firebase Authentication).
 * Tiene dos modos: "Entrar" (ya tienes cuenta) y "Crear cuenta nueva".
 */
public class LoginActivity extends Pantalla {

    EditText edCorreo, edContrasena, edConfirmar;
    Button btEntrar, btRegistrar;
    TextView tvTitulo, tvError;
    ProgressBar progreso;

    /** false = modo "Entrar"; true = modo "Crear cuenta nueva". */
    boolean modoRegistro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edCorreo = findViewById(R.id.edCorreo);
        edContrasena = findViewById(R.id.edContrasena);
        edConfirmar = findViewById(R.id.edConfirmar);
        btEntrar = findViewById(R.id.btEntrar);
        btRegistrar = findViewById(R.id.btRegistrar);
        tvTitulo = findViewById(R.id.tvTitulo);
        tvError = findViewById(R.id.tvError);
        progreso = findViewById(R.id.progreso);

        if (!Nube.lista(this)) {
            mostrarError("Falta conectar Firebase: agrega el archivo google-services.json a la carpeta app (ver GUIA_APP.md).");
            btEntrar.setEnabled(false);
            btRegistrar.setEnabled(false);
            return;
        }
        if (Nube.usuario() != null) {
            irAlMenu();
            return;
        }

        btEntrar.setOnClickListener(v -> enviar());
        btRegistrar.setOnClickListener(v -> cambiarModo(!modoRegistro));
        cambiarModo(false);
    }

    /** Cambia la pantalla entre "Entrar" y "Crear cuenta nueva". */
    private void cambiarModo(boolean registro) {
        modoRegistro = registro;
        tvError.setVisibility(View.GONE);
        edCorreo.setError(null);
        edContrasena.setError(null);
        edConfirmar.setError(null);
        if (registro) {
            tvTitulo.setText("Crear cuenta nueva");
            edContrasena.setHint("Crea tu contraseña");
            edConfirmar.setVisibility(View.VISIBLE);
            btEntrar.setText("Crear mi cuenta");
            btRegistrar.setText("Ya tengo cuenta, quiero entrar");
        } else {
            tvTitulo.setText("Entrar");
            edContrasena.setHint("Contraseña");
            edConfirmar.setVisibility(View.GONE);
            edConfirmar.setText("");
            btEntrar.setText("Entrar");
            btRegistrar.setText("¿No tienes cuenta? Crea una aquí");
        }
    }

    private void enviar() {
        String correo = edCorreo.getText().toString().trim();
        String contrasena = edContrasena.getText().toString();

        String error = Validador.correo(correo);
        if (error != null) {
            edCorreo.setError(error);
            edCorreo.requestFocus();
            return;
        }
        if (modoRegistro) {
            // Al crear la cuenta se exige contraseña segura y escrita igual dos veces
            error = Validador.contrasena(contrasena);
            if (error != null) {
                edContrasena.setError(error);
                edContrasena.requestFocus();
                return;
            }
            if (!contrasena.equals(edConfirmar.getText().toString())) {
                edConfirmar.setError("Las contraseñas no son iguales");
                edConfirmar.requestFocus();
                return;
            }
        } else if (contrasena.isEmpty()) {
            edContrasena.setError("Escribe tu contraseña");
            edContrasena.requestFocus();
            return;
        }

        cargando(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        boolean esRegistro = modoRegistro;
        (esRegistro ? auth.createUserWithEmailAndPassword(correo, contrasena)
                : auth.signInWithEmailAndPassword(correo, contrasena))
                .addOnCompleteListener(this, tarea -> {
                    cargando(false);
                    if (tarea.isSuccessful()) {
                        FirebaseUser u = Nube.usuario();
                        if (esRegistro && u != null) u.sendEmailVerification();
                        irAlMenu();
                    } else {
                        mostrarError(mensajeAmigable(tarea.getException(), esRegistro));
                    }
                });
    }

    /** Mensajes generales: no le decimos a nadie si un correo tiene cuenta o no. */
    private String mensajeAmigable(Exception e, boolean esRegistro) {
        String codigo = e instanceof FirebaseAuthException ? ((FirebaseAuthException) e).getErrorCode() : "";
        String detalle = e != null && e.getMessage() != null ? e.getMessage() : "";
        if (codigo.contains("TOO_MANY_REQUESTS")) return "Demasiados intentos. Espera unos minutos.";
        if (e != null && e.getClass().getSimpleName().contains("Network")) return "Sin conexión a internet.";
        // Errores de configuración del proyecto (no dicen nada de la cuenta de nadie)
        if (codigo.contains("OPERATION_NOT_ALLOWED") || detalle.contains("CONFIGURATION_NOT_FOUND")) {
            return "Falta activar el inicio con correo en Firebase: Authentication → Método de acceso → "
                    + "Correo electrónico/contraseña → Habilitar.";
        }
        if (esRegistro) {
            return "No se pudo crear la cuenta. Si ese correo ya tiene cuenta, toca «Ya tengo cuenta, quiero entrar».";
        }
        return "Correo o contraseña incorrectos.";
    }

    private void cargando(boolean si) {
        progreso.setVisibility(si ? View.VISIBLE : View.GONE);
        btEntrar.setEnabled(!si);
        btRegistrar.setEnabled(!si);
        tvError.setVisibility(View.GONE);
    }

    private void mostrarError(String mensaje) {
        tvError.setText(mensaje);
        tvError.setVisibility(View.VISIBLE);
    }

    private void irAlMenu() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
