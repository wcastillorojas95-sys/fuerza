package com.lucas.fuerza

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Las rutinas.
 *
 * Se pasan de lado, una por pantalla, y debajo se ven los dias de la que estas
 * mirando. La idea es que cambiar de rutina no sea leer tres tarjetas apiladas
 * comparandolas de memoria, sino deslizar y ver que dias te tocarian.
 *
 * Elegir una hace dos cosas: fija que ejercicios te propone la portada y pone el
 * contador de dias a cero. Cambiar de rutina no borra nada de lo que ya hiciste;
 * el historico es del ejercicio, no de la rutina.
 */
@Composable
fun PantallaPlan(
    ajustes: Ajustes,
    refresco: Int,
    onCambio: () -> Unit,
    onAbrirDia: (Rutina, DiaRutina) -> Unit
) {
    var elegida by remember(refresco) { mutableStateOf(ajustes.rutinaId) }
    val diaDeHoy = ajustes.diaIndice

    // El carrusel arranca en la rutina que sigues, no en la primera de la lista.
    val arranque = RUTINAS.indexOfFirst { it.id == elegida }.coerceAtLeast(0)
    val carrusel = rememberPagerState(initialPage = arranque) { RUTINAS.size }
    val mirando = RUTINAS[carrusel.currentPage]
    val activa = mirando.id == elegida

    Box(Modifier.fillMaxSize().background(Negro)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(top = 12.dp, bottom = 130.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Titular(
                    "Plan",
                    estilo = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
            }

            item {
                HorizontalPager(
                    state = carrusel,
                    contentPadding = PaddingValues(horizontal = 18.dp),
                    pageSpacing = 12.dp
                ) { pagina ->
                    val r = RUTINAS[pagina]
                    PortadaRutina(rutina = r, activa = r.id == elegida)
                }
            }

            item {
                Puntitos(
                    total = RUTINAS.size,
                    actual = carrusel.currentPage,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    mirando.resumen,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Humo,
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
            }

            // Los dias de la rutina que estas mirando. El de hoy solo se marca
            // si esa rutina es la que sigues: en las otras no hay "hoy" que
            // valga, porque su contador ni siquiera ha empezado.
            items(mirando.dias.size) { i ->
                FilaDia(
                    dia = mirando.dias[i],
                    numero = i + 1,
                    esDeHoy = activa && i == diaDeHoy % mirando.dias.size,
                    modifier = Modifier.padding(horizontal = 18.dp),
                    onAbrir = { onAbrirDia(mirando, mirando.dias[i]) }
                )
            }

            item {
                Column(Modifier.padding(horizontal = 18.dp)) {
                    Spacer(Modifier.height(6.dp))
                    if (activa) {
                        BotonBorde("Dejar esta rutina", onClick = {
                            elegida = null
                            ajustes.rutinaId = null
                            ajustes.diaIndice = 0
                            onCambio()
                        })
                    } else {
                        BotonRojo("Seguir ${mirando.nombre}", onClick = {
                            elegida = mirando.id
                            ajustes.rutinaId = mirando.id
                            ajustes.diaIndice = 0
                            onCambio()
                        })
                    }
                }
            }

            item {
                Tarjeta(
                    borde = true,
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    Etiqueta("Sin rutina", color = Rojo)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Tambien puedes no seguir ninguna y montar cada sesion sobre la marcha " +
                            "desde el catalogo. El historico y los records funcionan igual.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Humo
                    )
                }
            }
        }
    }
}

/**
 * La portada de una rutina.
 *
 * En las apps de gimnasio de pago aqui va una foto de alguien sudando. Fuerza no
 * tiene fotos ni permiso de internet para traerlas, asi que va el degradado de
 * la casa con sus rayas de disco. Cumple lo mismo -- que la tarjeta se distinga
 * de un vistazo y el titulo entre por los ojos -- y no suma un solo byte al APK.
 */
@Composable
private fun PortadaRutina(rutina: Rutina, activa: Boolean, alto: Dp = 178.dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(alto)
            .clip(RoundedCornerShape(22.dp))
            .background(VioletaSuave)
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawRect(
                Brush.linearGradient(
                    listOf(Color(0xFFE4DBFF), Color(0xFFF3EFFF)),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, 0f)
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Rojo.copy(alpha = 0.30f), Color.Transparent),
                    center = Offset(size.width * 0.86f, size.height * 0.18f),
                    radius = size.minDimension * 1.1f
                ),
                radius = size.minDimension * 1.1f,
                center = Offset(size.width * 0.86f, size.height * 0.18f)
            )
            val paso = size.width / 8f
            var x = -size.height
            while (x < size.width + size.height) {
                drawLine(
                    color = Rojo.copy(alpha = 0.06f),
                    start = Offset(x, size.height),
                    end = Offset(x + size.height * 0.55f, 0f),
                    strokeWidth = 2.5f
                )
                x += paso
            }
        }

        if (activa) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(14.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Rojo)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("LA TUYA", style = MaterialTheme.typography.labelMedium, color = SobreAcento)
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, end = 20.dp, bottom = 18.dp)
        ) {
            Titular(
                rutina.nombre.uppercase(),
                estilo = MaterialTheme.typography.displaySmall
            )
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "${rutina.diasSemana}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Tinta
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    "dias por semana",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Humo
                )
            }
        }
    }
}

/** Los puntitos de pagina del carrusel. */
@Composable
private fun Puntitos(total: Int, actual: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { i ->
            val esta = i == actual
            Box(
                Modifier
                    .padding(horizontal = 3.dp)
                    .height(6.dp)
                    .width(if (esta) 20.dp else 6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (esta) Rojo else Linea)
            )
        }
    }
}

/**
 * Un dia de la rutina.
 *
 * El de hoy va en violeta con su boton; los demas en blanco. No hay candados:
 * la referencia los pone para que compres el plan entero, pero aqui no hay nada
 * que vender y el dia que quieras adelantar el de pierna, lo adelantas.
 */
@Composable
private fun FilaDia(
    dia: DiaRutina,
    numero: Int,
    esDeHoy: Boolean,
    onAbrir: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (esDeHoy) Rojo else Carbon)
            .clickable { onAbrir() }
            .padding(start = 20.dp, end = if (esDeHoy) 14.dp else 20.dp, top = 18.dp, bottom = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Titular(
                dia.nombre,
                estilo = MaterialTheme.typography.headlineMedium,
                color = if (esDeHoy) SobreAcento else Tinta
            )
            Spacer(Modifier.height(1.dp))
            Text(
                dia.foco,
                style = MaterialTheme.typography.bodyMedium,
                color = if (esDeHoy) SobreAcento.copy(alpha = 0.85f) else Humo
            )
        }
        if (esDeHoy) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Carbon)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text("Empezar", style = MaterialTheme.typography.titleSmall, color = Rojo)
            }
        } else {
            Text(
                "$numero",
                style = MaterialTheme.typography.headlineSmall,
                color = Linea
            )
        }
    }
}
