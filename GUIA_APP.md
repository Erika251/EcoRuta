# EcoRuta: la app para Android

App hecha en **Android Studio con Java**, igual que las prácticas de clase.
Sirve para registrar acciones ecológicas, ganar EcoPuntos, ver la calidad del aire en los
municipios de Michoacán y encontrar puntos de reciclaje.

## Qué hace cada pantalla

| Pantalla | Archivo Java | Qué hace |
|---|---|---|
| Entrar | `LoginActivity` | Entrar o crear cuenta con correo y contraseña (Firebase Authentication) |
| Menú | `MainActivity` | Muestra tus EcoPuntos y los botones de cada sección |
| Registrar una acción | `NuevaAccionActivity` | Eliges qué hiciste y cuánto; la nota personal se guarda **cifrada** |
| Mis acciones | `AccionesActivity` | Lista de tus acciones; mantén presionada una para borrarla |
| Calidad del aire | `AireActivity` | Eliges uno de los 113 municipios de Michoacán y consulta una API externa gratuita (Open-Meteo) |
| Puntos de reciclaje | `ReciclajeActivity` | Lugares para llevar tu basura (se leen de la nube) |

Clases de apoyo (sin pantalla):

| Archivo | Para qué sirve |
|---|---|
| `Validador` | Revisa correos, contraseñas, cantidades y limpia los textos |
| `Cifrador` | Cifra y descifra las notas con AES-256-GCM |
| `LlaveSegura` | Guarda la llave de cifrado en la caja fuerte del teléfono (Android Keystore) |
| `TipoAccion`, `Accion` | Las acciones y sus puntos |
| `CalidadAire` | Lee la respuesta de la API de calidad del aire |
| `MunicipiosMichoacan` | Los 113 municipios de Michoacán con las coordenadas de su cabecera |
| `Pantalla` | Base de todas las pantallas: evita que las barras y el teclado tapen el contenido |
| `Nube`, `Lector` | Hablan con Firebase y revisan lo que llega |

## Cómo abrirla

1. Abre **Android Studio** → **File → Open** → elige la carpeta `Documentos\Uri\EcoRutaAndroid`.
2. Espera a que termine el *Gradle Sync* (la barra de abajo).
3. Conecta tu celular o abre un emulador y presiona **Run ▶**.

La app abre aunque todavía no conectes Firebase: la pantalla de entrada te avisa que falta.

## Conectar Firebase (una sola vez)

1. Entra a https://console.firebase.google.com y abre tu proyecto (o crea uno).
2. **Agregar app → Android**. En *Nombre del paquete* escribe exactamente: `mx.ecoruta.app`
3. Descarga el archivo **google-services.json** y ponlo dentro de la carpeta `EcoRutaAndroid\app`.
4. En el menú de la izquierda:
   - **Authentication → Comenzar → Correo electrónico/contraseña → Habilitar**.
   - **Realtime Database → Crear base de datos** (modo bloqueado).
5. En **Realtime Database → Reglas**, borra lo que hay, pega el contenido de `firebase/reglas_database.json` y presiona **Publicar**.
6. En **Realtime Database → Datos**, presiona los tres puntos ⋮ → **Importar JSON** y elige `firebase/puntos_reciclaje.json`. Así aparecen los puntos de reciclaje.
7. En Android Studio presiona **Sync Now** y luego **Run ▶**.

## Cómo protege los datos

| Riesgo | Qué hace la app |
|---|---|
| Contraseñas débiles | Pide 8 letras o más, con mayúscula, minúscula y número |
| Adivinar qué correos tienen cuenta | Los errores siempre dicen «Correo o contraseña incorrectos» |
| Ver datos de otra persona | Las reglas de Firebase solo dejan a cada usuario ver y escribir **sus** acciones |
| Datos falsos o código raro | Se revisan en la app y otra vez en las reglas de Firebase (tipo, tamaño, rango) |
| Espiar la conexión | Solo se permite HTTPS (`network_security_config.xml`) |
| Leer las notas personales | Se cifran en el teléfono con AES-256-GCM antes de subirlas |
| Robar la llave de cifrado | Vive en el Android Keystore, no se puede copiar |
| Sacar datos con un respaldo | `allowBackup="false"` y reglas de extracción de datos |
| Copiar el código de la app | La versión final se ofusca (`optimization enable true`) |

## Pruebas automáticas

Están en `app/src/test/java/mx/ecoruta/app/`:

| Prueba | Qué revisa |
|---|---|
| `ValidadorTest` | Que no se acepten correos, contraseñas o cantidades malas |
| `CifradorTest` | Que las notas se cifren, se descifren y se detecte si alguien las altera |
| `CalidadAireTest` | Que se lea bien la API y solo se use HTTPS |
| `MunicipiosMichoacanTest` | Que estén los 113 municipios, sin repetir y dentro de Michoacán |
| `RegistroAccionTest` | Validar, limpiar, cifrar y armar los datos juntos, como en la app (integración) |

Para correrlas: en Android Studio, clic derecho en la carpeta `test` → **Run 'Tests in mx.ecoruta.app'**.
O en la terminal de Android Studio:

```
gradlew testDebugUnitTest
```

GitHub también las corre solo cada vez que subes cambios, junto con la revisión de código (*lint*), y arma el APK.

