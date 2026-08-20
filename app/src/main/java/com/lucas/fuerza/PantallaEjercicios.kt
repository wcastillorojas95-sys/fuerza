package com.lucas.fuerza

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.rotate
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

/**
 * Lo que hay marcado en la barra de filtros. Todo a null es "ensename todo".
 */
data class Filtros(
    val musculo: Musculo? = null,
    val equipo: Equipo? = null,
    val nivel: Dificultad? = null,
    val compuesto: Boolean? = null
) {
    val cuantos: Int
        get() = listOfNotNull(musculo, equipo, nivel, compuesto).size
}

/**
 * Filtra el catalogo.
 *
 * El musculo compara **solo con el principal**. Antes tambien miraba los
 * secundarios y por eso filtrar por biceps sacaba dominadas, jalones y los tres
 * remos: son de espalda y llevan biceps de ayudante. Quien filtra por biceps
 * quiere el dia de biceps, no la lista de todo lo que se los toca de pasada.
 * Los secundarios siguen estando en la ficha y se pueden buscar escribiendolos.
 */
fun filtrarCatalogo(texto: String, f: Filtros = Filtros()): List<Ejercicio> {
    val q = sinAcentos(texto.trim())
    return CATALOGO.filter { e ->
        (f.musculo == null || e.musculo == f.musculo) &&
            (f.equipo == null || e.equipo == f.equipo) &&
            (f.nivel == null || e.dificultad == f.nivel) &&
            (f.compuesto == null || e.compuesto == f.compuesto) &&
            (q.isEmpty() ||
                sinAcentos(e.nombre).contains(q) ||
                sinAcentos(e.musculo.etiqueta).contains(q) ||
                sinAcentos(e.equipo.etiqueta).contains(q) ||
                (e.musculo == Musculo.CORE && q in setOf("abs", "abdomen", "core")) ||
                e.secundarios.any { sinAcentos(it.etiqueta).contains(q) })
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
    var filtros by remember { mutableStateOf(Filtros()) }
    var abierto by remember { mutableStateOf<String?>(null) }

    val lista = remember(texto, filtros) { filtrarCatalogo(texto, filtros) }

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
                    if (lista.size == CATALOGO.size) "${CATALOGO.size} movimientos, todos dentro del telefono"
                    else "${lista.size} de ${CATALOGO.size} movimientos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Humo
                )
                Spacer(Modifier.height(14.dp))
                Buscador(texto) { texto = it }
                Spacer(Modifier.height(12.dp))
                BarraFiltros(filtros) { filtros = it }
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
                        "Nada con eso. Prueba a quitar algun filtro o a buscar por el material.",
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
            .clip(RoundedCornerShape(14.dp))
            .background(Carbon)
            .border(1.dp, Linea, RoundedCornerShape(14.dp))
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconoSvg(
            recurso = R.drawable.ic_search,
            descripcion = null,
            color = HumoTenue,
            modifier = Modifier.size(19.dp)
        )
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
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Tinta),
                cursorBrush = SolidColor(Rojo),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (texto.isNotEmpty()) {
            IconoSvg(
                recurso = R.drawable.ic_close,
                descripcion = "Limpiar busqueda",
                color = Humo,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(18.dp)
                    .clickable { onTexto("") }
            )
        }
    }
}

/**
 * Los cuatro filtros del catalogo.
 *
 * Van como cuatro botones que se abren, y no como cuatro tiras de chips
 * apiladas: cuarenta y tantas opciones a la vez ocupan media pantalla y la
 * lista, que es a lo que vienes, se queda sin sitio. Se abre uno cada vez.
 */
