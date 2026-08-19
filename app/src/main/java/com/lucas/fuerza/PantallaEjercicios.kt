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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

/**
 * Normaliza para buscar: fuera acentos y todo a minusculas.
 *
 * Sin esto, escribir "biceps" no encuentra "Biceps" y escribir "gluteo" no
 * encuentra "Gluteo". Es de las cosas que nadie agradece cuando funcionan y
 * todo el mundo nota cuando no.
 */
fun sinAcentos(s: String): String {
    val de = "áàäâãéèëêíìïîóòöôõúùüûñçÁÀÄÂÃÉÈËÊÍÌÏÎÓÒÖÔÕÚÙÜÛÑÇ"
    val a = "aaaaaeeeeiiiiooooouuuuncAAAAAEEEEIIIIOOOOOUUUUNC"
    val sb = StringBuilder(s.length)
    for (c in s) {
        val i = de.indexOf(c)
        sb.append(if (i >= 0) a[i] else c)
    }
    return sb.toString().lowercase()
}

/** Filtra el catalogo por texto libre y por musculo. */
fun filtrarCatalogo(texto: String, musculo: Musculo?): List<Ejercicio> {
    val q = sinAcentos(texto.trim())
    return CATALOGO.filter { e ->
        (musculo == null || e.musculo == musculo || e.secundarios.contains(musculo)) &&
            (q.isEmpty() ||
                sinAcentos(e.nombre).contains(q) ||
                sinAcentos(e.musculo.etiqueta).contains(q) ||
                sinAcentos(e.equipo.etiqueta).contains(q))
    }
}

/**
 * El catalogo, para mirarlo con calma en el sofa.
 *
 * No lleva videos ni fotos. La app no pretende ensenarte un movimiento nuevo:
 * para eso hace falta ver a alguien hacerlo, y un GIF de tres segundos en un
 * movil tampoco lo consigue. Lo que hay son las dos frases que de verdad se
 * consultan entre serie y serie, cuando el ejercicio ya lo conoces.
 */
@Composable
fun PantallaEjercicios(almacen: Almacen) {
    var texto by remember { mutableStateOf("") }
    var musculo by remember { mutableStateOf<Musculo?>(null) }
    var abierto by remember { mutableStateOf<String?>(null) }

    val lista = remember(texto, musculo) { filtrarCatalogo(texto, musculo) }

    Box(Modifier.fillMaxSize().background(Negro)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 130.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Titular("Ejercicios", estilo = MaterialTheme.typography.displayMedium)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${CATALOGO.size} movimientos, todos dentro del telefono",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Humo
                )
                Spacer(Modifier.height(14.dp))
                Buscador(texto) { texto = it }
                Spacer(Modifier.height(12.dp))
                FiltroMusculos(musculo) { musculo = it }
                Spacer(Modifier.height(4.dp))
            }

            items(lista, key = { it.id }) { e ->
                FilaEjercicio(
                    ejercicio = e,
                    record = remember(e.id) { almacen.recordDe(e.id) },
                    desplegado = abierto == e.id,
                    onTocar = { abierto = if (abierto == e.id) null else e.id }
                )
            }

            if (lista.isEmpty()) {
                item {
                    Text(
                        "Nada con ese nombre. Prueba con el musculo o con el material.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = HumoTenue,
                        modifier = Modifier.padding(top = 30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Buscador(texto: String, onTexto: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(CarbonAlto)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("⌕", style = MaterialTheme.typography.titleMedium, color = HumoTenue)
        Spacer(Modifier.width(10.dp))
        Box(Modifier.weight(1f)) {
            if (texto.isEmpty()) {
                Text(
                    "Buscar ejercicio, musculo o material",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HumoTenue
                )
            }
            BasicTextField(
                value = texto,
                onValueChange = onTexto,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                cursorBrush = SolidColor(Rojo),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (texto.isNotEmpty()) {
            Text(
                "✕",
                style = MaterialTheme.typography.bodyMedium,
                color = Humo,
                modifier = Modifier.clickable { onTexto("") }.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun FiltroMusculos(actual: Musculo?, onElegir: (Musculo?) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            Chip("Todo", actual == null) { onElegir(null) }
        }
        items(Musculo.entries.toList()) { m ->
            Chip(m.etiqueta, actual == m) { onElegir(m) }
        }
    }
}

@Composable
fun Chip(texto: String, activo: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (activo) Rojo else CarbonAlto)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            texto.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = if (activo) Color.White else Humo
        )
    }
}

@Composable
private fun FilaEjercicio(
    ejercicio: Ejercicio,
    record: Serie?,
    desplegado: Boolean,
    onTocar: () -> Unit
) {
    Tarjeta(
        modifier = Modifier.fillMaxWidth().clickable { onTocar() },
        borde = true,
        relleno = 16.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DemoEjercicio(
                ejercicioId = ejercicio.id,
                animar = false,
                radio = 12.dp,
                modifier = Modifier.width(76.dp).height(52.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    ejercicio.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "${ejercicio.musculo.etiqueta} - ${ejercicio.equipo.etiqueta}",
                    style = MaterialTheme.typography.bodySmall,
                    color = HumoTenue
                )
            }
            if (record != null) {
                Column(horizontalAlignment = Alignment.End) {
                    Etiqueta("Tu record")
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "${limpio(record.kg)}x${record.reps}",
                        style = MaterialTheme.typography.titleSmall,
                        color = Rojo
                    )
                }
            }
        }
        if (desplegado) {
            Spacer(Modifier.height(14.dp))
            DemoEjercicio(
                ejercicioId = ejercicio.id,
                animar = true,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                ejercicio.claves,
                style = MaterialTheme.typography.bodyMedium,
                color = Humo
            )
            if (ejercicio.secundarios.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                Text(
                    "Tambien trabaja: " + ejercicio.secundarios.joinToString { it.etiqueta },
                    style = MaterialTheme.typography.bodySmall,
                    color = HumoTenue
                )
            }
        }
    }
}

/**
 * El selector que sale al anadir un ejercicio a mitad de entreno.
 *
 * Es una pantalla completa y no un dialogo: en un dialogo la lista se queda en
 * un pisito de trescientos pixeles y hay que hacer scroll dentro del scroll.
 */
@Composable
fun SelectorEjercicio(
    yaPuestos: Set<String>,
    onElegir: (String) -> Unit,
    onCerrar: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    var musculo by remember { mutableStateOf<Musculo?>(null) }
    val lista = remember(texto, musculo) { filtrarCatalogo(texto, musculo) }

    Box(Modifier.fillMaxSize().background(Negro)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Titular(
                        "Anadir",
                        estilo = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "Cerrar",
                        style = MaterialTheme.typography.titleSmall,
                        color = Humo,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(CarbonAlto)
                            .clickable { onCerrar() }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
                Spacer(Modifier.height(14.dp))
                Buscador(texto) { texto = it }
                Spacer(Modifier.height(12.dp))
                FiltroMusculos(musculo) { musculo = it }
                Spacer(Modifier.height(4.dp))
            }

            items(lista, key = { it.id }) { e ->
                val puesto = yaPuestos.contains(e.id)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Carbon)
                        .clickable(enabled = !puesto) { onElegir(e.id) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            e.nombre,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (puesto) HumoTenue else Color.White
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "${e.musculo.etiqueta} - ${e.equipo.etiqueta}",
                            style = MaterialTheme.typography.bodySmall,
                            color = HumoTenue
                        )
                    }
                    Text(
                        if (puesto) "puesto" else "+",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (puesto) HumoTenue else Rojo
                    )
                }
            }
        }
    }
}
