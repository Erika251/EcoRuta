package mx.ecoruta.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Una acción ecológica guardada en la nube. */
public class Accion {

    public final String id;
    public final TipoAccion tipo;
    public final int cantidad;
    public final long fecha;
    /** La nota siempre se guarda cifrada; ver {@link Cifrador}. */
    public final String notaCifrada;

    public Accion(String id, TipoAccion tipo, int cantidad, long fecha, String notaCifrada) {
        this.id = id;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.notaCifrada = notaCifrada;
    }

    public int puntos() {
        return tipo.puntos(cantidad);
    }

    /** Datos tal como se mandan a Firebase. */
    public Map<String, Object> aMapa() {
        Map<String, Object> m = new HashMap<>();
        m.put("tipo", tipo.name());
        m.put("cantidad", cantidad);
        m.put("fecha", fecha);
        if (notaCifrada != null) m.put("notaCifrada", notaCifrada);
        return m;
    }

    public String fechaTexto() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(fecha));
    }

    public static int totalPuntos(Iterable<Accion> acciones) {
        int total = 0;
        for (Accion a : acciones) total += a.puntos();
        return total;
    }
}
