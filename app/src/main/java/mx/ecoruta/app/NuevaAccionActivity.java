package mx.ecoruta.app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/** Formulario para registrar una acción ecológica. La nota se cifra antes de subirla. */
public class NuevaAccionActivity extends Pantalla {

    Spinner spTipo;
    EditText edCantidad, edNota;
    TextView tvUnidad, tvPuntos;
    Button btGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_accion);
        setTitle("Registrar acción");

        spTipo = findViewById(R.id.spTipo);
        edCantidad = findViewById(R.id.edCantidad);
        edNota = findViewById(R.id.edNota);
        tvUnidad = findViewById(R.id.tvUnidad);
        tvPuntos = findViewById(R.id.tvPuntosPrevios);
        btGuardar = findViewById(R.id.btGuardar);

        ArrayAdapter<TipoAccion> adaptador = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, TipoAccion.values());
        spTipo.setAdapter(adaptador);
        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> padre, View vista, int pos, long id) {
                actualizarPuntos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> padre) {
            }
        });
        edCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {
            }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                actualizarPuntos();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btGuardar.setOnClickListener(v -> guardar());
    }

    private TipoAccion tipo() {
        return (TipoAccion) spTipo.getSelectedItem();
    }

    private void actualizarPuntos() {
        TipoAccion t = tipo();
        tvUnidad.setText("Cantidad (" + t.unidad + ")");
        int n;
        try {
            n = Integer.parseInt(edCantidad.getText().toString().trim());
        } catch (NumberFormatException e) {
            n = 0;
        }
        tvPuntos.setText("Ganarás " + t.puntos(Math.max(0, Math.min(n, Validador.MAX_CANTIDAD))) + " EcoPuntos");
    }

    private void guardar() {
        String textoCantidad = edCantidad.getText().toString();
        String error = Validador.cantidad(textoCantidad);
        if (error != null) {
            edCantidad.setError(error);
            return;
        }
        int cantidad = Integer.parseInt(textoCantidad.trim());
        String nota = Validador.limpiar(edNota.getText().toString());

        String notaCifrada = null;
        if (!nota.isEmpty()) {
            try {
                notaCifrada = new Cifrador(LlaveSegura.obtener()).cifrar(nota);
            } catch (Exception e) {
                Toast.makeText(this, "No se pudo proteger la nota", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Accion accion = new Accion(null, tipo(), cantidad, System.currentTimeMillis(), notaCifrada);
        btGuardar.setEnabled(false);
        Nube.acciones().push().setValue(accion.aMapa()).addOnCompleteListener(this, tarea -> {
            if (tarea.isSuccessful()) {
                Toast.makeText(this, "¡Guardado! +" + accion.puntos() + " EcoPuntos", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                btGuardar.setEnabled(true);
                Toast.makeText(this, "No se pudo guardar. Revisa tu conexión.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
