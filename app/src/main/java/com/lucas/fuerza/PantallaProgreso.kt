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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import kotlin.math.roundToInt

private val LETRAS_SEMANA = listOf("L", "M", "X", "J", "V", "S", "D")

/**
 * Cuanto has progresado.
 *
 * De los tres numeros que se pueden ensenar aqui -- volumen, series y record --
 * el que de verdad importa es el tercero. El volumen sube solo con hacer mas
 * series, y hacer mas series no es lo mismo que ser mas fuerte. Por eso los
 * records ocupan la mitad de la pantalla y el volumen es una barra pequena.
 */
@Composable
fun PantallaProgreso(almacen: Almacen, refresco: Int) {
    val hoy = remember(refresco) { LocalDate.now() }
    val dias = remember(refresco) { (0L..6L).map { hoy.minusDays(6 - it) } }
    val volumenes = remember(refresco) { dias.map { almacen.volumenDe(it) } }

    val todas = remember(refresco) { almacen.terminadas() }
    val racha = remember(refresco) { almacen.rachaSemanas(hoy) }
    // Se filtran los ids que ya no estan en el catalogo. Solo pasa si algun dia
    // se retira un ejercicio: el historico viejo sigue en disco y no debe romper
    // la pantalla.
    val usados = remember(refresco) {
        almacen.ejerciciosUsados().filter { CATALOGO_POR_ID.containsKey(it) }
    }

    var elegido by remember(refresco) { mutableStateOf(usados.firstOrNull()) }
    val historico = remember(elegido, refresco) {
        elegido?.let { almacen.historicoDe(it) } ?: emptyList()
    }

    Box(Modifier.fillMaxSize().background(Negro)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 130.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            item {
                Titular("Progreso", estilo = MaterialTheme.typography.displayMedium)
                Spacer(Modifier.height(4.dp))
                Text(
                    if (todas.isEmpty()) "Cuando termines el primer entreno, esto se llena."
                    else "${todas.size} entrenos registrados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Humo
                )
                Spacer(Modifier.height(6.dp))
            }

            // ------------------------------------------------- los 7 dias ---
            item {
                Tarjeta(borde = true, relleno = 20.dp) {
                    Etiqueta("Los ultimos siete dias", color = Rojo)
                    Spacer(Modifier.height(8.dp))
                    Titular(
                        "${volumenes.sum().roundToInt()} kg",
                        estilo = MaterialTheme.typography.displaySmall
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "${volumenes.count { it > 0.0 }} entrenos - " +
                            "${todas.filter { it.fecha >= dias.first().toString() }.sumOf { it.totalSeries }} series",
                        style = MaterialTheme.typography.bodySmall,
                        color = Humo
                    )
                    Spacer(Modifier.height(20.dp))
                    Barras(
                        valores = volumenes,
                        etiquetas = dias.map { LETRAS_SEMANA[(it.dayOfWeek.value + 6) % 7] },
                        resaltado = 6
                    )
                }
            }

            // --------------------------------------------------- resumenes ---
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Cifra("Racha", if (racha > 0) "$racha" else "0", "semanas", Modifier.weight(1f))
                    Cifra("Entrenos", "${todas.size}", "en total", Modifier.weight(1f))
                    Cifra(
                        "Series",
                        "${todas.sumOf { it.totalSeries }}",
                        "en total",
                        Modifier.weight(1f)
                    )
                }
            }

            // ------------------------------------------------- progresion ---
            if (usados.isNotEmpty()) {
                item {
                    Tarjeta(borde = true, relleno = 20.dp) {
                        Etiqueta("Progresion", color = Rojo)
                        Spacer(Modifier.height(8.dp))
                        Titular(
                            ejercicioDe(elegido ?: "")?.nombre ?: "-",
                            estilo = MaterialTheme.typography.headlineMedium
                        )

                        val record = elegido?.let { almacen.recordDe(it) }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            if (record != null)
                                "Tu mejor serie: ${limpio(record.kg)} kg x ${record.reps} " +
                                    "(maximo estimado ${record.rmEstimado.roundToInt()} kg)"
                            else "Sin series registradas",
                            style = MaterialTheme.typography.bodySmall,
                            color = Humo
                        )

                        if (historico.size >= 2) {
                            Spacer(Modifier.height(18.dp))
                            Chispa(
                                valores = historico.map { it.second.rmEstimado },
                                modifier = Modifier.fillMaxWidth().height(90.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth()) {
                                Text(
                                    historico.first().first.takeLast(5),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = HumoTenue
                                )
                                Spacer(Modifier.weight(1f))
                                Text(
                                    historico.last().first.takeLast(5),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = HumoTenue
                                )
                            }
                        } else {
                            Spacer(Modifier.height(14.dp))
                            Text(
                                "Hacen falta al menos dos sesiones con este ejercicio para " +
                                    "dibujar una linea.",
                                style = MaterialTheme.typography.bodySmall,
                                color = HumoTenue
                            )
                        }
                    }
                }

                item {
                    Etiqueta("Tus records", color = Rojo)
                    Spacer(Modifier.height(4.dp))
                }

                items(usados.size) { i ->
                    val id = usados[i]
                    val e = CATALOGO_POR_ID.getValue(id)
                    val r = almacen.recordDe(id)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (elegido == id) CarbonAlto else Carbon)
                            .clickable { elegido = id }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .width(3.dp).height(28.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (elegido == id) Rojo else Color(0xFF33333D))
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                e.nombre,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                e.musculo.etiqueta,
                                style = MaterialTheme.typography.bodySmall,
                                color = HumoTenue
                            )
                        }
                        if (r != null) {
                            Text(
                                "${limpio(r.kg)} kg x ${r.reps}",
                                style = MaterialTheme.typography.titleSmall,
                                color = Rojo
                            )
                        }
                    }
                }
            }

            // -------------------------------------------------- historial ---
            if (todas.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Etiqueta("Ultimos entrenos", color = Rojo)
                    Spacer(Modifier.height(4.dp))
                }
                items(todas.take(12).size) { i ->
                    val s = todas[i]
                    Tarjeta(borde = true, relleno = 16.dp) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    s.diaNombre.ifBlank { "Sesion libre" },
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    "${s.fecha} - ${s.duracionSeg / 60} min",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = HumoTenue
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "${s.volumen.roundToInt()} kg",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Rojo
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    "${s.totalSeries} series",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = HumoTenue
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Una cifra suelta con su etiqueta arriba y su unidad debajo. */
@Composable
private fun Cifra(etiqueta: String, valor: String, unidad: String, modifier: Modifier = Modifier) {
    Tarjeta(modifier = modifier, borde = true, relleno = 16.dp) {
        Etiqueta(etiqueta)
        Spacer(Modifier.height(6.dp))
        Titular(valor, estilo = MaterialTheme.typography.displaySmall)
        Spacer(Modifier.height(1.dp))
        Text(unidad, style = MaterialTheme.typography.bodySmall, color = HumoTenue)
    }
}
