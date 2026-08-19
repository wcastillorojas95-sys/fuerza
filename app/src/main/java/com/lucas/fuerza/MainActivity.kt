package com.lucas.fuerza

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDate

private enum class Pestana(val etiqueta: String, val simbolo: String) {
    INICIO("Hoy", "⌂"),
    PLAN("Plan", "▤"),
    PROGRESO("Progreso", "◔"),
    EJERCICIOS("Catalogo", "▦"),
    AJUSTES("Ajustes", "⚙")
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Barras transparentes y siempre con iconos claros. enableEdgeToEdge() a
        // secas sigue al tema del sistema, asi que en un movil en modo claro los
        // iconos saldrian oscuros sobre nuestra cabecera negra y no se verian.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        setContent {
            FuerzaTheme { App() }
        }
    }
}

@Composable
private fun App() {
    val contexto = LocalContext.current
    val ajustes = remember { Ajustes(contexto.applicationContext) }
    val almacen = remember { Almacen(contexto.applicationContext) }

    var pestana by remember { mutableStateOf(Pestana.INICIO) }

    /**
     * El contador que obliga a recomponer lo que lee del disco.
     *
     * El almacen no es un flujo observable a proposito -- seria mucha fontaneria
     * para lo que hay que mover -- asi que las pantallas recuerdan sus calculos
     * contra este numero. Sube al terminar un entreno o al cambiar de rutina, y
     * entonces todo se vuelve a leer.
     */
    var refresco by remember { mutableIntStateOf(0) }

    // La sesion en curso, si la hay. Mientras no sea null, ocupa toda la pantalla.
    var enSesion by remember { mutableStateOf<Sesion?>(null) }

    val pedirAvisos = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pedirAvisos.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // ------------------------------------------------------- entreno abierto ---
    val sesion = enSesion
    if (sesion != null) {
        val rutina = sesion.rutinaId?.let { rutinaDe(it) }
        val dia = rutina?.dias?.firstOrNull { it.nombre == sesion.diaNombre }
        PantallaSesion(
            ajustes = ajustes,
            almacen = almacen,
            inicial = sesion,
            rutina = rutina,
            dia = dia,
            onTerminar = { terminada ->
                almacen.guardar(terminada)
                rutina?.let { ajustes.avanzarDia(it.dias.size) }
                enSesion = null
                refresco++
                pestana = Pestana.PROGRESO
            },
            onDescartar = {
                // Si no se anoto nada, la sesion vacia no se queda ocupando sitio.
                if (almacen.abierta()?.vacia == true) almacen.borrar(sesion.id)
                enSesion = null
                refresco++
            }
        )
        return
    }

    // ------------------------------------------------------------ navegacion ---
    Box(Modifier.fillMaxSize().background(Negro)) {
        AnimatedContent(
            targetState = pestana,
            transitionSpec = {
                (fadeIn() + slideInVertically { it / 16 }) togetherWith
                    (fadeOut() + slideOutVertically { -it / 16 }) using SizeTransform(clip = false)
            },
            label = "pestana"
        ) { actual ->
            when (actual) {
                Pestana.INICIO -> PantallaInicio(
                    ajustes = ajustes,
                    almacen = almacen,
                    refresco = refresco,
                    onEmpezar = { enSesion = nuevaSesion(ajustes, almacen) },
                    onRetomar = { enSesion = almacen.abierta() },
                    onIrPlan = { pestana = Pestana.PLAN }
                )

                Pestana.PLAN -> PantallaPlan(
                    ajustes = ajustes,
                    refresco = refresco,
                    onCambio = { refresco++ }
                )

                Pestana.PROGRESO -> PantallaProgreso(almacen = almacen, refresco = refresco)

                Pestana.EJERCICIOS -> PantallaEjercicios(almacen = almacen)

                Pestana.AJUSTES -> PantallaAjustes(
                    ajustes = ajustes,
                    almacen = almacen,
                    refresco = refresco
                )
            }
        }

        Pildora(
            actual = pestana,
            onElegir = { pestana = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * Empieza un entreno.
 *
 * Si quedaba uno abierto se retoma ese en vez de crear otro: dos sesiones
 * abiertas a la vez no significan nada y complican todas las consultas.
 */
private fun nuevaSesion(ajustes: Ajustes, almacen: Almacen): Sesion {
    almacen.abierta()?.let { return it }

    val rutina = ajustes.rutinaId?.let { rutinaDe(it) }
    val dia = rutina?.dias?.getOrNull(ajustes.diaIndice % rutina.dias.size.coerceAtLeast(1))
    val sesion = Sesion(
        id = System.currentTimeMillis(),
        fecha = LocalDate.now().toString(),
        rutinaId = rutina?.id,
        diaNombre = dia?.nombre ?: "Sesion libre",
        ejercicios = dia?.bloques?.map { EjercicioSesion(it.ejercicioId, emptyList()) } ?: emptyList(),
        duracionSeg = 0L,
        terminada = false
    )
    almacen.guardar(sesion)
    return sesion
}

/**
 * La pildora flotante de navegacion.
 *
 * Cinco pestanas es el limite: con la etiqueta desplegada solo en la activa,
 * caben en un movil estrecho sin que los simbolos se toquen.
 */
@Composable
private fun Pildora(actual: Pestana, onElegir: (Pestana) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .navigationBarsPadding()
            .padding(bottom = 14.dp)
            .clip(RoundedCornerShape(50))
            .background(CarbonAlto)
            .padding(horizontal = 6.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Pestana.entries.forEach { p ->
            val activa = p == actual
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (activa) Rojo else Color.Transparent)
                    .clickable { onElegir(p) }
                    .padding(horizontal = if (activa) 15.dp else 13.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = p.simbolo,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (activa) Color.White else Humo
                )
                if (activa) {
                    Spacer(Modifier.width(7.dp))
                    Text(
                        text = p.etiqueta,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}
