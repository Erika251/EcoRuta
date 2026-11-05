package mx.ecoruta.app;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/** Atajos para hablar con Firebase (inicio de sesión y base de datos en la nube). */
public final class Nube {

    private Nube() {
    }

    /** true si el proyecto ya tiene su google-services.json y Firebase arrancó. */
    public static boolean lista(Context contexto) {
        return !FirebaseApp.getApps(contexto).isEmpty();
    }

    public static FirebaseUser usuario() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /** Cada usuario guarda sus acciones en usuarios/{su id}/acciones. */
    public static DatabaseReference acciones() {
        FirebaseUser u = usuario();
        if (u == null) throw new IllegalStateException("No hay sesión");
        return FirebaseDatabase.getInstance().getReference("usuarios").child(u.getUid()).child("acciones");
    }

    public static DatabaseReference puntosReciclaje() {
        return FirebaseDatabase.getInstance().getReference("puntos_reciclaje");
    }
}
