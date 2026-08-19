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

## Por qué no hay vídeos

La alternativa era una API de pago con vídeos HD. Se descartó por tres motivos
que solo se ven leyendo la letra pequeña:

1. **Tope de ejercicios únicos al mes.** Los planes de entrada desbloquean entre
   250 y 350 ejercicios distintos al mes de un catálogo de más de mil, y el
   contador se reinicia cada mes.
2. **Las URLs de vídeo caducan a las 48 horas y la documentación prohíbe
   cachearlas.** Traducido: la app necesitaría internet cada vez que abres un
   ejercicio.
3. **El uso sin conexión solo está en el plan de 79 dólares al mes**, casi
   novecientos al año antes de tener un solo usuario.

El catálogo de esta app va compilado dentro del APK: 83 ejercicios con su grupo
muscular, su material y dos frases de técnica. Los nombres y los músculos que
trabaja cada movimiento son hechos, no obra de nadie; las descripciones están
escritas para esta app.

Si algún día quieres vídeo, el sitio por donde entra es `Ejercicios.kt`: cambias
de dónde sale la lista y el resto de la app no se entera.

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
