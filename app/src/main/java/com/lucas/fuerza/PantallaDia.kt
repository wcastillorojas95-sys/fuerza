package com.lucas.fuerza

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * Lo que te espera en un dia de la rutina, antes de entrar.
 *
 * Es la pantalla que faltaba. Hasta ahora se pasaba del plan al entreno de
 * golpe, sin poder mirar antes que toca: y mirar que toca es justo lo que uno
 * hace de camino al gimnasio. Aqui estan los ejercicios en orden, con sus
 * series, y un boton grande abajo que no se va nunca de la pantalla.
 */
@Composable
fun PantallaDia(
    rutina: Rutina,
    dia: DiaRutina,
    almacen: Almacen,
    onVolver: () -> Unit,
    onComenzar: () -> Unit
) {
    // Si quedo un entreno a medias de otro dia, empezar aqui lo retomaria a el
    // -- dos sesiones abiertas a la vez no significan nada. Mas vale decirlo en
    // el boton que dejar que se entere al entrar.
    val abierta = remember(dia.nombre) { almacen.abierta() }
    val otroAbierto = abierta != null && abierta.diaNombre != dia.nombre

    Box(Modifier.fillMaxSize().background(Negro)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 130.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { CabeceraDia(rutina, dia, onVolver) }

            item {
                Tarjeta(
                    borde = true,
                    relleno = 20.dp,
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    FilaDatos(
                        listOf(
                            "Ejercicios" to "${dia.bloques.size}",
                            "Series" to "${dia.bloques.sumOf { it.series }}",
                            "Dura unos" to "${dia.minutos} min"
                        )
                    )
                }
            }

            item {
                Titular(
                    "${dia.bloques.size} ejercicios",
                    estilo = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 2.dp)
                )
            }

            items(dia.bloques.size) { i ->
                FilaBloqueDia(
                    bloque = dia.bloques[i],
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
            }

            item {
                CreditoVideos(Modifier.padding(horizontal = 20.dp, vertical = 4.dp))
            }
        }

        // El boton no se va con el desplazamiento: en la referencia tampoco, y
        // es lo unico que de verdad vienes a tocar en esta pantalla.
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Negro)
                .navigationBarsPadding()
                .padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 14.dp)
        ) {
            BotonRojo(
                if (otroAbierto) "Retomar ${abierta?.diaNombre ?: ""}" else "Comenzar",
                onClick = onComenzar
            )
            if (otroAbierto) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Tienes un entreno a medias. Terminalo o descartalo y vuelve, " +
                        "y entonces este dia empieza de cero.",
                    style = MaterialTheme.typography.bodySmall,
                    color = HumoTenue
                )
            }
        }
    }
}

/** Un ejercicio del dia: la figura, el nombre y lo que toca hacer. */
@Composable
private fun FilaBloqueDia(bloque: Bloque, modifier: Modifier = Modifier) {
    val e = ejercicioDe(bloque.ejercicioId)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Carbon)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DemoEjercicio(
            ejercicioId = bloque.ejercicioId,
            animar = false,
            radio = 12.dp,
            modifier = Modifier.size(58.dp)
        )
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(
                e?.nombre ?: bloque.ejercicioId,
                style = MaterialTheme.typography.titleMedium,
                color = Tinta
            )
            Spacer(Modifier.height(3.dp))
            Text(
                "${bloque.series} series x ${bloque.rango} reps",
                style = MaterialTheme.typography.bodySmall,
                color = Humo
            )
        }
        e?.let {
            Text(
                it.musculo.etiqueta.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = HumoTenue
            )
        }
    }
}

/**
 * La cabecera del dia.
 *
 * Con foto va la foto y un velo encima; sin ella, el degradado de la casa. El
 * velo no es decoracion: un titular blanco sobre una foto de gimnasio se lee en
 * unas y desaparece en otras segun donde caiga el flash, y no hay forma de
 * saberlo hasta que la ves puesta.
 */
@Composable
private fun CabeceraDia(rutina: Rutina, dia: DiaRutina, onVolver: () -> Unit) {
    val foto = dia.foto
    val sobreFoto = foto != null
    val principal = if (sobreFoto) SobreAcento else Tinta

    Box(Modifier.fillMaxWidth().height(260.dp)) {
        if (foto != null) {
            Image(
                painter = painterResource(foto),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Canvas(Modifier.fillMaxSize()) {
                drawRect(Rojo.copy(alpha = 0.20f))
                drawRect(
                    Brush.verticalGradient(
                        0f to Tinta.copy(alpha = 0.50f),
                        0.42f to Tinta.copy(alpha = 0.28f),
                        1f to Tinta.copy(alpha = 0.90f)
                    )
                )
            }
        } else {
            Cabecera(alto = 260.dp) {}
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 14.dp, top = 6.dp)
                .clip(RoundedCornerShape(50))
                .background(Carbon)
                .clickable { onVolver() }
                .padding(11.dp)
        ) {
            IconoSvg(
                recurso = R.drawable.ic_close,
                descripcion = "Volver al plan",
                color = Tinta,
                modifier = Modifier.size(16.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 18.dp, end = 18.dp, bottom = 20.dp)
        ) {
            Etiqueta(rutina.nombre, color = if (sobreFoto) SobreAcento.copy(alpha = 0.85f) else Rojo)
            Spacer(Modifier.height(6.dp))
            Titular(
                dia.nombre.uppercase(),
                estilo = MaterialTheme.typography.displayLarge,
                color = principal
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Carbon)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    dia.foco,
                    style = MaterialTheme.typography.titleSmall,
                    color = Rojo
                )
            }
        }
    }
}
