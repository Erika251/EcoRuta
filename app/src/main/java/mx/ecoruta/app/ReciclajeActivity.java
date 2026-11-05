package mx.ecoruta.app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/** Lugares donde se puede llevar la basura reciclable (se leen de la nube, solo lectura). */
public class ReciclajeActivity extends Pantalla {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciclaje);
        setTitle("Puntos de reciclaje");

        ListView lista = findViewById(R.id.lista);
        TextView tvVacio = findViewById(R.id.tvVacio);
        List<String> renglones = new ArrayList<>();
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, renglones);
        lista.setAdapter(adaptador);
        lista.setEmptyView(tvVacio);

        Nube.puntosReciclaje().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datos) {
                for (DataSnapshot p : datos.getChildren()) {
                    String nombre = p.child("nombre").getValue(String.class);
                    String direccion = p.child("direccion").getValue(String.class);
                    String acepta = p.child("acepta").getValue(String.class);
                    if (nombre != null) {
                        renglones.add("📍 " + nombre + "\n" + (direccion == null ? "" : direccion)
                                + (acepta == null ? "" : "\nAcepta: " + acepta));
                    }
                }
                adaptador.notifyDataSetChanged();
                tvVacio.setText("Aún no hay puntos de reciclaje registrados.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvVacio.setText("No se pudieron cargar los puntos de reciclaje.");
            }
        });
    }
}
