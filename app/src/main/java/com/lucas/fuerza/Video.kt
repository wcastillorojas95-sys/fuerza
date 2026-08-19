package com.lucas.fuerza

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * El video de cada ejercicio, sin que la app toque internet.
 *
 * Aqui no hay ninguna API ni ninguna clave: al tocar "Ver video" se lanza un
 * Intent y es YouTube quien abre el video. Eso tiene tres consecuencias buenas
 * que conviene entender, porque es lo que hace que esta solucion gane a pagar
 * una API:
 *
 *  1. La app sigue sin permiso de INTERNET. Lanzar un Intent no lo necesita --
 *     quien se conecta es YouTube, no nosotros -- asi que la promesa de que
 *     nada de lo que guardas puede salir del telefono se mantiene intacta.
 *  2. No cuesta nada y no caduca. Ninguna API de video deja guardarlos en el
 *     telefono, asi que de todas formas hacia falta cobertura para verlos.
 *  3. El autor del video cobra sus visitas. Descargar el video de otro y
 *     servirlo desde tu app es justo lo que no hay que hacer.
 *
 * Por defecto el boton abre una busqueda en YouTube. Cuando encuentras el video
 * que te gusta, lo fijas pegando su enlace y a partir de ahi ese ejercicio abre
 * siempre ese video. Al cabo de unas semanas tienes tu propia videoteca,
 * elegida por ti y guardada en el telefono.
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

/**
 * La fila del video: un boton para verlo y otro para fijar cual.
 *
 * Va debajo de la demostracion en fotos. Las fotos siguen siendo lo primero
 * porque funcionan siempre; el video es el paso siguiente, para cuando quieres
 * ver el movimiento entero y tienes cobertura.
 */
@Composable
fun FilaVideo(ejercicio: Ejercicio, modifier: Modifier = Modifier) {
    val contexto = LocalContext.current
    val videos = remember { Videos(contexto) }
    var fijado by remember(ejercicio.id) { mutableStateOf(videos.fijadoDe(ejercicio.id)) }
    var fijando by remember { mutableStateOf(false) }

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(50))
                .background(if (fijado != null) Rojo else CarbonAlto)
                .clickable { Videos.abrir(contexto, ejercicio, fijado) }
                .padding(horizontal = 18.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "▶",
                style = MaterialTheme.typography.bodyMedium,
                color = if (fijado != null) Color.White else Rojo
            )
            Spacer(Modifier.width(10.dp))
            Text(
                if (fijado != null) "VER VIDEO" else "BUSCAR VIDEO EN YOUTUBE",
                style = MaterialTheme.typography.labelLarge,
                color = if (fijado != null) Color.White else Humo
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
                if (fijado != null) "CAMBIAR" else "FIJAR",
                style = MaterialTheme.typography.labelLarge,
                color = Humo
            )
        }
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
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
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
