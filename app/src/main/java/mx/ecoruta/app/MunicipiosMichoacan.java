package mx.ecoruta.app;

/**
 * Los 113 municipios de Michoacán con las coordenadas de su cabecera municipal.
 * Fuentes: lista de municipios de Wikipedia (Anexo:Municipios de Michoacán) y
 * coordenadas de la API de geocodificación de Open-Meteo (GeoNames).
 */
public final class MunicipiosMichoacan {

    public static final class Municipio {
        public final String nombre;
        public final double latitud;
        public final double longitud;

        Municipio(String nombre, double latitud, double longitud) {
            this.nombre = nombre;
            this.latitud = latitud;
            this.longitud = longitud;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    private MunicipiosMichoacan() {
    }

    /** En orden de su clave oficial (001 Acuitzio ... 113 Zitácuaro). */
    public static final Municipio[] TODOS = {
            new Municipio("Acuitzio", 19.4964, -101.3329),
            new Municipio("Aguililla", 18.7370, -102.7885),
            new Municipio("Álvaro Obregón", 19.8250, -101.0394),
            new Municipio("Angamacutiro", 20.1506, -101.7102),
            new Municipio("Angangueo", 19.6206, -100.2844),
            new Municipio("Apatzingán", 19.0886, -102.3570),
            new Municipio("Aporo", 19.6703, -100.4097),
            new Municipio("Aquila", 18.5993, -103.5037),
            new Municipio("Ario", 19.2083, -101.7090),
            new Municipio("Arteaga", 18.3568, -102.2911),
            new Municipio("Briseñas", 20.2688, -102.5618),
            new Municipio("Buenavista", 19.2114, -102.5883),
            new Municipio("Carácuaro", 19.0172, -101.1272),
            new Municipio("Coahuayana", 18.7036, -103.6627),
            new Municipio("Coalcomán de Vázquez Pallares", 18.7775, -103.1600),
            new Municipio("Coeneo", 19.8208, -101.5843),
            new Municipio("Contepec", 19.9552, -100.1634),
            new Municipio("Copándaro", 19.8931, -101.2139),
            new Municipio("Cotija", 19.8101, -102.7021),
            new Municipio("Cuitzeo", 19.9691, -101.1405),
            new Municipio("Charapan", 19.6504, -102.2517),
            new Municipio("Charo", 19.7477, -101.0461),
            new Municipio("Chavinda", 20.0067, -102.4599),
            new Municipio("Cherán", 19.6862, -101.9540),
            new Municipio("Chilchota", 19.8474, -102.1179),
            new Municipio("Chinicuila", 18.7583, -103.3685),
            new Municipio("Chucándiro", 19.9001, -101.3337),
            new Municipio("Churintzio", 20.1505, -102.0657),
            new Municipio("Churumuco", 18.6637, -101.6475),
            new Municipio("Ecuandureo", 20.1625, -102.1947),
            new Municipio("Epitacio Huerta", 20.1353, -100.2929),
            new Municipio("Erongarícuaro", 19.5887, -101.7205),
            new Municipio("Gabriel Zamora", 19.1581, -102.0511),
            new Municipio("Hidalgo", 19.6896, -100.5550),
            new Municipio("La Huacana", 18.9622, -101.8079),
            new Municipio("Huandacareo", 19.9886, -101.2761),
            new Municipio("Huaniqueo", 19.8987, -101.5046),
            new Municipio("Huetamo", 18.6260, -100.9000),
            new Municipio("Huiramba", 19.5461, -101.4365),
            new Municipio("Indaparapeo", 19.7897, -100.9685),
            new Municipio("Irimbo", 19.6999, -100.4788),
            new Municipio("Ixtlán", 20.1684, -102.3942),
            new Municipio("Jacona", 19.9563, -102.3056),
            new Municipio("Jiménez", 19.9231, -101.7469),
            new Municipio("Jiquilpan", 19.9917, -102.7184),
            new Municipio("Juárez", 19.3125, -100.4344),
            new Municipio("Jungapeo", 19.4586, -100.4945),
            new Municipio("Lagunillas", 19.5637, -101.4155),
            new Municipio("Madero", 19.3918, -101.2787),
            new Municipio("Maravatío", 19.8919, -100.4419),
            new Municipio("Marcos Castellanos", 19.9896, -103.0215),
            new Municipio("Lázaro Cárdenas", 17.9626, -102.1989),
            new Municipio("Morelia", 19.7008, -101.1844),
            new Municipio("Morelos", 20.0038, -101.4128),
            new Municipio("Múgica", 19.0247, -102.0931),
            new Municipio("Nahuatzen", 19.6543, -101.9170),
            new Municipio("Nocupétaro", 19.0436, -101.1621),
            new Municipio("Nuevo Parangaricutiro", 19.4175, -102.1306),
            new Municipio("Nuevo Urecho", 19.1661, -101.8681),
            new Municipio("Numarán", 20.2550, -101.9472),
            new Municipio("Ocampo", 19.5832, -100.3390),
            new Municipio("Pajacuarán", 20.1167, -102.5643),
            new Municipio("Panindícuaro", 19.9843, -101.7612),
            new Municipio("Parácuaro", 19.1473, -102.2186),
            new Municipio("Paracho", 19.6476, -102.0477),
            new Municipio("Pátzcuaro", 19.5132, -101.6099),
            new Municipio("Penjamillo", 20.1044, -101.9335),
            new Municipio("Peribán", 19.5203, -102.4153),
            new Municipio("La Piedad", 20.3411, -102.0225),
            new Municipio("Purépero", 19.9103, -102.0069),
            new Municipio("Puruándiro", 20.0888, -101.5156),
            new Municipio("Queréndaro", 19.8101, -100.8851),
            new Municipio("Quiroga", 19.6648, -101.5232),
            new Municipio("Cojumatlán de Régules", 20.1183, -102.8515),
            new Municipio("Los Reyes", 19.5885, -102.4733),
            new Municipio("Sahuayo", 20.0570, -102.7164),
            new Municipio("San Lucas", 18.5911, -100.7847),
            new Municipio("Santa Ana Maya", 20.0074, -101.0208),
            new Municipio("Salvador Escalante", 19.4066, -101.6399),
            new Municipio("Senguio", 19.7341, -100.3532),
            new Municipio("Susupuato", 19.2152, -100.4080),
            new Municipio("Tacámbaro", 19.2361, -101.4580),
            new Municipio("Tancítaro", 19.3368, -102.3628),
            new Municipio("Tangamandapio", 19.9558, -102.4353),
            new Municipio("Tangancícuaro", 19.8868, -102.2072),
            new Municipio("Tanhuato", 20.2849, -102.3281),
            new Municipio("Taretan", 19.3344, -101.9178),
            new Municipio("Tarímbaro", 19.7958, -101.1778),
            new Municipio("Tepalcatepec", 19.1872, -102.8470),
            new Municipio("Tingambato", 19.5016, -101.8533),
            new Municipio("Tingüindín", 19.7389, -102.4816),
            new Municipio("Tiquicheo de Nicolás Romero", 18.9016, -100.7368),
            new Municipio("Tlalpujahua", 19.8049, -100.1746),
            new Municipio("Tlazazalca", 19.9718, -102.0565),
            new Municipio("Tocumbo", 19.7029, -102.5213),
            new Municipio("Tumbiscatío", 18.5248, -102.3755),
            new Municipio("Turicato", 19.0541, -101.4198),
            new Municipio("Tuxpan", 19.5679, -100.4641),
            new Municipio("Tuzantla", 19.2029, -100.5668),
            new Municipio("Tzintzuntzan", 19.6283, -101.5776),
            new Municipio("Tzitzio", 19.5856, -100.9248),
            new Municipio("Uruapan", 19.4168, -102.0584),
            new Municipio("Venustiano Carranza", 20.1155, -102.6533),
            new Municipio("Villamar", 20.0203, -102.5956),
            new Municipio("Vista Hermosa", 20.2730, -102.4743),
            new Municipio("Yurécuaro", 20.3395, -102.2822),
            new Municipio("Zacapu", 19.8152, -101.7920),
            new Municipio("Zamora", 19.9840, -102.2863),
            new Municipio("Zináparo", 20.1720, -101.9980),
            new Municipio("Zinapécuaro", 19.8601, -100.8273),
            new Municipio("Ziracuaretiro", 19.4167, -101.9103),
            new Municipio("Zitácuaro", 19.4366, -100.3564),
            new Municipio("José Sixto Verduzco", 20.3026, -101.5970),
    };

    /** Posición de Morelia en la lista, para mostrarla primero. */
    public static int posicionDe(String nombre) {
        for (int i = 0; i < TODOS.length; i++) {
            if (TODOS[i].nombre.equals(nombre)) return i;
        }
        return 0;
    }
}
