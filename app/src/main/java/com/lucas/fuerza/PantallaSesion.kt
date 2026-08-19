package com.lucas.fuerza

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.DisposableEffect
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * La pantalla donde de verdad se usa la app.
 *
 * Todo lo demas -- el catalogo, las rutinas, las graficas -- existe para que
 * esta funcione. Aqui estas de pie, con el movil en una mano, entre dos series,
 * y lo unico que importa es apuntar rapido lo que acabas de hacer y saber
 * cuanto queda de descanso.
 *
 * De ahi las tres decisiones de diseno de la pantalla:
 *
 *  1. Cada ejercicio muestra lo que hiciste la ultima vez. Es el dato que nadie
 *     recuerda y el que decide el peso de hoy.
 *  2. Los pesos se ajustan con botones de mas y menos ademas de con el teclado.
 *     Escribir "62.5" con los dedos sudados y prisa es tres veces mas lento.
 *  3. El descanso arranca solo al anotar una serie. Un temporizador que hay que
 *     acordarse de encender no lo usa nadie a partir de la tercera serie.
 */
@Composable
fun PantallaSesion(
    ajustes: Ajustes,
    almacen: Almacen,
    inicial: Sesion,
    rutina: Rutina?,
    dia: DiaRutina?,
    onTerminar: (Sesion) -> Unit,
    onDescartar: () -> Unit
) {
    val contexto = LocalContext.current
    var ejercicios by remember { mutableStateOf(inicial.ejercicios) }
    var eligiendo by remember { mutableStateOf(false) }
    var confirmandoSalida by remember { mutableStateOf(false) }

    // Reloj de la sesion y del descanso. Un solo tic para los dos.
    var ahora by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) { delay(500); ahora = System.currentTimeMillis() }
    }

    var finDescanso by remember { mutableLongStateOf(0L) }
    var totalDescanso by remember { mutableIntStateOf(0) }
    val restante = ((finDescanso - ahora) / 1000L).toInt()
    val descansando = finDescanso > 0L && restante > 0

    // Cuando el descanso llega a cero, el aviso ya lo ha dado AlarmManager;
    // aqui solo se limpia el estado para que la barra desaparezca.
    LaunchedEffect(descansando) {
        if (finDescanso > 0L && !descansando) { delay(1500); finDescanso = 0L }
    }

    // La pantalla no se apaga mientras entrenas, si asi lo has pedido.
    val vista = LocalView.current
    DisposableEffect(ajustes.pantallaEncendida) {
        vista.keepScreenOn = ajustes.pantallaEncendida
        onDispose { vista.keepScreenOn = false }
    }

    fun guardar(nuevos: List<EjercicioSesion>) {
        ejercicios = nuevos
        almacen.guardar(
            inicial.copy(
                ejercicios = nuevos,
                duracionSeg = (System.currentTimeMillis() - inicial.id) / 1000L,
                terminada = false
            )
        )
    }

    fun arrancarDescanso(segundos: Int, nombreSiguiente: String?) {
        totalDescanso = segundos
        finDescanso = System.currentTimeMillis() + segundos * 1000L
        if (ajustes.avisarDescanso) AvisoDescanso.programar(contexto, segundos, nombreSiguiente)
    }

    fun pararDescanso() {
        finDescanso = 0L
        AvisoDescanso.cancelar(contexto)
    }

    val minutos = ((ahora - inicial.id) / 60000L).toInt()
    val seriesHechas = ejercicios.sumOf { it.series.size }
    val volumen = ejercicios.sumOf { it.volumen }

    Box(Modifier.fillMaxSize().background(Negro)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(
                start = 18.dp, end = 18.dp, top = 10.dp,
                bottom = if (descansando) 190.dp else 110.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ------------------------------------------------------ cabecera ---
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Etiqueta(if (rutina != null) rutina.nombre else "Sesion libre", color = Rojo)
                        Spacer(Modifier.height(6.dp))
                        Titular(
                            dia?.nombre ?: "Entreno",
                            estilo = MaterialTheme.typography.displaySmall
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(CarbonAlto)
                            .clickable { confirmandoSalida = true }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text("Salir", style = MaterialTheme.typography.titleSmall, color = Humo)
                    }
                }
                Spacer(Modifier.height(14.dp))
                Tarjeta(borde = true, color = Carbon) {
                    FilaDatos(
                        listOf(
                            "Tiempo" to "$minutos min",
                            "Series" to "$seriesHechas",
                            "Volumen" to "${volumen.roundToInt()} kg"
                        )
                    )
                }
            }

            // ----------------------------------------------------- ejercicios ---
            itemsIndexed(ejercicios) { indice, ejercicio ->
                val bloque = dia?.bloques?.firstOrNull { it.ejercicioId == ejercicio.ejercicioId }
                TarjetaEjercicio(
                    ejercicio = ejercicio,
                    bloque = bloque,
                    ultimaVez = remember(ejercicio.ejercicioId) {
                        almacen.ultimaVezDe(ejercicio.ejercicioId, inicial.id)
                    },
                    record = remember(ejercicio.ejercicioId, ejercicio.series.size) {
                        almacen.recordDe(ejercicio.ejercicioId)
                    },
                    incremento = ajustes.incrementoKg,
                    onAnotar = { serie ->
                        val nuevos = ejercicios.toMutableList()
                        nuevos[indice] = ejercicio.copy(series = ejercicio.series + serie)
                        guardar(nuevos)
                        arrancarDescanso(
                            bloque?.descansoSeg ?: 90,
                            ejercicioDe(ejercicio.ejercicioId)?.nombre
                        )
                    },
                    onBorrarSerie = { j ->
                        val nuevos = ejercicios.toMutableList()
                        nuevos[indice] = ejercicio.copy(
                            series = ejercicio.series.filterIndexed { k, _ -> k != j }
                        )
                        guardar(nuevos)
                    },
                    onQuitar = {
                        guardar(ejercicios.filterIndexed { k, _ -> k != indice })
                    }
                )
            }

            item {
                BotonBorde("Anadir ejercicio", onClick = { eligiendo = true })
            }
        }

        // ------------------------------------------------ barra de descanso ---
        if (descansando) {
            BarraDescanso(
                restante = restante,
                total = totalDescanso,
                onMas = { finDescanso += 30_000L; AvisoDescanso.programar(contexto, restante + 30, null) },
                onSaltar = { pararDescanso() },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                BotonRojo(
                    texto = "Terminar entreno",
                    habilitado = seriesHechas > 0,
                    onClick = {
                        pararDescanso()
                        onTerminar(
                            inicial.copy(
                                ejercicios = ejercicios,
                                duracionSeg = (System.currentTimeMillis() - inicial.id) / 1000L,
                                terminada = true
                            )
                        )
                    }
                )
            }
        }
    }

    if (eligiendo) {
        SelectorEjercicio(
            yaPuestos = ejercicios.map { it.ejercicioId }.toSet(),
            onElegir = { id ->
                eligiendo = false
                guardar(ejercicios + EjercicioSesion(id, emptyList()))
            },
            onCerrar = { eligiendo = false }
        )
    }

    if (confirmandoSalida) {
        AlertDialog(
            onDismissRequest = { confirmandoSalida = false },
            containerColor = Carbon,
            title = { Text("Dejar el entreno", style = MaterialTheme.typography.headlineSmall, color = Color.White) },
            text = {
                Text(
                    if (seriesHechas > 0)
                        "Las $seriesHechas series que llevas quedan guardadas. Puedes retomar el entreno donde lo dejaste."
                    else
                        "No has anotado ninguna serie, asi que no se guarda nada.",
                    style = MaterialTheme.typography.bodyMedium, color = Humo
                )
            },
            confirmButton = {
                TextButton(onClick = { pararDescanso(); confirmandoSalida = false; onDescartar() }) {
                    Text("Salir", color = Rojo)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmandoSalida = false }) { Text("Seguir", color = Humo) }
            }
        )
    }
}

