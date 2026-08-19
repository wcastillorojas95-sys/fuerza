package com.lucas.fuerza

import android.os.Build
import android.os.Bundle
import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDate

private enum class Pestana(val etiqueta: String, @DrawableRes val icono: Int) {
    INICIO("Inicio", R.drawable.ic_nav_home),
    PLAN("Plan", R.drawable.ic_nav_plan),
    PROGRESO("Progreso", R.drawable.ic_nav_progress),
    EJERCICIOS("Ejercicios", R.drawable.ic_nav_exercises),
    AJUSTES("Ajustes", R.drawable.ic_nav_settings)
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Barras transparentes con iconos oscuros, coherentes con el tema claro.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
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
    // Se leen al arrancar, no al abrir el Plan: si sigues una rutina tuya, la
    // portada tiene que saber cual es antes de que toques nada.
    val biblioteca = remember { Biblioteca(contexto.applicationContext).also { it.mias() } }

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

    /**
     * El dia de rutina que estas mirando antes de entrenarlo.
     *
     * Se guarda por nombre y no por objeto para que sobreviva a un cambio de
     * rutina sin arrastrar un dia que ya no existe: si no se encuentra, la
     * pantalla simplemente no se abre.
     */
    var diaAbierto by remember { mutableStateOf<Pair<String, String>?>(null) }

    /** true mientras estas montando una rutina tuya. */
    var creandoRutina by remember { mutableStateOf(false) }

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

    // ---------------------------------------------------------- rutina nueva ---
    if (creandoRutina) {
        PantallaCrearRutina(
            biblioteca = biblioteca,
            onGuardada = {
                creandoRutina = false
                refresco++
                pestana = Pestana.PLAN
            },
            onCerrar = { creandoRutina = false }
        )
        return
    }

    // ------------------------------------------------------- dia por delante ---
    val abierto = diaAbierto
    val rutinaAbierta = abierto?.let { rutinaDe(it.first) }
    val diaEnPantalla = rutinaAbierta?.dias?.firstOrNull { it.nombre == abierto.second }
    if (rutinaAbierta != null && diaEnPantalla != null) {
        PantallaDia(
            rutina = rutinaAbierta,
            dia = diaEnPantalla,
            almacen = almacen,
            onVolver = { diaAbierto = null },
            onComenzar = {
                enSesion = nuevaSesion(ajustes, almacen, rutinaAbierta, diaEnPantalla)
                diaAbierto = null
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
                    onIrPlan = { pestana = Pestana.PLAN },
                    onCrearRutina = { creandoRutina = true }
                )

                Pestana.PLAN -> PantallaPlan(
                    ajustes = ajustes,
                    biblioteca = biblioteca,
                    refresco = refresco,
                    onCambio = { refresco++ },
                    onAbrirDia = { r, d -> diaAbierto = r.id to d.nombre },
                    onCrearRutina = { creandoRutina = true }
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
 * Sin [rutinaElegida] hace el dia que toca hoy, que es lo que pide la portada.
 * Con ella, el dia concreto que hayas abierto desde el plan.
 *
 * Si quedaba uno abierto se retoma ese en vez de crear otro: dos sesiones
 * abiertas a la vez no significan nada y complican todas las consultas. La
 * pantalla del dia avisa de eso en el boton antes de que lo toques.
 */
private fun nuevaSesion(
    ajustes: Ajustes,
    almacen: Almacen,
    rutinaElegida: Rutina? = null,
    diaElegido: DiaRutina? = null
): Sesion {
    almacen.abierta()?.let { return it }

    val rutina = rutinaElegida ?: ajustes.rutinaId?.let { rutinaDe(it) }
    val dia = diaElegido
        ?: rutina?.dias?.getOrNull(ajustes.diaIndice % rutina.dias.size.coerceAtLeast(1))
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
 * Barra inferior clara con los cinco destinos siempre visibles.
 */
@Composable
private fun Pildora(actual: Pestana, onElegir: (Pestana) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Carbon)
            .navigationBarsPadding()
            .padding(start = 4.dp, top = 8.dp, end = 4.dp, bottom = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Pestana.entries.forEach { p ->
            val activa = p == actual
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onElegir(p) }
                    .padding(vertical = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconoSvg(
                    recurso = p.icono,
                    descripcion = p.etiqueta,
                    color = if (activa) Rojo else HumoTenue,
                    modifier = Modifier.size(22.dp)
                )
                Box(Modifier.height(3.dp))
                Text(
                    text = p.etiqueta,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (activa) Rojo else HumoTenue
                )
            }
        }
    }
}
