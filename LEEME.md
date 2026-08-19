# Fuerza

App de gimnasio para Android. Eliges una rutina, la app te dice qué toca hoy, y
tú apuntas cada serie con el peso y las repeticiones. Al anotar una serie arranca
solo el descanso, y te avisa aunque tengas la pantalla apagada.

## En qué se diferencia de Foco y de Hábitos

Son tres proyectos independientes, con paquetes distintos (`com.lucas.foco`,
`com.lucas.habitos` y `com.lucas.fuerza`), así que se instalan a la vez sin
pisarse. Fuerza no comparte código con ninguna: lo único que hereda es la forma
de trabajar (Kotlin, Compose, cero cuentas, APK por GitHub Actions) y las
tipografías.

Se distinguen a la primera en el cajón de aplicaciones: Foco es lila y de día,
Hábitos es naranja y crudo, Fuerza es negra y roja.

## Privacidad

**La app no tiene permiso de INTERNET.** No es una promesa, es una restricción
del sistema: sin ese permiso, nada de lo que guarda puede salir del teléfono.

Es además lo que hace que sirva en un gimnasio de sótano, donde no hay cobertura.

## Las imágenes

Cada uno de los 83 ejercicios lleva **dos fotogramas**: posición inicial y
posición final. Alternándolos cada segundo se ve el movimiento. Van dentro del
APK, en WebP, y los 83 completos ocupan menos de 4 MB — así que funcionan en un
sótano sin cobertura, que es donde se usan.

Aparecen en tres sitios: miniatura en el entreno de hoy, miniatura en cada fila
del catálogo, y a pantalla completa al tocar la tarjeta de un ejercicio durante
la sesión, junto con las claves de técnica.

Las fotos vienen del conjunto público `free-exercise-db`. El repositorio se
publica bajo Unlicense, pero **el origen de las imágenes no está del todo claro**
y hay hilos abiertos preguntándolo. Para una app que te instalas tú por tu
cuenta no hay problema; si algún día la subes a Play, esto hay que resolverlo
antes.

### Por qué no vídeo

Una API de vídeo HD era la alternativa. Sigue estando sobre la mesa, pero tiene
letra pequeña que conviene saber: los planes de entrada desbloquean entre 250 y
350 ejercicios distintos al mes de un catálogo de más de mil; las URLs de vídeo
caducan a las 48 horas y la documentación prohíbe cachearlas, así que la app
necesitaría internet cada vez que abres un ejercicio; y el uso sin conexión solo
está en el plan de 79 dólares al mes.

Si algún día quieres vídeo, el sitio por donde entra es `Imagenes.kt`: cambias de
dónde salen los fotogramas y el resto de la app no se entera.

## Las tipografías

- **Bebas Neue** para los titulares. Es condensada y de caja alta — no tiene
  minúsculas, y no le hacen falta. Un solo peso: pedirle un negrita haría que
  Android se inventara uno engordando los trazos.
- **Barlow** para el cuerpo y los datos. Misma familia grotesca, algo estrechada,
  aire deportivo, y seis pesos con números de altura uniforme, que es lo que hace
  falta cuando media pantalla son kilos y repeticiones.

Las dos son SIL Open Font License; las licencias van en `tipografias/`.

## Cómo se guardan los entrenos

Un archivo JSON en la carpeta privada de la app, no una base de datos. Un año
entrenando cinco días por semana son unas doscientas cincuenta sesiones, unos
pocos cientos de kilobytes que caben de sobra en memoria. Se escribe primero en
un temporal y luego se renombra, que es una operación atómica: si el teléfono se
apaga a mitad de guardado te quedas con el archivo anterior entero, nunca con uno
a medias.

## El descanso

No usa un servicio en primer plano, usa `AlarmManager.setAlarmClock`. Es exacto
al segundo, atraviesa el modo Doze con la pantalla apagada y no pide ningún
permiso especial: Android lo trata como un despertador. El precio es el iconito
de alarma en la barra de estado mientras corre el descanso, que además hace de
recordatorio.

## Las rutinas

Tres, y a propósito no hay más:

- **Cuerpo completo**, 3 días. Lo que más rinde el primer año.
- **Torso / Pierna**, 4 días. El punto dulce entre frecuencia y volumen.
- **Empuje / Tirón / Pierna**, 6 días. Solo si duermes y comes en serio.

El contador de días avanza al terminar un entreno, no con el calendario. Si te
saltas el martes, el martes que viene te toca lo que te tocaba: la rutina no se
descuadra por faltar un día, que es lo que hace que la gente la abandone.

## Compilar

`git push` a la rama `main` y GitHub Actions genera el APK. Queda en la pestaña
Actions como artefacto y también como descarga directa en `releases/tag/apk`.

El proyecto usa exactamente las mismas versiones de AGP, Kotlin y Compose que
Foco, que son las que ya se sabe que compilan en Actions.
