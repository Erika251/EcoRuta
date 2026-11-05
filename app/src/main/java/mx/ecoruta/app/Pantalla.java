package mx.ecoruta.app;

import android.graphics.Rect;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Base de TODAS las pantallas de la app.
 * En Android 15 o más nuevo la app se dibuja detrás de la barra de arriba (con el título),
 * de la barra de navegación de abajo y del teclado. Esta clase deja el espacio justo para cada
 * una, así nada queda tapado, y cuando aparece el teclado recorre la pantalla hasta el campo
 * donde escribes.
 */
public class Pantalla extends AppCompatActivity {

    @Override
    public void setContentView(int idDiseno) {
        super.setContentView(idDiseno);
        respetarBordes();
    }

    private void respetarBordes() {
        View contenido = findViewById(android.R.id.content);

        ViewCompat.setOnApplyWindowInsetsListener(contenido, (v, insets) -> {
            // El espacio de arriba ya incluye la barra del título
            Insets barras = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            Insets teclado = insets.getInsets(WindowInsetsCompat.Type.ime());
            v.setPadding(barras.left, barras.top, barras.right, Math.max(barras.bottom, teclado.bottom));
            if (teclado.bottom > 0) v.post(this::mostrarCampoActual);
            return WindowInsetsCompat.CONSUMED;
        });
        ViewCompat.requestApplyInsets(contenido);

        // Al pasar de un campo a otro, también se recorre hasta él
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener(
                (antes, ahora) -> {
                    if (ahora != null) ahora.post(this::mostrarCampoActual);
                });
    }

    /** Pide que el campo con el cursor (y un poco de espacio debajo) quede a la vista. */
    private void mostrarCampoActual() {
        View campo = getCurrentFocus();
        if (campo == null) return;
        int margen = (int) (24 * getResources().getDisplayMetrics().density);
        campo.requestRectangleOnScreen(new Rect(0, 0, campo.getWidth(), campo.getHeight() + margen), false);
    }
}
