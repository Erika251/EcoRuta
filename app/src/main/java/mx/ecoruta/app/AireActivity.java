package mx.ecoruta.app;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

/** Consulta la calidad del aire con una API externa gratuita (Open-Meteo), siempre por HTTPS. */
public class AireActivity extends Pantalla {

    // Municipios de Michoacán en una lista fija: así no pedimos el permiso de ubicación (menos datos personales)
    Spinner spCiudad;
    Button btConsultar;
    TextView tvResultado;
    ProgressBar progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aire);
        setTitle("Calidad del aire");

        spCiudad = findViewById(R.id.spCiudad);
        btConsultar = findViewById(R.id.btConsultar);
        tvResultado = findViewById(R.id.tvResultado);
        progreso = findViewById(R.id.progreso);

        spCiudad.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                MunicipiosMichoacan.TODOS));
        spCiudad.setSelection(MunicipiosMichoacan.posicionDe("Morelia"));
        btConsultar.setOnClickListener(v -> consultar());
    }

    private void consultar() {
        MunicipiosMichoacan.Municipio m = MunicipiosMichoacan.TODOS[spCiudad.getSelectedItemPosition()];
        progreso.setVisibility(View.VISIBLE);
        btConsultar.setEnabled(false);
        tvResultado.setText("");

        // La red no se usa en el hilo de la pantalla: se hace en otro hilo
        new Thread(() -> {
            String texto;
            try {
                CalidadAire aire = CalidadAire.desdeJson(descargar(CalidadAire.url(m.latitud, m.longitud)));
                texto = m.nombre + ", Michoacán"
                        + "\n\nCalidad del aire: " + aire.nivel()
                        + "\n\nÍndice: " + aire.indice
                        + "\nPM2.5: " + String.format(java.util.Locale.US, "%.1f", aire.pm25) + " µg/m³"
                        + "\n\n" + aire.consejo();
            } catch (Exception e) {
                texto = "No se pudo consultar la calidad del aire. Revisa tu conexión.";
            }
            String resultado = texto;
            runOnUiThread(() -> {
                progreso.setVisibility(View.GONE);
                btConsultar.setEnabled(true);
                tvResultado.setText(resultado);
            });
        }).start();
    }

    private static String descargar(String direccion) throws Exception {
        HttpsURLConnection con = (HttpsURLConnection) new URL(direccion).openConnection();
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);
        try {
            if (con.getResponseCode() != 200) throw new Exception("Servicio no disponible");
            StringBuilder sb = new StringBuilder();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = r.readLine()) != null) sb.append(linea);
            }
            return sb.toString();
        } finally {
            con.disconnect();
        }
    }
}
