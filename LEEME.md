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

**La app no tiene permiso de INTERNET.** No es una promesa, es una restricción
del sistema: sin ese permiso, nada de lo que guarda puede salir del teléfono.

Es además lo que hace que sirva en un gimnasio de sótano, donde no hay cobertura.

## Las imágenes

Cada ejercicio lleva una **demostración animada de doce cuadros**: cuerpo gris
con los músculos que trabajan en rojo, sobre fondo blanco. Es el mismo estilo que
usan las apps de gimnasio de pago.

Van dentro del APK y funcionan sin cobertura. Los doce cuadros de cada ejercicio
vienen en una sola imagen, uno al lado del otro, y el reproductor dibuja el
trozo que toca: doce cambios por segundo, un ciclo completo cada segundo. Así se
mueve sin añadir un decodificador de GIF ni descomprimir doce bitmaps. Los 82
ejercicios completos ocupan 3 MB.

Los cuadros se cambian secos, sin fundido. Hubo un fundido entre cuadro y cuadro
y hacía **parpadear** la figura: dibujar el cuadro que se va a media opacidad y
encima el que entra también a media deja pasar por el medio el fondo de la
tarjeta, así que doce veces por segundo el dibujo perdía un cuarto de su color.
Un GIF tampoco funde.

Aparecen en tres sitios: miniatura quieta en el entreno de hoy y en cada fila del
catálogo, y animada en grande al tocar la tarjeta de un ejercicio.

De los 83 del catálogo, **colgarse de la barra** se queda sin demostración porque
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

Va con un Intent y no con una API, y eso importa por tres cosas:

1. **La app sigue sin permiso de INTERNET.** Lanzar un Intent no lo necesita:
   quien se conecta es YouTube, no nosotros.
2. **No cuesta nada y no caduca.** Se miraron dos APIs de pago (ymove y
   MuscleWiki). Ninguna deja guardar el vídeo en el teléfono — MuscleWiki lo dice
   con todas las letras: *"el vídeo nunca se escribe en disco, almacenamiento de
   objetos ni caché compartida"* — así que de todas formas hacía falta cobertura
   para verlos. Y el plan gratis de MuscleWiki no da clave de API, solo su web.
3. **El autor del vídeo cobra sus visitas.** Bajarse el vídeo de otro y servirlo
   desde tu app es justo lo que no hay que hacer.

Las fotos siguen siendo lo primero porque funcionan siempre. El vídeo es el paso
siguiente, para cuando quieres ver el movimiento entero y tienes cobertura.

## La tipografía

Dos letras, cada una en lo suyo:

- **Bricolage Grotesque** en los titulares y en las cifras grandes — el nombre
  del día, los kilos de la semana, la cuenta atrás del descanso. Tiene arista, y
  eso es lo que evita que un titular parezca un párrafo grande.
- **Google Sans** en todo lo demás: textos, botones y etiquetas. Es la letra de
  interfaz que ya lees a diario sin darte cuenta, y eso es exactamente lo que se
  le pide a la letra con la que consultas una técnica a media serie.

Las dos son SIL Open Font License y sus licencias van en `tipografias/`.

Los `.ttf` de Google Sans vienen de fábrica con 2 MB cada uno porque traen medio
alfabeto del mundo. En el APK van **recortados** a lo que la app escribe de
verdad — latino, puntuación y cuatro símbolos — y bajan a 91 KB. Los cuatro
archivos suman 342 KB en vez de 8 MB. Si alguna vez aparece un carácter fuera
del recorte, lo dibuja la letra del sistema; no se rompe nada.

Va solo el grosor que se usa. Un grosor de más son 70 KB dentro del APK que
nadie llega a ver nunca.

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
