package mx.ecoruta.app;

import org.json.JSONException;
import org.json.JSONObject;

/** Resultado de la API de calidad del aire (Open-Meteo). */
public class CalidadAire {

    /** Índice europeo de calidad del aire: 0 es muy bueno. */
    public final int indice;
    public final double pm25;

    public CalidadAire(int indice, double pm25) {
        this.indice = indice;
        this.pm25 = pm25;
    }

    public String nivel() {
        if (indice <= 20) return "Buena";
        if (indice <= 40) return "Aceptable";
        if (indice <= 60) return "Moderada";
        if (indice <= 80) return "Mala";
        return "Muy mala";
    }

    public String consejo() {
        return indice > 40
                ? "Hoy conviene caminar o usar transporte público para no contaminar más."
                : "¡Buen día para moverte en bici o caminando!";
    }

    /** Lee la respuesta JSON de la API. Si viene algo raro, lanza JSONException. */
    public static CalidadAire desdeJson(String json) throws JSONException {
        JSONObject actual = new JSONObject(json).getJSONObject("current");
        return new CalidadAire(
                (int) Math.round(actual.optDouble("european_aqi", 0)),
                actual.optDouble("pm2_5", 0));
    }

    /** Dirección de la API: solo HTTPS y con coordenadas revisadas. */
    public static String url(double latitud, double longitud) {
        if (Math.abs(latitud) > 90 || Math.abs(longitud) > 180) {
            throw new IllegalArgumentException("Coordenadas fuera de rango");
        }
        return String.format(java.util.Locale.US,
                "https://air-quality-api.open-meteo.com/v1/air-quality?latitude=%.4f&longitude=%.4f&current=european_aqi,pm2_5",
                latitud, longitud);
    }
}
