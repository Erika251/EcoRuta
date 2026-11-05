package mx.ecoruta.app;

import java.util.regex.Pattern;

/**
 * Revisa todo lo que escribe el usuario ANTES de usarlo o mandarlo a la nube.
 * Cada método regresa null si el dato está bien, o el mensaje de error si está mal.
 */
public final class Validador {

    public static final int MAX_NOTA = 280;
    public static final int MAX_CANTIDAD = 1000;

    private static final Pattern CORREO =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private Validador() {
    }

    public static String correo(String valor) {
        String v = valor == null ? "" : valor.trim();
        if (v.isEmpty()) return "Escribe tu correo";
        if (v.length() > 254 || !CORREO.matcher(v).matches()) return "Correo no válido";
        return null;
    }

    /** Contraseña segura: 8 o más letras, con mayúscula, minúscula y número. */
    public static String contrasena(String valor) {
        String v = valor == null ? "" : valor;
        if (v.length() < 8) return "Mínimo 8 caracteres";
        if (v.length() > 128) return "Máximo 128 caracteres";
        if (!v.matches(".*[A-Z].*")) return "Incluye una mayúscula";
        if (!v.matches(".*[a-z].*")) return "Incluye una minúscula";
        if (!v.matches(".*[0-9].*")) return "Incluye un número";
        return null;
    }

    public static String cantidad(String valor) {
        int n;
        try {
            n = Integer.parseInt(valor == null ? "" : valor.trim());
        } catch (NumberFormatException e) {
            return "Escribe un número";
        }
        if (n < 1 || n > MAX_CANTIDAD) return "Entre 1 y " + MAX_CANTIDAD;
        return null;
    }

    /** Quita caracteres de control y los signos < > (evita meter código) y recorta el largo. */
    public static String limpiar(String valor) {
        if (valor == null) return "";
        String v = valor.replaceAll("[\\p{Cntrl}]", " ").replaceAll("[<>]", "").trim();
        return v.length() > MAX_NOTA ? v.substring(0, MAX_NOTA) : v;
    }
}
