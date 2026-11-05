package mx.ecoruta.app;

/** Las acciones ecológicas que se pueden registrar y cuántos puntos da cada una. */
public enum TipoAccion {
    RECICLAJE("Separé o reciclé basura", "kg", 10),
    TRANSPORTE("Usé bici, caminé o fui en transporte público", "km", 2),
    AGUA("Ahorré agua", "litros", 1),
    ENERGIA("Ahorré luz", "horas", 3),
    REFORESTACION("Planté un árbol", "árboles", 50);

    public final String descripcion;
    public final String unidad;
    public final int puntosPorUnidad;

    TipoAccion(String descripcion, String unidad, int puntosPorUnidad) {
        this.descripcion = descripcion;
        this.unidad = unidad;
        this.puntosPorUnidad = puntosPorUnidad;
    }

    public int puntos(int cantidad) {
        return puntosPorUnidad * cantidad;
    }

    /** Busca el tipo por su nombre; si viene uno desconocido (dato alterado) regresa null. */
    public static TipoAccion desdeNombre(String nombre) {
        for (TipoAccion t : values()) {
            if (t.name().equals(nombre)) return t;
        }
        return null;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
