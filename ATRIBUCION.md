# Atribución

## Vídeos de demostración

Los 103 archivos `.mp4` de `res/raw/` son de **MuscleWiki** — llevan su marca
dentro del propio vídeo y no se recorta. Las miniaturas `vm_*.webp` se extraen
de esos mismos vídeos.

Hay que decirlo claro: los [términos de su API](https://api.musclewiki.com/api-terms)
prohíben descargar y re-alojar sus vídeos, y esto es exactamente eso. Este APK
se instala a mano en un teléfono y no se distribuye, así que es un asunto entre
su dueño y ellos; **para publicar la app en cualquier tienda esto es un bloqueo
que hay que resolver antes**, sacando licencia de su API o grabando los vídeos.

El mecanismo que los reproduce sirve para cualquier `.mp4`, incluido uno grabado
por ti. Sustituir los archivos no toca ni una línea de código.

## Datos del catálogo

Los pasos de ejecución se adaptaron y resumieron en español a partir de las
páginas de ejercicios de MuscleWiki. Los nombres, las claves de técnica y las
rutinas están ajustados para esta app.

## Iconos

Todos vienen de [SVG Repo](https://www.svgrepo.com/), adaptados a Android Vector
Drawable y tintados desde Compose.

Cinco son de la primera tanda y están bajo **CC0**:

- [Dumbbell](https://www.svgrepo.com/svg/287300/dumbbell) — pestaña de Ejercicios
- [Search](https://www.svgrepo.com/svg/450490/search)
- [Close](https://www.svgrepo.com/svg/431530/close)
- [Arrow right](https://www.svgrepo.com/svg/500330/arrow-right)
- [Play](https://www.svgrepo.com/svg/159868/play)

Los otros diez se descargaron aparte: inicio, plan, progreso, ajustes, hecho,
más, menos y los tres objetivos de la pantalla de ajustes. **Su licencia no está
verificada aquí**: SVG Repo mezcla CC0, MIT y algunas de atribución según quién
subiera el archivo. Para una app que te instalas tú da igual; si algún día va a
una tienda, comprueba la ficha de cada uno.

## Tipografías

- **Anton** — SIL Open Font License 1.1 (`tipografias/OFL-Anton.txt`). Titulares
  y cifras.
- **Figtree** — SIL Open Font License 1.1 (`tipografias/OFL-Figtree.txt`). Texto,
  botones y etiquetas.

Los `.ttf` del proyecto van **recortados** a los caracteres que la app escribe,
no son los archivos originales. La OFL permite modificar y redistribuir, y
ninguna de las dos declara nombre de fuente reservado en su aviso de copyright,
así que conservan su nombre. Las licencias completas, tal cual vienen, están en
`tipografias/`.

## Vídeos opcionales de YouTube

Además de la demostración local, el botón para fijar un vídeo abre YouTube
mediante un Intent o su reproductor incrustado. La reproducción, la publicidad y
las visitas de ese vídeo opcional siguen perteneciendo a YouTube y a su autor.