/**
 * La tarjeta de un ejercicio dentro del entreno.
 *
 * El editor de la serie siguiente va siempre desplegado abajo, no escondido tras
 * un boton: entre serie y serie no hay paciencia para dos toques.
 */
@Composable
private fun TarjetaEjercicio(
    ejercicio: EjercicioSesion,
    bloque: Bloque?,
    ultimaVez: EjercicioSesion?,
    record: Serie?,
    incremento: Double,
    onAnotar: (Serie) -> Unit,
    onBorrarSerie: (Int) -> Unit,
    onQuitar: () -> Unit
) {
    val datos = ejercicioDe(ejercicio.ejercicioId)
    val sugerida = ejercicio.series.lastOrNull()
        ?: ultimaVez?.series?.lastOrNull()
        ?: Serie(20.0, bloque?.repsMin ?: 10)

    // El campo guarda texto, no numeros. Si guardara un Double y lo volviera a
    // pintar en cada tecla, escribir "62.5" seria imposible: en cuanto tecleas el
    // punto, 62. se parsea a 62.0 y se repinta como "62", borrandote el punto.
    var kgTexto by remember(ejercicio.ejercicioId) { mutableStateOf(limpio(sugerida.kg)) }
    var repsTexto by remember(ejercicio.ejercicioId) { mutableStateOf(sugerida.reps.toString()) }

    val kg = kgTexto.replace(',', '.').toDoubleOrNull() ?: 0.0
    val reps = repsTexto.toIntOrNull() ?: 0

    // La demostracion va plegada por defecto. Cuando ya te sabes el ejercicio
    // estorba, y en una sesion de seis ejercicios son seis imagenes grandes que
    // te obligan a bajar media pantalla para llegar al siguiente.
    var demoAbierta by remember(ejercicio.ejercicioId) { mutableStateOf(false) }

    Tarjeta(borde = true) {
        Row(verticalAlignment = Alignment.Top) {
            // La miniatura quieta hace de boton: al tocarla se abre la
            // demostracion en grande y empieza a moverse.
            DemoEjercicio(
                ejercicioId = ejercicio.ejercicioId,
                animar = false,
                radio = 12.dp,
                modifier = Modifier
                    .width(78.dp)
                    .height(52.dp)
                    .clickable { demoAbierta = !demoAbierta }
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Titular(
                    datos?.nombre ?: ejercicio.ejercicioId,
                    estilo = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = buildString {
                        if (bloque != null) append("${bloque.series} series x ${bloque.rango}   ")
                        append(datos?.musculo?.etiqueta ?: "")
                        datos?.let { append(" - ${it.equipo.etiqueta}") }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = HumoTenue
                )
            }
            Text(
                text = "x",
                style = MaterialTheme.typography.titleMedium,
                color = HumoTenue,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { onQuitar() }
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }

        if (demoAbierta) {
            Spacer(Modifier.height(12.dp))
            DemoEjercicio(
                ejercicioId = ejercicio.ejercicioId,
                animar = true,
                modifier = Modifier.fillMaxWidth().height(190.dp)
            )
            if (datos != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    datos.claves,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Humo
                )
                Spacer(Modifier.height(14.dp))
                FilaVideo(datos)
            }
        }

        // Lo que hiciste la ultima vez, que es el dato que decide el peso de hoy.
        val ultima = ultimaVez?.series
        if (!ultima.isNullOrEmpty()) {
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Ultima vez: " + ultima.joinToString("  ") { "${limpio(it.kg)}x${it.reps}" },
                style = MaterialTheme.typography.bodySmall,
                color = Humo
            )
        }

        // Las series de hoy.
        if (ejercicio.series.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            ejercicio.series.forEachIndexed { i, s ->
                val esRecord = record != null && s.rmEstimado >= record.rmEstimado
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CarbonAlto)
                        .clickable { onBorrarSerie(i) }
                        .padding(horizontal = 12.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${i + 1}",
                        style = MaterialTheme.typography.labelLarge,
                        color = HumoTenue,
                        modifier = Modifier.width(22.dp)
                    )
                    Text(
                        "${limpio(s.kg)} kg",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "${s.reps} reps",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White
                    )
                    if (esRecord) {
                        Spacer(Modifier.width(8.dp))
                        Text("PR", style = MaterialTheme.typography.labelLarge, color = Verde)
                    }
                }
            }
        }

        // ------------------------------------------------ editor de la serie ---
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Campo(
                etiqueta = "Kg",
                valor = kgTexto,
                onValor = { t -> if (t.length <= 6 && t.all { it.isDigit() || it == '.' || it == ',' }) kgTexto = t },
                onMenos = { kgTexto = limpio((kg - incremento).coerceAtLeast(0.0)) },
                onMas = { kgTexto = limpio(kg + incremento) },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(10.dp))
            Campo(
                etiqueta = "Reps",
                valor = repsTexto,
                onValor = { t -> if (t.length <= 3 && t.all { it.isDigit() }) repsTexto = t },
                onMenos = { repsTexto = (reps - 1).coerceAtLeast(0).toString() },
                onMas = { repsTexto = (reps + 1).toString() },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (reps > 0) Rojo else CarbonAlto)
                    .clickable(enabled = reps > 0) { onAnotar(Serie(kg, reps)) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "OK",
                    style = MaterialTheme.typography.titleSmall,
                    color = if (reps > 0) Color.White else HumoTenue
                )
            }
        }
    }
}

