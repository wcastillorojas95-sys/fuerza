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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

/**
 * Montarte una rutina tuya.
 *
 * Le pones nombre, eliges ejercicios del catalogo con los mismos filtros de
 * siempre y la guardas. A partir de ahi sale en el carrusel del Plan junto a las
 * tres de serie, y se entrena igual que ellas.
 *
 * No se preguntan series ni repeticiones. Se ponen las de [bloquePorDefecto] --
 * cuatro por seis-ocho lo pesado, tres por diez-doce lo de aislar -- porque
 * decidir eso antes de tocar la barra es adivinar: durante el entreno cambias
 * los numeros en dos toques y esos si valen.
 */
@Composable
fun PantallaCrearRutina(
    biblioteca: Biblioteca,
    onGuardada: () -> Unit,
    onCerrar: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var elegidos by remember { mutableStateOf(emptyList<String>()) }
    var anadiendo by remember { mutableStateOf(false) }

    if (anadiendo) {
        SelectorEjercicio(
            yaPuestos = elegidos.toSet(),
            onElegir = { ids ->
                elegidos = elegidos + ids.filterNot { elegidos.contains(it) }
                anadiendo = false
            },
            onCerrar = { anadiendo = false }
        )
        return
    }

    Box(Modifier.fillMaxSize().background(Negro)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(
                start = 18.dp, end = 18.dp, top = 12.dp,
                bottom = if (elegidos.isEmpty()) 40.dp else 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Titular(
                        "Nueva rutina",
                        estilo = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(CarbonAlto)
                            .clickable { onCerrar() }
                            .padding(11.dp)
                    ) {
                        IconoSvg(
                            recurso = R.drawable.ic_close,
                            descripcion = "Cerrar",
                            color = Humo,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            item {
                Tarjeta(borde = true, relleno = 18.dp) {
                    Etiqueta("Como se llama", color = Rojo)
                    Spacer(Modifier.height(10.dp))
                    Box {
                        if (nombre.isEmpty()) {
                            Text(
                                "Brazos",
                                style = MaterialTheme.typography.headlineSmall,
                                color = HumoTenue
                            )
                        }
                        BasicTextField(
                            value = nombre,
                            onValueChange = { nombre = it.take(28) },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.headlineSmall.copy(color = Tinta),
                            cursorBrush = SolidColor(Rojo),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Titular(
                        if (elegidos.isEmpty()) "Ejercicios"
                        else "${elegidos.size} ejercicios",
                        estilo = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(VioletaSuave)
                            .clickable { anadiendo = true }
                            .padding(horizontal = 16.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconoSvg(
                            recurso = R.drawable.ic_plus,
                            descripcion = null,
                            color = Rojo,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "ANADIR",
                            style = MaterialTheme.typography.labelLarge,
                            color = Rojo
                        )
                    }
                }
            }

            if (elegidos.isEmpty()) {
                item {
                    Tarjeta(borde = true, relleno = 20.dp) {
                        Text(
                            "Todavia no has elegido ninguno. Dale a anadir y usa los filtros: " +
                                "marcas todos los de biceps de una vez y vuelves.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Humo
                        )
                    }
                }
            }

            items(elegidos.size) { i ->
                val id = elegidos[i]
                val bloque = remember(id) { bloquePorDefecto(id) }
                FilaElegido(
                    ejercicioId = id,
                    bloque = bloque,
                    onQuitar = { elegidos = elegidos.filterNot { it == id } }
                )
            }
        }

        if (elegidos.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Negro)
                    .navigationBarsPadding()
                    .padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 14.dp)
            ) {
                BotonRojo(
                    "Guardar en tu biblioteca",
                    onClick = {
                        biblioteca.guardar(nombre, elegidos)
                        onGuardada()
                    }
                )
            }
        }
    }
}

/** Un ejercicio ya metido en la rutina que estas montando. */
@Composable
private fun FilaElegido(ejercicioId: String, bloque: Bloque, onQuitar: () -> Unit) {
    val e = ejercicioDe(ejercicioId)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Carbon)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DemoEjercicio(
            ejercicioId = ejercicioId,
            animar = false,
            radio = 24.dp,
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                e?.nombre ?: ejercicioId,
                style = MaterialTheme.typography.titleSmall,
                color = Tinta
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "${bloque.series} series x ${bloque.rango} - ${e?.musculo?.etiqueta ?: ""}",
                style = MaterialTheme.typography.bodySmall,
                color = HumoTenue
            )
        }
        IconoSvg(
            recurso = R.drawable.ic_close,
            descripcion = "Quitar ${e?.nombre ?: ejercicioId}",
            color = HumoTenue,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable { onQuitar() }
                .padding(8.dp)
                .size(15.dp)
        )
    }
}
