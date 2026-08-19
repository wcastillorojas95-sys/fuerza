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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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

/**
 * Los ajustes, que son cuatro.
 *
 * Una app con veinte interruptores es una app que no decidio nada. Los que hay
 * aqui son los unicos que cambian de verdad como se usa: si avisa el descanso,
 * si la pantalla se queda encendida, de cuanto en cuanto suben los discos y para
 * que entrenas.
 */
@Composable
fun PantallaAjustes(ajustes: Ajustes, almacen: Almacen, refresco: Int) {
    var avisar by remember(refresco) { mutableStateOf(ajustes.avisarDescanso) }
    var pantalla by remember(refresco) { mutableStateOf(ajustes.pantallaEncendida) }
    var incremento by remember(refresco) { mutableStateOf(ajustes.incrementoKg) }
    var objetivo by remember(refresco) { mutableStateOf(ajustes.objetivo) }

    Box(Modifier.fillMaxSize().background(Negro)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 130.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Titular("Ajustes", estilo = MaterialTheme.typography.displayMedium)
                Spacer(Modifier.height(10.dp))
            }

            item {
                Tarjeta(borde = true, relleno = 20.dp) {
                    Etiqueta("Para que entrenas", color = Rojo)
                    Spacer(Modifier.height(12.dp))
                    Objetivo.entries.forEach { o ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (objetivo == o) CarbonAlto else Color.Transparent)
                                .clickable { objetivo = o; ajustes.objetivo = o }
                                .padding(horizontal = 14.dp, vertical = 13.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                o.etiqueta,
                                style = MaterialTheme.typography.titleSmall,
                                color = if (objetivo == o) Tinta else Humo,
                                modifier = Modifier.weight(1f)
                            )
                            if (objetivo == o) {
                                IconoSvg(
                                    recurso = R.drawable.ic_check,
                                    descripcion = "Seleccionado",
                                    color = Rojo,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Tarjeta(borde = true, relleno = 20.dp) {
                    Interruptor(
                        titulo = "Avisar al terminar el descanso",
                        detalle = "Vibra y saca una notificacion aunque tengas la pantalla apagada. " +
                            "Usa el despertador del sistema, por eso aparece el icono de alarma arriba.",
                        activo = avisar
                    ) { avisar = it; ajustes.avisarDescanso = it }

                    Spacer(Modifier.height(18.dp))

                    Interruptor(
                        titulo = "Mantener la pantalla encendida",
                        detalle = "Solo durante el entreno. Evita desbloquear el movil entre serie y serie.",
                        activo = pantalla
                    ) { pantalla = it; ajustes.pantallaEncendida = it }
                }
            }

            item {
                Tarjeta(borde = true, relleno = 20.dp) {
                    Etiqueta("Salto de peso", color = Rojo)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Cuanto suma y resta cada toque en los botones de kilos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Humo
                    )
                    Spacer(Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(1.0, 2.5, 5.0).forEach { v ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (incremento == v) Rojo else CarbonAlto)
                                    .clickable { incremento = v; ajustes.incrementoKg = v }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${limpio(v)} kg",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (incremento == v) SobreAcento else Humo
                                )
                            }
                        }
                    }
                }
            }

            item {
                Tarjeta(borde = true, relleno = 20.dp) {
                    Etiqueta("Tus datos", color = Rojo)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Fuerza no tiene permiso de internet. No es una promesa, es una " +
                            "restriccion del sistema: sin ese permiso, nada de lo que guarda " +
                            "puede salir del telefono.\n\n" +
                            "Tus ${almacen.terminadas().size} entrenos viven en un archivo dentro " +
                            "de la carpeta privada de la app. Si desinstalas, se van con ella.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Humo
                    )
                }
            }

            item {
                Row(
                    Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LogoF()
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Fuerza - hecha para un gimnasio sin cobertura",
                        style = MaterialTheme.typography.bodySmall,
                        color = HumoTenue
                    )
                }
            }
        }
    }
}

@Composable
private fun Interruptor(
    titulo: String,
    detalle: String,
    activo: Boolean,
    onCambio: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.Top) {
        Column(Modifier.weight(1f).padding(end = 14.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall, color = Tinta)
            Spacer(Modifier.height(4.dp))
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = HumoTenue)
        }
        Switch(
            checked = activo,
            onCheckedChange = onCambio,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SobreAcento,
                checkedTrackColor = Rojo,
                uncheckedThumbColor = Humo,
                uncheckedTrackColor = CarbonAlto,
                uncheckedBorderColor = Linea
            )
        )
    }
}
