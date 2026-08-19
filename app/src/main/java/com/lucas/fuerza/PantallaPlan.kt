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
 * Las rutinas.
 *
 * Elegir aqui hace dos cosas: fija que ejercicios te propone la portada y pone
 * el contador de dias a cero. Cambiar de rutina no borra nada de lo que ya
 * hiciste; el historico es del ejercicio, no de la rutina.
 */
@Composable
fun PantallaPlan(
    ajustes: Ajustes,
    refresco: Int,
    onCambio: () -> Unit
) {
    var elegida by remember(refresco) { mutableStateOf(ajustes.rutinaId) }
    var abierta by remember { mutableStateOf<String?>(null) }

    Box(Modifier.fillMaxSize().background(Negro)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 130.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Titular("Plan", estilo = MaterialTheme.typography.displayMedium)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Lo unico que decides aqui es cuantos dias vas a aparecer de verdad. " +
                        "Tres bien hechos ganan a seis a medias.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Humo
                )
                Spacer(Modifier.height(6.dp))
            }

            items(RUTINAS.size) { i ->
                val r = RUTINAS[i]
                TarjetaRutina(
                    rutina = r,
                    activa = elegida == r.id,
                    desplegada = abierta == r.id,
                    onElegir = {
                        elegida = if (elegida == r.id) null else r.id
                        ajustes.rutinaId = elegida
                        ajustes.diaIndice = 0
                        onCambio()
                    },
                    onDesplegar = { abierta = if (abierta == r.id) null else r.id }
                )
            }

            item {
                Tarjeta(borde = true) {
                    Etiqueta("Sin rutina", color = Rojo)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Tambien puedes no seguir ninguna y montar cada sesion sobre la marcha " +
                            "desde el catalogo. El historico y los records funcionan igual.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Humo
                    )
                    if (elegida != null) {
                        Spacer(Modifier.height(14.dp))
                        BotonBorde("Ir por libre", onClick = {
                            elegida = null
                            ajustes.rutinaId = null
                            ajustes.diaIndice = 0
                            onCambio()
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaRutina(
    rutina: Rutina,
    activa: Boolean,
    desplegada: Boolean,
    onElegir: () -> Unit,
    onDesplegar: () -> Unit
) {
    Tarjeta(
        modifier = Modifier.fillMaxWidth(),
        color = Carbon,
        borde = !activa,
        relleno = 20.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Etiqueta(
                    "${rutina.diasSemana} dias por semana",
                    color = if (activa) Rojo else HumoTenue
                )
                Spacer(Modifier.height(6.dp))
                Titular(rutina.nombre, estilo = MaterialTheme.typography.headlineMedium)
            }
            if (activa) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Rojo)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("ACTIVA", style = MaterialTheme.typography.labelMedium, color = Color.White)
                }
            }
        }

        Spacer(Modifier.height(10.dp))
        Text(rutina.resumen, style = MaterialTheme.typography.bodyMedium, color = Humo)

        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.weight(1f)) {
                if (activa) {
                    BotonBorde("Quitar", onClick = onElegir)
                } else {
                    BotonRojo("Elegir", onClick = onElegir)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(
            if (desplegada) "Ocultar los dias" else "Ver los ${rutina.dias.size} dias",
            style = MaterialTheme.typography.labelLarge,
            color = Humo,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable { onDesplegar() }
                .padding(vertical = 6.dp)
        )

        if (desplegada) {
            Spacer(Modifier.height(8.dp))
            rutina.dias.forEach { d ->
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Rojo)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        d.nombre.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        d.foco,
                        style = MaterialTheme.typography.bodySmall,
                        color = HumoTenue
                    )
                }
                Spacer(Modifier.height(6.dp))
                d.bloques.forEach { b ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                    ) {
                        Text(
                            ejercicioDe(b.ejercicioId)?.nombre ?: b.ejercicioId,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Humo,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "${b.series} x ${b.rango}",
                            style = MaterialTheme.typography.bodySmall,
                            color = HumoTenue
                        )
                    }
                }
            }
        }
    }
}
