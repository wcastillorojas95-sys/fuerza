package com.lucas.fuerza

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/**
 * Un titular de los grandes.
 *
 * Conserva las mayusculas y minusculas que recibe para mantener el tono amable
 * y editorial de la interfaz clara.
 */
@Composable
fun Titular(
    texto: String,
    modifier: Modifier = Modifier,
    estilo: TextStyle = MaterialTheme.typography.displayMedium,
    color: Color = Tinta,
    compresion: Float = 1f,
    alineacion: TextAlign? = null
) {
    Text(
        text = texto,
        style = estilo,
        color = color,
        textAlign = alineacion,
        modifier = modifier.graphicsLayer {
            scaleX = compresion
            transformOrigin = TransformOrigin(0f, 0.5f)
        }
    )
}

/** Las etiquetitas en caja alta que van encima de cada dato. */
@Composable
fun Etiqueta(texto: String, modifier: Modifier = Modifier, color: Color = HumoTenue) {
    Text(
        text = texto.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = color,
        modifier = modifier
    )
}

/** Iconos vectoriales procedentes de SVG Repo, tintados por la interfaz. */
@Composable
fun IconoSvg(
    @DrawableRes recurso: Int,
    descripcion: String?,
    modifier: Modifier = Modifier,
    color: Color = Tinta
) {
    Image(
        painter = painterResource(recurso),
        contentDescription = descripcion,
        colorFilter = ColorFilter.tint(color),
        modifier = modifier
    )
}

/** Tarjeta blanca con borde opcional y esquinas amplias. */
@Composable
fun Tarjeta(
    modifier: Modifier = Modifier,
    color: Color = Carbon,
    radio: Dp = 20.dp,
    relleno: Dp = 18.dp,
    borde: Boolean = false,
    contenido: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(radio))
            .background(color)
            .then(
                if (borde) Modifier.border(1.dp, Linea, RoundedCornerShape(radio))
                else Modifier
            )
            .padding(relleno),
        content = contenido
    )
}

/**
 * El boton principal: bloque violeta con texto centrado y flecha vectorial.
 *
 * Solo deberia haber uno por pantalla. En cuanto hay dos, ninguno es el
 * principal.
 */
@Composable
fun BotonRojo(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    color: Color = Rojo
) {
    val fondo = if (habilitado) color else CarbonAlto
    val tinta = if (habilitado) SobreAcento else HumoTenue
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(fondo)
            .clickable(enabled = habilitado, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, style = MaterialTheme.typography.titleSmall, color = tinta)
        IconoSvg(
            recurso = R.drawable.ic_arrow_right,
            descripcion = null,
            color = tinta,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(18.dp)
        )
    }
}

/** El boton secundario: mismo tamano, sin relleno. */
@Composable
fun BotonBorde(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Tinta
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, color.copy(alpha = 0.28f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, style = MaterialTheme.typography.titleSmall, color = color)
    }
}

/**
 * La cabecera con degradado que abre cada pantalla.
 *
 * Aqui es donde la referencia pone una foto de gimnasio. Se ha resuelto con un
 * degradado lavanda y un resplandor violeta en vez de con fotografia, para
 * mantener la interfaz ligera y el texto siempre legible.
 *
 * Si algun dia quieres tus propias fotos, este es el sitio: mete el drawable en
 * res/drawable y pinta aqui debajo una Image con ContentScale.Crop y el mismo
 * degradado encima para que el texto siga leyendose.
 */
@Composable
fun Cabecera(
    modifier: Modifier = Modifier,
    alto: Dp = 260.dp,
    contenido: @Composable androidx.compose.foundation.layout.BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxWidth().height(alto)) {
        Canvas(Modifier.fillMaxWidth().height(alto)) {
            drawRect(DegradadoCabecera)
            // Resplandor violeta muy suave para separar visualmente la cabecera.
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Rojo.copy(alpha = 0.42f), Color.Transparent),
                    center = Offset(size.width * 0.82f, size.height * 0.30f),
                    radius = size.minDimension * 0.85f
                ),
                radius = size.minDimension * 0.85f,
                center = Offset(size.width * 0.82f, size.height * 0.30f)
            )
            // Rayas diagonales tenues, como las de una placa de disco.
            val paso = size.width / 9f
            var x = -size.height
            while (x < size.width + size.height) {
                drawLine(
                    color = Rojo.copy(alpha = 0.05f),
                    start = Offset(x, size.height),
                    end = Offset(x + size.height * 0.55f, 0f),
                    strokeWidth = 2.5f
                )
                x += paso
            }
        }
        contenido()
    }
}

