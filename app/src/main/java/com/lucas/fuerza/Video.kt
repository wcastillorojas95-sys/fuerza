package com.lucas.fuerza

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * El video de cada ejercicio.
 *
 * Aqui no hay ninguna API de video ni ninguna clave. Lo que hay es esto:
 *
 *  1. **Buscar** abre YouTube por fuera, con un Intent. Buscar es cosa de una
 *     vez por ejercicio y ahi la app de YouTube gana a cualquier cosa que
 *     montemos nosotros.
 *  2. **Fijar** guarda el id del video en el telefono. Solo el id, once
 *     caracteres.
 *  3. **Ver** abre el video dentro de Fuerza, en el reproductor incrustado de
 *     YouTube. Sin salir de la app y sin descargar nada: el que reproduce y el
 *     que cobra las visitas sigue siendo YouTube, que es como tiene que ser.
 *
 * Esto ultimo es lo que obligo a anadir el permiso de INTERNET, y no salio
 * gratis: hasta entonces "tus entrenos no pueden salir del telefono" lo
 * garantizaba el sistema operativo, no nosotros. Ahora lo garantiza el codigo,
 * que es menos. Lo que abre la app son dos cosas y ninguna mas: la miniatura
 * del video y el reproductor. Ni analitica, ni cuentas, ni servidor propio.
 *
 * Hay videos que su autor no deja incrustar. Cuando pasa, el reproductor lo
 * dice y el popup lleva un boton para abrirlo en YouTube.
 */
class Videos(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)

    /** El id de YouTube fijado para este ejercicio, o null si no hay ninguno. */
    fun fijadoDe(ejercicioId: String): String? = prefs.getString(ejercicioId, null)

    /**
     * Fija un video. Acepta el enlace completo, el corto, el de shorts o el id
     * pelado, porque nadie sabe cual de los cuatro le va a dar el boton de
     * compartir.
     *
     * Devuelve false si lo pegado no contiene ningun id reconocible.
     */
    fun fijar(ejercicioId: String, pegado: String): Boolean {
        val id = idDe(pegado) ?: return false
        prefs.edit().putString(ejercicioId, id).apply()
        return true
    }

    fun soltar(ejercicioId: String) {
        prefs.edit().remove(ejercicioId).apply()
    }

    companion object {
        private const val ARCHIVO = "fuerza_videos"

        private val FORMAS = Regex(
            "(?:youtu\\.be/|v=|/shorts/|/embed/|/live/)([A-Za-z0-9_-]{11})"
        )
        private val PELADO = Regex("^[A-Za-z0-9_-]{11}$")

        /** Saca el id de YouTube de lo que sea que hayan pegado. */
        fun idDe(texto: String): String? {
            val t = texto.trim()
            FORMAS.find(t)?.let { return it.groupValues[1] }
            return if (PELADO.matches(t)) t else null
        }

        /**
         * Abre el video en YouTube.
         *
         * Se intenta primero con la app de YouTube y, si no esta instalada, se
         * deja que Android elija. Sin el paquete forzado algunos moviles abren
         * el navegador aunque tengan la app, que es peor.
         */
        fun abrir(context: Context, ejercicio: Ejercicio, fijado: String?) {
            val url = if (fijado != null) {
                "https://www.youtube.com/watch?v=$fijado"
            } else {
                val consulta = Uri.encode("${ejercicio.nombre} tecnica ejercicio")
                "https://www.youtube.com/results?search_query=$consulta"
            }
            val base = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            try {
                context.startActivity(Intent(base).setPackage("com.google.android.youtube"))
            } catch (e: ActivityNotFoundException) {
                runCatching { context.startActivity(base) }
            }
        }
    }
}

