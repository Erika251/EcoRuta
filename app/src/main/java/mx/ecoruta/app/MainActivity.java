package mx.ecoruta.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/** Menú principal: muestra los EcoPuntos y lleva a cada sección. */
public class MainActivity extends Pantalla {

    TextView tvHola, tvPuntos;
    DatabaseReference refAcciones;
    ValueEventListener escucha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser u = Nube.lista(this) ? Nube.usuario() : null;
        if (u == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);

        tvHola = findViewById(R.id.tvHola);
        tvPuntos = findViewById(R.id.tvPuntos);
        tvHola.setText("¡Hola, " + u.getEmail() + "!");

        Button btRegistrar = findViewById(R.id.btRegistrarAccion);
        Button btAcciones = findViewById(R.id.btMisAcciones);
        Button btAire = findViewById(R.id.btAire);
        Button btReciclaje = findViewById(R.id.btReciclaje);
        Button btSalir = findViewById(R.id.btSalir);

        btRegistrar.setOnClickListener(v -> startActivity(new Intent(this, NuevaAccionActivity.class)));
        btAcciones.setOnClickListener(v -> startActivity(new Intent(this, AccionesActivity.class)));
        btAire.setOnClickListener(v -> startActivity(new Intent(this, AireActivity.class)));
        btReciclaje.setOnClickListener(v -> startActivity(new Intent(this, ReciclajeActivity.class)));
        btSalir.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Suma en vivo los puntos de todas las acciones del usuario
        refAcciones = Nube.acciones();
        escucha = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datos) {
                int total = 0;
                for (DataSnapshot hijo : datos.getChildren()) {
                    Accion a = Lector.accion(hijo);
                    if (a != null) total += a.puntos();
                }
                tvPuntos.setText(total + " EcoPuntos");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvPuntos.setText("No se pudieron cargar tus puntos");
            }
        };
        refAcciones.addValueEventListener(escucha);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refAcciones != null && escucha != null) refAcciones.removeEventListener(escucha);
    }
}