/**
 * La fila de datos con separadores verticales de la portada.
 *
 * Tres columnas como maximo: con cuatro, las etiquetas en caja alta ya no caben
 * en una linea en un movil estrecho.
 */
@Composable
fun FilaDatos(datos: List<Pair<String, String>>, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        datos.forEachIndexed { i, (etiqueta, valor) ->
            Column(Modifier.weight(1f).padding(end = 8.dp)) {
                Etiqueta(etiqueta)
                Spacer(Modifier.height(5.dp))
                Titular(
                    valor,
                    estilo = MaterialTheme.typography.headlineSmall,
                    color = Rojo
                )
            }
            if (i < datos.lastIndex) {
                Box(Modifier.width(1.dp).height(34.dp).background(Linea))
                Spacer(Modifier.width(14.dp))
            }
        }
    }
}

/**
 * La linea de progresion.
 *
 * Sin ejes, sin cuadricula y sin numeros: es una chispa, no una grafica. Su
 * unico trabajo es contestar de un vistazo a "esto sube o baja", y para eso los
 * ejes estorban. Los numeros exactos estan al lado, en texto.
 */
@Composable
fun Chispa(
    valores: List<Double>,
    modifier: Modifier = Modifier,
    color: Color = Rojo,
    grosor: Dp = 2.5.dp
) {
    Canvas(modifier) {
        if (valores.size < 2) return@Canvas
        val min = valores.min()
        val max = valores.max()
        val rango = (max - min).takeIf { it > 0.0 } ?: 1.0
        val pasoX = size.width / (valores.size - 1)
        // Un margen arriba y abajo para que la linea no toque el borde del lienzo.
        val margen = size.height * 0.12f
        val alto = size.height - margen * 2

        fun punto(i: Int): Offset {
            val y = margen + (1.0 - (valores[i] - min) / rango).toFloat() * alto
            return Offset(pasoX * i, y)
        }

        val linea = Path().apply {
            moveTo(punto(0).x, punto(0).y)
            for (i in 1 until valores.size) lineTo(punto(i).x, punto(i).y)
        }
        // El relleno bajo la linea le da cuerpo sobre la tarjeta blanca.
        val relleno = Path().apply {
            addPath(linea)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(
            relleno,
            Brush.verticalGradient(listOf(color.copy(alpha = 0.28f), Color.Transparent))
        )
        drawPath(linea, color, style = Stroke(width = grosor.toPx()))
        // El punto final, que es el dato de hoy.
        drawCircle(color, radius = grosor.toPx() * 1.7f, center = punto(valores.lastIndex))
    }
}

/**
 * Las barras de volumen por dia de la semana.
 *
 * [valores] y [etiquetas] tienen que venir con el mismo tamano. El dia sin
 * entreno se pinta igualmente, en gris y con altura minima: los huecos cuentan
 * tanto como los llenos.
 */
@Composable
fun Barras(
    valores: List<Double>,
    etiquetas: List<String>,
    modifier: Modifier = Modifier,
    resaltado: Int = -1
) {
    val max = (valores.maxOrNull() ?: 0.0).takeIf { it > 0.0 } ?: 1.0
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        valores.forEachIndexed { i, v ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val fraccion = (v / max).toFloat().coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((6 + 78 * fraccion).dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when {
                                v <= 0.0 -> Linea
                                i == resaltado -> Rojo
                                else -> Rojo.copy(alpha = 0.45f)
                            }
                        )
                )
                Spacer(Modifier.height(7.dp))
                Text(
                    text = etiquetas.getOrElse(i) { "" },
                    style = MaterialTheme.typography.labelMedium,
                    color = if (i == resaltado) Tinta else HumoTenue
                )
            }
        }
    }
}

/** Marca compacta: la mancuerna CC0 de SVG Repo usada también en el lanzador. */
@Composable
fun LogoF(modifier: Modifier = Modifier, color: Color = Rojo) {
    IconoSvg(
        recurso = R.drawable.ic_nav_exercises,
        descripcion = null,
        color = color,
        modifier = modifier.size(26.dp)
    )
}


/**
 * A cuantos cuadros por segundo va la demostracion.
 *
 * Doce, que son los cuadros que tiene cada ejercicio: asi el ciclo completo
 * dura un segundo justo, que es el ritmo de la repeticion que ensena.
 */
private const val CUADROS_POR_SEGUNDO = 12L

