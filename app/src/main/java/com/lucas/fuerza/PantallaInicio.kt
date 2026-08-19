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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import kotlin.math.roundToInt

private val LETRAS = listOf("L", "M", "X", "J", "V", "S", "D")

/**
 * La portada.
 *
 * Responde a una sola pregunta -- que toca hoy -- y pone el boton para empezar
 * debajo. Todo lo demas que aparece aqui (la semana, la racha, el volumen) esta
 * por debajo del pliegue a proposito: es informacion para el sofa, no para la
 * puerta del gimnasio.
 */
@Composable
fun PantallaInicio(
    ajustes: Ajustes,
    almacen: Almacen,
    refresco: Int,
    onEmpezar: () -> Unit,
    onRetomar: () -> Unit,
    onIrPlan: () -> Unit
) {
    val hoy = remember(refresco) { LocalDate.now() }
    val rutina = remember(refresco) { ajustes.rutinaId?.let { rutinaDe(it) } }
    val dia = remember(refresco) {
        rutina?.dias?.getOrNull(ajustes.diaIndice % rutina.dias.size.coerceAtLeast(1))
    }
    val abierta = remember(refresco) { almacen.abierta() }

    val dias = remember(refresco) { (0L..6L).map { hoy.minusDays(6 - it) } }
    val volumenes = remember(refresco) { dias.map { almacen.volumenDe(it) } }
    val racha = remember(refresco) { almacen.rachaSemanas(hoy) }
    val entrenosSemana = remember(refresco) { volumenes.count { it > 0.0 } }

    Box(Modifier.fillMaxSize().background(Negro)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 130.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ------------------------------------------------------ cabecera ---
            item {
                Cabecera(alto = 300.dp) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LogoF()
                            Spacer(Modifier.width(9.dp))
                            Titular(
                                "Fuerza",
                                estilo = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                diaDeLaSemana(hoy).uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                color = Humo
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Etiqueta(if (rutina != null) "Hoy toca" else "Sin rutina", color = Rojo)
                        Spacer(Modifier.height(8.dp))
                        Titular(
                            dia?.nombre ?: "Sesion libre",
                            estilo = MaterialTheme.typography.displayLarge
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            dia?.foco ?: "Elige una rutina o entrena a tu aire",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Humo
                        )

                        Spacer(Modifier.height(20.dp))
                        FilaDatos(
                            listOf(
                                "Rutina" to (rutina?.nombre?.take(14) ?: "Libre"),
                                "Objetivo" to ajustes.objetivo.corto,
                                "Racha" to if (racha > 0) "$racha sem" else "-"
                            )
                        )
                    }
                }
            }

            // ------------------------------------------- el entreno de hoy ---
            item {
                Column(Modifier.padding(horizontal = 18.dp)) {
                    if (abierta != null) {
                        Tarjeta(color = Rojo) {
                            Etiqueta("Entreno a medias", color = Color.White.copy(alpha = 0.75f))
                            Spacer(Modifier.height(6.dp))
                            Titular(
                                abierta.diaNombre.ifBlank { "Sesion libre" },
                                estilo = MaterialTheme.typography.headlineMedium
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "${abierta.totalSeries} series anotadas. Puedes seguir donde lo dejaste.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                            Spacer(Modifier.height(14.dp))
                            BotonBorde("Retomar", onClick = onRetomar, color = Color.White)
                        }
                        Spacer(Modifier.height(14.dp))
                    }

                    Tarjeta(borde = true) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Etiqueta("El entreno de hoy", color = Rojo)
                            Spacer(Modifier.weight(1f))
                            Text(
                                if (rutina != null) "Cambiar" else "Elegir rutina",
                                style = MaterialTheme.typography.labelLarge,
                                color = Humo,
                                modifier = Modifier.clickable { onIrPlan() }
                            )
                        }
                        Spacer(Modifier.height(14.dp))

                        if (dia == null) {
                            Text(
                                "Todavia no sigues ninguna rutina. Puedes elegir una en Plan, " +
                                    "o empezar una sesion libre y anadir los ejercicios sobre la marcha.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Humo
                            )
                        } else {
                            dia.bloques.forEach { b ->
                                FilaBloque(b)
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))
                    BotonRojo(
                        texto = if (dia != null) "Empezar entreno" else "Empezar sesion libre",
                        onClick = onEmpezar
                    )
                }
            }

            // --------------------------------------------------- la semana ---
            item {
                Column(Modifier.padding(horizontal = 18.dp)) {
                    Tarjeta(borde = true) {
                        Row(verticalAlignment = Alignment.Top) {
                            Column(Modifier.weight(1f)) {
                                Etiqueta("Esta semana", color = Rojo)
                                Spacer(Modifier.height(6.dp))
                                Titular(
                                    "$entrenosSemana de 7 dias",
                                    estilo = MaterialTheme.typography.headlineMedium
                                )
                                Spacer(Modifier.height(3.dp))
                                Text(
                                    "${volumenes.sum().roundToInt()} kg movidos",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Humo
                                )
                            }
                            if (volumenes.count { it > 0.0 } >= 2) {
                                Chispa(
                                    valores = volumenes,
                                    modifier = Modifier.width(96.dp).height(48.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(18.dp))
                        Barras(
                            valores = volumenes,
                            etiquetas = dias.map { LETRAS[(it.dayOfWeek.value + 6) % 7] },
                            resaltado = 6
                        )
                    }
                }
            }
        }
    }
}

/** Una linea del entreno de hoy: nombre a la izquierda, series y reps a la derecha. */
@Composable
private fun FilaBloque(bloque: Bloque) {
    val e = ejercicioDe(bloque.ejercicioId)
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DemoEjercicio(
            ejercicioId = bloque.ejercicioId,
            animar = false,
            radio = 9.dp,
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = e?.nombre ?: bloque.ejercicioId,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${bloque.series} x ${bloque.rango}",
            style = MaterialTheme.typography.labelLarge,
            color = Humo
        )
    }
}

fun diaDeLaSemana(d: LocalDate): String = when ((d.dayOfWeek.value + 6) % 7) {
    0 -> "Lunes"
    1 -> "Martes"
    2 -> "Miercoles"
    3 -> "Jueves"
    4 -> "Viernes"
    5 -> "Sabado"
    else -> "Domingo"
}
