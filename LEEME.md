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
- El catálogo, las demostraciones y las rutinas siguen compilados dentro del
  APK. En un gimnasio de sótano sin cobertura la app funciona entera menos el
  vídeo.

Si algún día quieres la garantía de vuelta, es quitar una línea del manifest y
el botón de vídeo vuelve a abrir YouTube por fuera.

## Las imágenes

Cada ejercicio lleva una **demostración**: cuerpo gris con los músculos que
trabajan en rojo, sobre fondo blanco. Es el mismo estilo que usan las apps de
gimnasio de pago, porque las imágenes son exactamente las mismas.

### No son una animación, y conviene saberlo

Los GIF originales de Gym visual no animan el movimiento. Traen **dos poses** —
la de inicio y la de final — y se pasan de una a otra con un fundido. El GIF
está un segundo quieto en cada pose y dedica medio segundo a cruzarlas con cinco
cuadros de mezcla. Ese cuadro de en medio no es una postura intermedia: son los
dos cuerpos superpuestos al 50%.

Se comprobó ejercicio por ejercicio, ajustando cada cuadro como mezcla lineal de
los dos extremos: encaja con un error de 2 sobre 255, que es el ruido de la
paleta de 256 colores del GIF. De los 82, ochenta tienen dos poses; el paseo del
granjero y el puente de glúteo tienen cuatro.

Así que la app **guarda solo las poses de verdad** y hace el fundido ella:

- Pesa cuatro veces menos: 0,7 MB en vez de 2,9.
- El fundido va a la velocidad de la pantalla, no a cinco pasos.
- No arrastra el emborronado de la paleta del GIF.
- Y respeta los tiempos del original, que es lo que estaba roto: la app pasaba
  los doce cuadros a intervalos iguales, así que la pausa de un segundo sobre
  cada pose desaparecía y solo se veía el cruce. De ahí que pareciera que se
  paraba y se entrecortaba.

El fundido se dibuja con la pose de abajo opaca y la de encima a la mezcla que
toque. Con las dos a medias —que es como estaba— las opacidades no suman uno,
por el medio se cuela el fondo de la tarjeta y la figura pierde un cuarto de su
color en cada cruce. Eso era el titileo.

Van dentro del APK y funcionan sin cobertura.

### Los que ya tienen vídeo

Un ejercicio con vídeo no usa nada de lo anterior: ahí hay una persona haciendo
el movimiento entero, en bucle y sin sonido, en vez de dos poses fundiéndose.
Cuesta unos cientos de KB por ejercicio y se lleva por delante el dibujo, que se borra
en cuanto entra el vídeo — la miniatura de las listas sale de un fotograma del
propio vídeo.

Sigue siendo todo local: nada de streaming, así que en el sótano funcionan
igual. De dónde salen y qué hay que tener en cuenta está en `ATRIBUCION.md`.

De los 100 del catálogo, **colgarse de la barra** se queda sin demostración porque
no hay ninguna equivalente en el conjunto de datos. La app lo dibuja con su hueco
en vez de enseñar otro ejercicio distinto.

### De dónde salen y qué hay que respetar

Las imágenes son **propiedad de Gym visual** (https://gymvisual.com/). Vienen del
conjunto [hasaneyldrm/exercises-dataset](https://github.com/hasaneyldrm/exercises-dataset),
que las redistribuye **con permiso escrito** del autor bajo dos condiciones:

1. **180×180 como máximo.** Por eso las demostraciones no se escalan más allá de
   su tamaño original y se ven algo suaves a pantalla completa. No es un
   descuido: subirlas de tamaño incumpliría el permiso.
2. **El crédito tiene que verse.** Va debajo de cada demostración grande, en
   `CreditoImagenes()`. No lo quites.

Los **datos** del conjunto — nombres, categorías, material e instrucciones en
diez idiomas — son aparte y van bajo licencia MIT.

Ese permiso es del repositorio, no tuyo: para una app que te instalas tú por tu
cuenta, respetando las dos condiciones, esto es exactamente el uso que describe.
Si algún día la subes a Play, saca tu propia licencia en Gym visual — venden los
GIF sueltos a menos de un euro cada uno.

### El vídeo

Cada ejercicio tiene un botón que abre YouTube. Por defecto lanza una búsqueda;
cuando encuentras el vídeo que te gusta le das a **Fijar**, pegas el enlace, y a
partir de ahí ese ejercicio abre siempre ese vídeo. En unas semanas tienes tu
propia videoteca, elegida por ti y guardada en el teléfono.

Buscar abre YouTube por fuera; **ver** el vídeo fijado lo abre dentro de la app,
en el reproductor incrustado de YouTube, sin salir de Fuerza. Y cuando hay vídeo
fijado, el vídeo pasa a ser lo primero que ves en la ficha y el dibujo baja
debajo: el vídeo enseña el movimiento entero, el dibujo funciona sin cobertura.

No se usa ninguna API de vídeo, y eso importa por dos cosas:

1. **No cuesta nada y no caduca.** Se miraron dos APIs de pago (ymove y
   MuscleWiki). Ninguna deja guardar el vídeo en el teléfono — MuscleWiki lo dice
   con todas las letras: *"el vídeo nunca se escribe en disco, almacenamiento de
   objetos ni caché compartida"* — así que de todas formas hacía falta cobertura
   para verlos. Y el plan gratis de MuscleWiki no da clave de API, solo su web.
2. **El autor del vídeo cobra sus visitas.** Bajarse el vídeo de otro y servirlo
   desde tu app es justo lo que no hay que hacer. Con el reproductor incrustado
   la visita se la sigue llevando él.

Hay vídeos que su autor no deja incrustar. Cuando pasa, el reproductor lo dice y
el popup lleva un botón para abrirlo en YouTube.

Las fotos siguen siendo lo primero porque funcionan siempre. El vídeo es el paso
siguiente, para cuando quieres ver el movimiento entero y tienes cobertura.

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