@Composable
fun BarraFiltros(filtros: Filtros, onCambio: (Filtros) -> Unit) {
    var abierto by remember { mutableStateOf<String?>(null) }

    Column {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                ChipGrupo(
                    texto = filtros.musculo?.etiqueta ?: "Musculo",
                    marcado = filtros.musculo != null,
                    desplegado = abierto == "musculo"
                ) { abierto = if (abierto == "musculo") null else "musculo" }
            }
            item {
                ChipGrupo(
                    texto = filtros.equipo?.etiqueta ?: "Material",
                    marcado = filtros.equipo != null,
                    desplegado = abierto == "material"
                ) { abierto = if (abierto == "material") null else "material" }
            }
            item {
                ChipGrupo(
                    texto = filtros.nivel?.etiqueta ?: "Nivel",
                    marcado = filtros.nivel != null,
                    desplegado = abierto == "nivel"
                ) { abierto = if (abierto == "nivel") null else "nivel" }
            }
            item {
                ChipGrupo(
                    texto = when (filtros.compuesto) {
                        true -> "Compuesto"
                        false -> "Aislamiento"
                        null -> "Tipo"
                    },
                    marcado = filtros.compuesto != null,
                    desplegado = abierto == "tipo"
                ) { abierto = if (abierto == "tipo") null else "tipo" }
            }
            if (filtros.cuantos > 0) {
                item {
                    Chip("Quitar filtros", false) { onCambio(Filtros()); abierto = null }
                }
            }
        }

        if (abierto != null) {
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                when (abierto) {
                    "musculo" -> {
                        item { Chip("Todos", filtros.musculo == null) { onCambio(filtros.copy(musculo = null)) } }
                        items(Musculo.entries.toList()) { m ->
                            Chip(m.etiqueta, filtros.musculo == m) {
                                onCambio(filtros.copy(musculo = if (filtros.musculo == m) null else m))
                            }
                        }
                    }
                    "material" -> {
                        item { Chip("Todo", filtros.equipo == null) { onCambio(filtros.copy(equipo = null)) } }
                        items(Equipo.entries.toList()) { q ->
                            Chip(q.etiqueta, filtros.equipo == q) {
                                onCambio(filtros.copy(equipo = if (filtros.equipo == q) null else q))
                            }
                        }
                    }
                    "nivel" -> {
                        item { Chip("Todos", filtros.nivel == null) { onCambio(filtros.copy(nivel = null)) } }
                        items(Dificultad.entries.toList()) { d ->
                            Chip(d.etiqueta, filtros.nivel == d) {
                                onCambio(filtros.copy(nivel = if (filtros.nivel == d) null else d))
                            }
                        }
                    }
                    else -> {
                        item { Chip("Todo", filtros.compuesto == null) { onCambio(filtros.copy(compuesto = null)) } }
                        item {
                            Chip("Compuesto", filtros.compuesto == true) {
                                onCambio(filtros.copy(compuesto = if (filtros.compuesto == true) null else true))
                            }
                        }
                        item {
                            Chip("Aislamiento", filtros.compuesto == false) {
                                onCambio(filtros.copy(compuesto = if (filtros.compuesto == false) null else false))
                            }
                        }
                    }
                }
            }
        }
    }
}

/** El boton que abre un grupo de filtros. Marcado cuando ese grupo filtra algo. */
@Composable
private fun ChipGrupo(
    texto: String,
    marcado: Boolean,
    desplegado: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (marcado) VioletaSuave else CarbonAlto)
            .clickable(onClick = onClick)
            .padding(start = 16.dp, end = 12.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            texto.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = if (marcado) Rojo else Humo
        )
        Spacer(Modifier.width(6.dp))
        IconoSvg(
            recurso = R.drawable.ic_arrow_right,
            descripcion = null,
            color = if (marcado) Rojo else HumoTenue,
            modifier = Modifier
                .size(12.dp)
                .rotate(if (desplegado) -90f else 90f)
        )
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
            color = if (activo) SobreAcento else Humo
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
            // Redonda, como en las apps que se miran de pie con una mano.
            DemoEjercicio(
                ejercicioId = ejercicio.id,
                animar = false,
                radio = 28.dp,
                modifier = Modifier.size(56.dp)
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    ejercicio.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = Tinta
                )
                Spacer(Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${ejercicio.musculo.etiqueta} - ${ejercicio.equipo.etiqueta}",
                        style = MaterialTheme.typography.bodySmall,
                        color = HumoTenue
                    )
                    if (record != null) {
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "${limpio(record.kg)}x${record.reps}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Rojo
                        )
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            IconoSvg(
                recurso = R.drawable.ic_info,
                descripcion = if (desplegado) "Ocultar la ficha" else "Ver la ficha",
                color = if (desplegado) Rojo else HumoTenue,
                modifier = Modifier.size(24.dp)
            )
        }
        if (desplegado) {
            Spacer(Modifier.height(14.dp))
            DemostracionYVideo(ejercicio) {
                if (ejercicio.pasos.isNotEmpty()) {
                    Spacer(Modifier.height(18.dp))
                    Pasos(ejercicio.pasos)
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    ejercicio.claves,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Humo
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    buildString {
                        append(if (ejercicio.compuesto) "Compuesto" else "Aislamiento")
                        append(" - nivel ")
                        append(ejercicio.dificultad.etiqueta.lowercase())
                        if (ejercicio.secundarios.isNotEmpty()) {
                            append(" - tambien trabaja ")
                            append(ejercicio.secundarios.joinToString { it.etiqueta.lowercase() })
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = HumoTenue
                )
            }
        }
    }
}

