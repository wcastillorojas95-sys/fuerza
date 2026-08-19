package com.lucas.fuerza

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Un titular de los grandes.
 *
 * Va en caja alta y con una compresion horizontal del 90%. La referencia usa una
 * tipografia condensada de verdad; aqui se aprovecha la Outfit que ya viene con
 * la familia de apps y se le estrecha un pelin con graphicsLayer. A este tamano
 * y en mayusculas el resultado es practicamente el mismo, y son cero kilobytes
 * mas de fuentes en el APK.
 */
@Composable
fun Titular(
    texto: String,
    modifier: Modifier = Modifier,
    estilo: TextStyle = MaterialTheme.typography.displayMedium,
    color: Color = Color.White,
    compresion: Float = 0.9f,
    alineacion: TextAlign? = null
) {
    Text(
        text = texto.uppercase(),
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

/** La tarjeta oscura de siempre. */
@Composable
fun Tarjeta(
    modifier: Modifier = Modifier,
    color: Color = Carbon,
    radio: Dp = 22.dp,
    relleno: Dp = 18.dp,
    borde: Boolean = false,
    contenido: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(radio))
            .background(color)
            .then(
                if (borde) Modifier.border(1.dp, Color(0xFF2A2A32), RoundedCornerShape(radio))
                else Modifier
            )
            .padding(relleno),
        content = contenido
    )
}

/**
 * El boton principal: pastilla roja con la flecha en su circulito.
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
    val tinta = if (habilitado) Color.White else HumoTenue
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(fondo)
            .clickable(enabled = habilitado, onClick = onClick)
            .padding(start = 26.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Titular(texto, estilo = MaterialTheme.typography.headlineSmall, color = tinta, compresion = 0.94f)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .background(if (habilitado) Color.White else Color(0xFF2A2A32)),
            contentAlignment = Alignment.Center
        ) {
            Text("→", style = MaterialTheme.typography.titleMedium, color = if (habilitado) fondo else HumoTenue)
        }
    }
}

/** El boton secundario: mismo tamano, sin relleno. */
@Composable
fun BotonBorde(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .border(1.5.dp, Color(0xFF2E2E38), RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(vertical = 17.dp),
        contentAlignment = Alignment.Center
    ) {
        Titular(texto, estilo = MaterialTheme.typography.headlineSmall, color = color, compresion = 0.94f)
    }
}

/**
 * La cabecera con degradado que abre cada pantalla.
 *
 * Aqui es donde la referencia pone una foto de gimnasio. Se ha resuelto con un
 * degradado granate y un resplandor rojo en diagonal en vez de con fotografia,
 * por dos razones: cualquier foto decente de banco de imagenes tiene licencia
 * detras, y un JPEG a pantalla completa pesa mas que todo el resto del APK.
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
            // El resplandor: un circulo rojo muy difuminado abajo a la derecha,
            // que es lo que da la sensacion de foco de sala de pesas.
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
                    color = Color.White.copy(alpha = 0.035f),
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
                    color = Rojo,
                    compresion = 0.92f
                )
            }
            if (i < datos.lastIndex) {
                Box(Modifier.width(1.dp).height(34.dp).background(Color(0xFF2A2A32)))
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
        // El relleno bajo la linea, que es lo que le da cuerpo sobre negro.
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
                                v <= 0.0 -> Color(0xFF23232B)
                                i == resaltado -> Rojo
                                else -> Rojo.copy(alpha = 0.45f)
                            }
                        )
                )
                Spacer(Modifier.height(7.dp))
                Text(
                    text = etiquetas.getOrElse(i) { "" },
                    style = MaterialTheme.typography.labelMedium,
                    color = if (i == resaltado) Color.White else HumoTenue
                )
            }
        }
    }
}

/**
 * La F de la marca, dibujada con el mismo trazado que el icono del lanzador.
 *
 * Se dibuja en vez de escribirse con la fuente para que sea identica al icono
 * pase lo que pase con la tipografia.
 */
@Composable
fun LogoF(modifier: Modifier = Modifier, color: Color = Rojo) {
    Canvas(modifier.size(26.dp)) {
        val e = size.minDimension / 108f
        val p = Path().apply {
            moveTo(40 * e, 26 * e); lineTo(84 * e, 26 * e); lineTo(80 * e, 40 * e)
            lineTo(51 * e, 40 * e); lineTo(48 * e, 52 * e); lineTo(73 * e, 52 * e)
            lineTo(69 * e, 66 * e); lineTo(45 * e, 66 * e); lineTo(38 * e, 86 * e)
            lineTo(23 * e, 86 * e); close()
        }
        drawPath(p, color)
    }
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
            color = Color(0xFF23232B),
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
