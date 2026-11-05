package mx.ecoruta.app;

import com.google.firebase.database.DataSnapshot;

/** Convierte lo que llega de Firebase en objetos de la app, revisando cada dato. */
public final class Lector {

    private Lector() {
    }

    /** Regresa la acción, o null si el dato está incompleto o fue alterado. */
    public static Accion accion(DataSnapshot d) {
        TipoAccion tipo = TipoAccion.desdeNombre(d.child("tipo").getValue(String.class));
        Long cantidad = d.child("cantidad").getValue(Long.class);
        Long fecha = d.child("fecha").getValue(Long.class);
        if (tipo == null || cantidad == null || fecha == null) return null;
        return new Accion(d.getKey(), tipo, cantidad.intValue(), fecha, d.child("notaCifrada").getValue(String.class));
    }
}