@Composable
private fun DialogoFijarVideo(
    ejercicio: Ejercicio,
    hayFijado: Boolean,
    onGuardar: (String) -> Boolean,
    onQuitar: () -> Unit,
    onCerrar: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    var malo by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCerrar,
        containerColor = Carbon,
        title = {
            Titular(
                "Fijar video",
                estilo = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Column {
                Text(
                    "Busca en YouTube el video que te guste de ${ejercicio.nombre}, dale a " +
                        "compartir, copia el enlace y pegalo aqui. A partir de ahora este " +
                        "ejercicio abrira siempre ese video.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Humo
                )
                Spacer(Modifier.height(14.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CarbonAlto)
                        .padding(horizontal = 14.dp, vertical = 13.dp)
                ) {
                    if (texto.isEmpty()) {
                        Text(
                            "https://youtu.be/...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = HumoTenue
                        )
                    }
                    BasicTextField(
                        value = texto,
                        onValueChange = { texto = it; malo = false },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = Tinta),
                        cursorBrush = SolidColor(Rojo),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (malo) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Eso no parece un enlace de YouTube.",
                        style = MaterialTheme.typography.bodySmall,
                        color = RojoVivo
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { if (!onGuardar(texto)) malo = true }) {
                Text("Guardar", color = Rojo)
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (hayFijado) {
                    TextButton(onClick = onQuitar) { Text("Quitar", color = Humo) }
                }
                TextButton(onClick = onCerrar) { Text("Cancelar", color = Humo) }
            }
        }
    )
}

// ------------------------------------------------------------------ video ---

/** Lo que ya se ha bajado, para no volver a pedirlo en cada recomposicion. */
private val miniaturas = mutableMapOf<String, ImageBitmap?>()

/**
 * La miniatura del video, o null mientras baja o si no hay cobertura.
 *
 * Se pide a pelo con [java.net.URL] en vez de meter una libreria de imagenes.
 * Son doce lineas y una sola imagen pequena por ejercicio: Coil aqui serian
 * dos megas de dependencia para esto.
 */
@Composable
private fun miniaturaDe(videoId: String): ImageBitmap? {
    var imagen by remember(videoId) { mutableStateOf(miniaturas[videoId]) }
    LaunchedEffect(videoId) {
        if (miniaturas.containsKey(videoId)) return@LaunchedEffect
        val bajada = withContext(Dispatchers.IO) {
            runCatching {
                java.net.URL("https://img.youtube.com/vi/$videoId/hqdefault.jpg")
                    .openStream()
                    .use { BitmapFactory.decodeStream(it) }
                    ?.asImageBitmap()
            }.getOrNull()
        }
        miniaturas[videoId] = bajada
        imagen = bajada
    }
    return imagen
}

/**
 * La tarjeta del video fijado, en el sitio donde antes estaba el dibujo.
 *
 * Cuando has elegido un video, el video manda: es lo que de verdad ensena el
 * movimiento. El dibujo pasa debajo, que es donde sigue siendo util -- funciona
 * sin cobertura y se mira de un vistazo entre serie y serie.
 */
@Composable
private fun TarjetaVideo(videoId: String, onAbrir: () -> Unit, modifier: Modifier = Modifier) {
    val mini = miniaturaDe(videoId)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(16.dp))
            .background(Tinta)
            .clickable { onAbrir() },
        contentAlignment = Alignment.Center
    ) {
        if (mini != null) {
            Image(
                bitmap = mini,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(50))
                .background(Rojo),
            contentAlignment = Alignment.Center
        ) {
            IconoSvg(
                recurso = R.drawable.ic_play,
                descripcion = "Ver el video",
                color = SobreAcento,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/**
 * El video, dentro de la app.
 *
 * Un WebView con el iframe de YouTube. Se carga con [WebView.loadDataWithBaseURL]
 * y no apuntando directamente a la url del embed porque asi la pagina que pide
 * el reproductor viene de youtube.com, que es lo que espera, y de paso el
 * player ocupa la caja entera sin los margenes de la pagina de YouTube.
 */
@Composable
private fun PopupVideo(videoId: String, ejercicio: Ejercicio, onCerrar: () -> Unit) {
    val contexto = LocalContext.current
    Dialog(
        onDismissRequest = onCerrar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Carbon)
                .padding(14.dp)
        ) {
            Text(
                ejercicio.nombre,
                style = MaterialTheme.typography.titleMedium,
                color = Tinta
            )
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.Black)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        WebView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            setBackgroundColor(android.graphics.Color.BLACK)
                            webChromeClient = WebChromeClient()
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            // Sin esto el video espera un toque que ya diste al
                            // abrir el popup, y parece que no funciona.
                            settings.mediaPlaybackRequiresUserGesture = false
                            loadDataWithBaseURL(
                                "https://www.youtube.com",
                                paginaDelVideo(videoId),
                                "text/html",
                                "utf-8",
                                null
                            )
                        }
                    },
                    // Sin esto el audio sigue sonando despues de cerrar.
                    onRelease = { web ->
                        web.loadUrl("about:blank")
                        web.destroy()
                    }
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(CarbonAlto)
                        .clickable { Videos.abrir(contexto, ejercicio, videoId) }
                        .padding(vertical = 13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "ABRIR EN YOUTUBE",
                        style = MaterialTheme.typography.labelLarge,
                        color = Humo
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(Rojo)
                        .clickable { onCerrar() }
                        .padding(vertical = 13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "CERRAR",
                        style = MaterialTheme.typography.labelLarge,
                        color = SobreAcento
                    )
                }
            }
        }
    }
}