/**
 * El selector que sale al anadir ejercicios a mitad de entreno.
 *
 * Es una pantalla completa y no un dialogo: en un dialogo la lista se queda en
 * un pisito de trescientos pixeles y hay que hacer scroll dentro del scroll.
 *
 * Y se eligen **varios de una vez**. Antes cada toque anadia uno y cerraba, asi
 * que para meter tres ejercicios de biceps habia que entrar tres veces y volver
 * a poner el filtro las tres. Ahora se marcan los que quieras y abajo aparece
 * el boton con la cuenta.
 */
@Composable
fun SelectorEjercicio(
    yaPuestos: Set<String>,
    onElegir: (List<String>) -> Unit,
    onCerrar: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    var filtros by remember { mutableStateOf(Filtros()) }
    var seleccion by remember { mutableStateOf(emptySet<String>()) }
    val lista = remember(texto, filtros) { filtrarCatalogo(texto, filtros) }

    Box(Modifier.fillMaxSize().background(Negro)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(
                start = 18.dp, end = 18.dp, top = 12.dp,
                bottom = if (seleccion.isEmpty()) 40.dp else 120.dp
            ),
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
                BarraFiltros(filtros) { filtros = it }
                Spacer(Modifier.height(4.dp))
            }

            items(lista, key = { it.id }) { e ->
                val puesto = yaPuestos.contains(e.id)
                val marcado = seleccion.contains(e.id)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (marcado) VioletaSuave else Carbon)
                        .clickable(enabled = !puesto) {
                            seleccion = if (marcado) seleccion - e.id else seleccion + e.id
                        }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DemoEjercicio(
                        ejercicioId = e.id,
                        animar = false,
                        radio = 24.dp,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            e.nombre,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (puesto) HumoTenue else Tinta
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "${e.musculo.etiqueta} - ${e.equipo.etiqueta}",
                            style = MaterialTheme.typography.bodySmall,
                            color = HumoTenue
                        )
                    }
                    if (puesto) {
                        Text("puesto", style = MaterialTheme.typography.bodySmall, color = HumoTenue)
                    } else {
                        Marca(marcado)
                    }
                }
            }

            if (lista.isEmpty()) {
                item {
                    Text(
                        "Nada con eso. Prueba a quitar algun filtro.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = HumoTenue,
                        modifier = Modifier.padding(top = 30.dp)
                    )
                }
            }
        }

        // El boton solo existe cuando hay algo marcado: un boton apagado ocupando
        // sitio abajo no informa de nada que la lista no diga ya.
        if (seleccion.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Negro)
                    .navigationBarsPadding()
                    .padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 14.dp)
            ) {
                BotonRojo(
                    if (seleccion.size == 1) "Anadir 1 ejercicio"
                    else "Anadir ${seleccion.size} ejercicios",
                    onClick = { onElegir(CATALOGO.filter { it.id in seleccion }.map { it.id }) }
                )
            }
        }
    }
}

/** El redondel de marcar, a la derecha de cada ejercicio del selector. */
@Composable
private fun Marca(marcado: Boolean) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(50))
            .background(if (marcado) Rojo else CarbonAlto),
        contentAlignment = Alignment.Center
    ) {
        if (marcado) {
            IconoSvg(
                recurso = R.drawable.ic_check,
                descripcion = null,
                color = SobreAcento,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}