/** Un campo numerico con sus botones de menos y mas a los lados. */
@Composable
private fun Campo(
    etiqueta: String,
    valor: String,
    onValor: (String) -> Unit,
    onMenos: () -> Unit,
    onMas: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Etiqueta(etiqueta)
        Spacer(Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CarbonAlto),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "-",
                style = MaterialTheme.typography.headlineMedium,
                color = Humo,
                modifier = Modifier.clickable { onMenos() }.padding(horizontal = 13.dp, vertical = 12.dp)
            )
            BasicTextField(
                value = valor,
                onValueChange = onValor,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                cursorBrush = SolidColor(Rojo),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Text(
                "+",
                style = MaterialTheme.typography.headlineMedium,
                color = Humo,
                modifier = Modifier.clickable { onMas() }.padding(horizontal = 13.dp, vertical = 12.dp)
            )
        }
    }
}

/** La barra de descanso, anclada abajo mientras corre la cuenta atras. */
@Composable
private fun BarraDescanso(
    restante: Int,
    total: Int,
    onMas: () -> Unit,
    onSaltar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(CarbonAlto)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                Aro(
                    fraccion = if (total > 0) restante / total.toFloat() else 0f,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    "$restante",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Etiqueta("Descanso", color = Rojo)
                Spacer(Modifier.height(3.dp))
                Text(
                    "Te aviso aunque apagues la pantalla",
                    style = MaterialTheme.typography.bodySmall,
                    color = Humo
                )
            }
            Text(
                "+30",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Carbon)
                    .clickable { onMas() }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Saltar",
                style = MaterialTheme.typography.titleSmall,
                color = Rojo,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { onSaltar() }
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            )
        }
    }
}

/** 62.5 se escribe "62.5" y 60.0 se escribe "60". */
fun limpio(kg: Double): String =
    if (kg % 1.0 == 0.0) kg.toInt().toString() else ((kg * 10).roundToInt() / 10.0).toString()