/**
 * La demostracion del ejercicio.
 *
 * Doce cuadros del movimiento que se van pasando: cuerpo gris con los musculos
 * que trabajan en rojo. Los cuadros vienen en una sola imagen, uno al lado del
 * otro, y aqui se dibuja el trozo que toca en cada momento. Eso evita meter una
 * libreria para leer GIF y, sobre todo, evita descomprimir doce bitmaps: se
 * carga uno y ya.
 *
 * Los cuadros se cambian secos, sin fundido entre uno y el siguiente. Hubo un
 * fundido y hacia parpadear la figura: al dibujar el cuadro viejo a media
 * opacidad y encima el nuevo tambien a media, lo que queda debajo -- el fondo
 * de la caja -- se cuela por el medio y el dibujo pierde un cuarto de su color
 * justo entre cuadro y cuadro. Doce veces por segundo, eso se ve como un
 * titileo. Un GIF tampoco funde: cambia de cuadro y ya.
 *
 * Con [animar] a false se queda quieto en el primer cuadro. Es lo que usan las
 * miniaturas de las listas: doce imagenes moviendose a la vez en una pantalla
 * no ayudan a nada y se comen la bateria.
 *
 * El fondo va blanco porque las imagenes ya lo incorporan y asi se integran con
 * las tarjetas del tema claro.
 */
@Composable
fun DemoEjercicio(
    ejercicioId: String,
    modifier: Modifier = Modifier,
    animar: Boolean = true,
    radio: Dp = 16.dp
) {
    val fotos = fotogramasDe(ejercicioId)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(radio))
            .background(if (fotos == null) CarbonAlto else Carbon),
        contentAlignment = Alignment.Center
    ) {
        if (fotos == null) {
            Text(
                "SIN DEMO",
                style = MaterialTheme.typography.labelMedium,
                color = HumoTenue
            )
        } else if (!animar) {
            Image(
                painter = painterResource(fotos.mini),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            val tira = ImageBitmap.imageResource(fotos.tira)

            // El cuadro que toca ahora mismo.
            //
            // Vive en un estado que solo se lee dentro del dibujo: asi cambiar
            // de cuadro repinta el lienzo y no rehace el arbol de composicion.
            // Y solo cambia doce veces por segundo, no en cada refresco.
            val cuadro = remember(ejercicioId) { mutableIntStateOf(0) }
            LaunchedEffect(ejercicioId, fotos.fotogramas) {
                val inicio = withFrameNanos { it }
                while (true) {
                    withFrameNanos { ahora ->
                        val toca = (((ahora - inicio) * CUADROS_POR_SEGUNDO /
                            1_000_000_000L) % fotos.fotogramas).toInt()
                        if (cuadro.intValue != toca) cuadro.intValue = toca
                    }
                }
            }

            Canvas(Modifier.fillMaxSize()) {
                // Los cuadros son cuadrados. Se centra el mayor cuadrado que
                // quepa para que la figura no salga estirada sea cual sea la
                // forma de la caja.
                val lado = tira.height
                val destino = minOf(size.width, size.height)
                drawImage(
                    image = tira,
                    srcOffset = IntOffset(cuadro.intValue * lado, 0),
                    srcSize = IntSize(lado, lado),
                    dstOffset = IntOffset(
                        ((size.width - destino) / 2f).toInt(),
                        ((size.height - destino) / 2f).toInt()
                    ),
                    dstSize = IntSize(destino.toInt(), destino.toInt()),
                    filterQuality = FilterQuality.High
                )
            }
        }
    }
}

/**
 * El credito de las imagenes.
 *
 * No es decoracion: la licencia con la que se pueden usar estas demostraciones
 * exige que se vea. Va debajo de cada demostracion grande.
 */
@Composable
fun CreditoImagenes(modifier: Modifier = Modifier) {
    Text(
        text = CREDITO_IMAGENES,
        style = MaterialTheme.typography.labelMedium,
        color = HumoTenue,
        modifier = modifier
    )
}

/** Un aro de progreso, para el temporizador de descanso. */
@Composable
fun Aro(
    fraccion: Float,
    modifier: Modifier = Modifier,
    color: Color = Rojo,
    grosor: Dp = 8.dp
) {
    Canvas(modifier) {
        val trazo = Stroke(width = grosor.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
        val margen = grosor.toPx() / 2
        val lado = size.minDimension - grosor.toPx()
        val esquina = Offset(margen + (size.width - size.minDimension) / 2, margen)
        drawArc(
            color = Linea,
            startAngle = -90f, sweepAngle = 360f, useCenter = false,
            topLeft = esquina, size = Size(lado, lado), style = trazo
        )
        drawArc(
            color = color,
            startAngle = -90f, sweepAngle = 360f * fraccion.coerceIn(0f, 1f), useCenter = false,
            topLeft = esquina, size = Size(lado, lado), style = trazo
        )
    }
}
