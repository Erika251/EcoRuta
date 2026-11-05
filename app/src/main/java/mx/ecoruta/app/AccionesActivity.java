package mx.ecoruta.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Lista de las acciones del usuario. Mantén presionada una acción para borrarla. */
public class AccionesActivity extends Pantalla {

    TextView tvTotal, tvCuantas;
    ListView lista;
    final List<Accion> acciones = new ArrayList<>();
    AdaptadorAcciones adaptador;
    DatabaseReference ref;
    Query consulta;
    ValueEventListener escucha;
    Cifrador cifrador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);
        setTitle("Mis acciones");

        tvTotal = findViewById(R.id.tvTotal);
        tvCuantas = findViewById(R.id.tvCuantas);
        lista = findViewById(R.id.lista);

        try {
            cifrador = new Cifrador(LlaveSegura.obtener());
        } catch (Exception e) {
            cifrador = null;   // sin llave las notas no se muestran, pero la lista sí
        }

        adaptador = new AdaptadorAcciones(this, acciones, cifrador);
        lista.setAdapter(adaptador);
        lista.setEmptyView(findViewById(R.id.tvVacio));

        lista.setOnItemLongClickListener((padre, vista, pos, id) -> {
            Accion a = acciones.get(pos);
            new AlertDialog.Builder(this)
                    .setTitle("¿Borrar esta acción?")
                    .setMessage(a.tipo.descripcion + " (" + a.cantidad + " " + a.tipo.unidad + ")")
                    .setPositiveButton("Borrar", (d, w) -> ref.child(a.id).removeValue())
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        });

        ref = Nube.acciones();
        escucha = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datos) {
                acciones.clear();
                for (DataSnapshot hijo : datos.getChildren()) {
                    Accion a = Lector.accion(hijo);
                    if (a != null) acciones.add(a);
                }
                Collections.reverse(acciones);   // las más nuevas arriba
                adaptador.notifyDataSetChanged();
                tvTotal.setText(String.valueOf(Accion.totalPuntos(acciones)));
                tvCuantas.setText(acciones.size() == 1 ? "1 acción registrada" : acciones.size() + " acciones registradas");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvTotal.setText("—");
                tvCuantas.setText("No se pudieron cargar tus acciones");
            }
        };
        consulta = ref.orderByChild("fecha").limitToLast(100);
        consulta.addValueEventListener(escucha);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (consulta != null && escucha != null) consulta.removeEventListener(escucha);
    }

    /** Dibuja cada acción con su diseño (item_accion.xml): ícono, nombre, detalle, nota y puntos. */
    static class AdaptadorAcciones extends ArrayAdapter<Accion> {

        private final Cifrador cifrador;

        AdaptadorAcciones(Context contexto, List<Accion> acciones, Cifrador cifrador) {
            super(contexto, 0, acciones);
            this.cifrador = cifrador;
        }

        @NonNull
        @Override
        public View getView(int pos, View vista, @NonNull ViewGroup padre) {
            if (vista == null) {
                vista = LayoutInflater.from(getContext()).inflate(R.layout.item_accion, padre, false);
            }
            Accion a = getItem(pos);
            if (a == null) return vista;

            TextView tvIcono = vista.findViewById(R.id.tvIcono);
            TextView tvTipo = vista.findViewById(R.id.tvTipo);
            TextView tvDetalle = vista.findViewById(R.id.tvDetalle);
            TextView tvNota = vista.findViewById(R.id.tvNota);
            TextView tvPuntos = vista.findViewById(R.id.tvPuntosAccion);

            tvIcono.setText(icono(a.tipo));
            tvTipo.setText(a.tipo.descripcion);
            tvDetalle.setText(a.cantidad + " " + a.tipo.unidad + "   •   " + a.fechaTexto());
            tvPuntos.setText("+" + a.puntos());

            String nota = nota(a);
            tvNota.setVisibility(nota == null ? View.GONE : View.VISIBLE);
            tvNota.setText(nota == null ? "" : "📝 " + nota);
            return vista;
        }

        private String nota(Accion a) {
            if (a.notaCifrada == null || cifrador == null) return null;
            try {
                return cifrador.descifrar(a.notaCifrada);
            } catch (Exception e) {
                return "(nota escrita en otro teléfono)";
            }
        }

        private static String icono(TipoAccion t) {
            switch (t) {
                case RECICLAJE: return "♻";
                case TRANSPORTE: return "🚲";
                case AGUA: return "💧";
                case ENERGIA: return "💡";
                default: return "🌳";
            }
        }
    }
}
