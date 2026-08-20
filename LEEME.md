# Fuerza

App de gimnasio para Android. Eliges una rutina, la app te dice qué toca hoy, y
tú apuntas cada serie con el peso y las repeticiones. Al anotar una serie arranca
solo el descanso, y te avisa aunque tengas la pantalla apagada.

## En qué se diferencia de Foco y de Hábitos

Son tres proyectos independientes, con paquetes distintos (`com.lucas.foco`,
`com.lucas.habitos` y `com.lucas.fuerza`), así que se instalan a la vez sin
pisarse. Fuerza no comparte código con ninguna: lo único que hereda es la forma
de trabajar (Kotlin, Compose, cero cuentas, APK por GitHub Actions).

Se distinguen a la primera en el cajón de aplicaciones: Fuerza usa una interfaz
clara, tarjetas blancas y un violeta deportivo como color principal.

## Privacidad

Esto cambió, y merece contarse entero.

Fuerza nació **sin permiso de INTERNET**, a propósito. Sin ese permiso, "nada de
lo que guardas sale del teléfono" no era una promesa: era una restricción del
sistema operativo, y no había que fiarse de nadie para creérsela.

El permiso se añadió para poder ver el vídeo del ejercicio dentro de la app en
vez de saltar a YouTube. A partir de ahí la garantía pasa a ser **confianza**, y
eso es un escalón más abajo. Conviene decirlo claro en vez de disimularlo.

Lo que sigue siendo cierto, y se puede comprobar leyendo el código:

- Los entrenos siguen en un archivo JSON de la carpeta privada de la app. Nadie
  los lee ni los envía a ningún sitio.
- No hay analítica, ni cuentas, ni servidor propio. La app abre exactamente dos
  cosas: la miniatura del vídeo que fijaste y el reproductor de YouTube, y solo
  cuando tocas ese vídeo.
- El catálogo, las demostraciones y las rutinas están compilados dentro del
  APK. Los vídeos de técnica funcionan incluso en un gimnasio sin cobertura.
  Solo los enlaces opcionales de YouTube necesitan internet.

Si algún día quieres la garantía de vuelta, es quitar una línea del manifest y
el botón de vídeo vuelve a abrir YouTube por fuera.

## Demostraciones en vídeo

Los **103 ejercicios** del catálogo tienen un vídeo local de MuscleWiki y una
miniatura extraída del mismo archivo. Se reproducen en bucle y sin sonido dentro
de la ficha del ejercicio, sin streaming y sin depender de la cobertura.

Las ilustraciones generadas que se usaban como sustituto fueron retiradas. El
origen de los vídeos y la advertencia necesaria para una futura publicación
están documentados en `ATRIBUCION.md`.

### Vídeos de YouTube opcionales

Cada ejercicio tiene un botón que abre YouTube. Por defecto lanza una búsqueda;
cuando encuentras el vídeo que te gusta le das a **Fijar**, pegas el enlace, y a
partir de ahí ese ejercicio abre siempre ese vídeo. En unas semanas tienes tu
propia videoteca, elegida por ti y guardada en el teléfono.

Buscar abre YouTube por fuera; **ver** el vídeo fijado lo abre dentro de la app,
en el reproductor incrustado de YouTube, sin salir de Fuerza. Esta función es
independiente de la demostración local incluida en cada ejercicio.

Hay vídeos que su autor no deja incrustar. Cuando pasa, el reproductor lo dice y
el popup lleva un botón para abrirlo en YouTube.

La demostración local sigue disponible aunque el vídeo de YouTube no cargue.

## La tipografía

Dos letras, cada una en lo suyo:

- **Anton** en los titulares y en las cifras grandes — el nombre del día, los
  kilos de la semana, la cuenta atrás del descanso. Condensada y muy negra, de
  cartel de gimnasio. Aguanta bien en grande y fatal en pequeño, así que no pisa
  nunca un párrafo.
- **Figtree** en todo lo demás: textos, botones y etiquetas. Geométrica y
  abierta, que es justo lo que hace falta debajo de una condensada tan cerrada.

Las dos son SIL Open Font License y sus licencias van en `tipografias/`.

Los `.ttf` van **recortados** a lo que la app escribe de verdad — latino,
puntuación y cuatro símbolos. Las cuatro suman 167 KB. Si alguna vez aparece un
carácter fuera del recorte lo dibuja la letra del sistema; no se rompe nada.

Anton trae un solo grosor y de Figtree van tres. Un grosor de más son 30 KB
dentro del APK que nadie llega a ver nunca.

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