/** El html minimo que envuelve al reproductor. */
private fun paginaDelVideo(videoId: String): String = """
    <!doctype html><html><head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>html,body{margin:0;padding:0;height:100%;background:#000}
    iframe{border:0;width:100%;height:100%;display:block}</style>
    </head><body>
    <iframe src="https://www.youtube.com/embed/$videoId?autoplay=1&playsinline=1&rel=0&modestbranding=1"
      allow="autoplay; encrypted-media; picture-in-picture" allowfullscreen></iframe>
    </body></html>
""".trimIndent()

/**
 * La demostracion del ejercicio con su video, si lo hay, y los botones.
 *
 * Va todo junto en un solo sitio porque comparten un estado: cual es el video
 * fijado. Si la tarjeta del video y el boton de fijar vivieran en composables
 * distintos, fijar uno no actualizaria al otro hasta salir y volver a entrar.
 *
 * [entreMedias] es el hueco donde cada pantalla mete lo suyo -- la tecnica, los
 * musculos -- entre el credito de las imagenes y los botones.
 */
@Composable
fun DemostracionYVideo(
    ejercicio: Ejercicio,
    modifier: Modifier = Modifier,
    alto: Dp = 240.dp,
    entreMedias: @Composable ColumnScope.() -> Unit = {}
) {
    val contexto = LocalContext.current
    val videos = remember { Videos(contexto) }
    var fijado by remember(ejercicio.id) { mutableStateOf(videos.fijadoDe(ejercicio.id)) }
    var viendo by remember(ejercicio.id) { mutableStateOf(false) }
    var fijando by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        val video = fijado
        if (video != null) {
            TarjetaVideo(video, onAbrir = { viendo = true })
            Spacer(Modifier.height(10.dp))
        }

        DemoEjercicio(
            ejercicioId = ejercicio.id,
            animar = true,
            modifier = Modifier.fillMaxWidth().height(if (video != null) alto * 0.8f else alto)
        )
        Spacer(Modifier.height(6.dp))
        CreditoImagenes()

        entreMedias()

        Spacer(Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(if (video != null) Rojo else CarbonAlto)
                    .clickable {
                        if (video != null) viendo = true
                        else Videos.abrir(contexto, ejercicio, null)
                    }
                    .padding(horizontal = 18.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconoSvg(
                    recurso = R.drawable.ic_play,
                    descripcion = null,
                    color = if (video != null) SobreAcento else Rojo,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    if (video != null) "VER VIDEO" else "BUSCAR VIDEO EN YOUTUBE",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (video != null) SobreAcento else Humo
                )
            }
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(CarbonAlto)
                    .clickable { fijando = true }
                    .padding(horizontal = 16.dp, vertical = 13.dp)
            ) {
                Text(
                    if (video != null) "CAMBIAR" else "FIJAR",
                    style = MaterialTheme.typography.labelLarge,
                    color = Humo
                )
            }
        }
    }

    val abierto = fijado
    if (viendo && abierto != null) {
        PopupVideo(abierto, ejercicio) { viendo = false }
    }

    if (fijando) {
        DialogoFijarVideo(
            ejercicio = ejercicio,
            hayFijado = fijado != null,
            onGuardar = { pegado ->
                if (videos.fijar(ejercicio.id, pegado)) {
                    fijado = videos.fijadoDe(ejercicio.id)
                    fijando = false
                    true
                } else false
            },
            onQuitar = {
                videos.soltar(ejercicio.id)
                fijado = null
                fijando = false
            },
            onCerrar = { fijando = false }
        )
    }
}
