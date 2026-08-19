package com.lucas.fuerza

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxSize

/**
 * El aspecto de Fuerza.
 *
 * Negro y rojo, sin modo claro. Foco es lila y de dia; Habitos es naranja y
 * crudo; esta va de sala de pesas a las seis de la manana. Las tres se
 * distinguen de un vistazo en el cajon de aplicaciones, que es justo lo que se
 * busca teniendo tres apps del mismo autor instaladas a la vez.
 *
 *  - Titulares: Outfit Black en mayusculas, apretado y con un pelin de
 *    compresion horizontal (ver [Titular] en Componentes.kt). No es una
 *    condensada de verdad, pero puesta en caja alta y con el tracking en
 *    negativo da el mismo golpe.
 *  - Cuerpo y datos: Plus Jakarta Sans, que a tamano pequeno y sobre negro se
 *    lee mucho mejor que una geometrica.
 */
val Titulares = FontFamily(
    Font(R.font.outfit_medium, FontWeight.Medium),
    Font(R.font.outfit_semibold, FontWeight.SemiBold),
    Font(R.font.outfit_bold, FontWeight.Bold),
    Font(R.font.outfit_extrabold, FontWeight.ExtraBold),
    Font(R.font.outfit_black, FontWeight.Black)
)

val Cuerpo = FontFamily(
    Font(R.font.jakarta_regular, FontWeight.Normal),
    Font(R.font.jakarta_medium, FontWeight.Medium),
    Font(R.font.jakarta_semibold, FontWeight.SemiBold),
    Font(R.font.jakarta_bold, FontWeight.Bold),
    Font(R.font.jakarta_extrabold, FontWeight.ExtraBold)
)

// ---------------------------------------------------------------- colores ---

/** Fondo de la app. No es negro puro: el negro puro hace que los bordes de las
 *  tarjetas desaparezcan y la pantalla parezca un agujero. */
val Negro = Color(0xFF08080A)

/** Tarjetas. */
val Carbon = Color(0xFF141418)

/** Tarjetas que van encima de otra tarjeta, y campos de texto. */
val CarbonAlto = Color(0xFF1E1E24)

/** El rojo de la marca. */
val Rojo = Color(0xFFE11D2A)

/** El mismo rojo un punto mas encendido, para acentos pequenos sobre negro. */
val RojoVivo = Color(0xFFFF3040)

/** Rojo muy oscuro, casi granate: el que tine los degradados de cabecera. */
val RojoHondo = Color(0xFF43070D)

val Humo = Color(0xFF8E8E99)
val HumoTenue = Color(0xFF5A5A66)

/** Verde de "hoy toca" y de record batido. Se usa con cuentagotas. */
val Verde = Color(0xFF39D98A)

/**
 * El degradado de las cabeceras: granate arriba, negro abajo.
 *
 * Se usa detras de los titulares grandes. Sustituye a la fotografia de la
 * referencia sin arrastrar el problema de la fotografia, que es de donde sale.
 * Si algun dia quieres poner tus propias fotos, el sitio es [Cabecera] en
 * Componentes.kt.
 */
val DegradadoCabecera = Brush.verticalGradient(
    0f to RojoHondo,
    0.55f to Color(0xFF1A0407),
    1f to Negro
)

private val EsquemaOscuro = darkColorScheme(
    primary = Rojo,
    onPrimary = Color.White,
    secondary = RojoVivo,
    background = Negro,
    onBackground = Color.White,
    surface = Carbon,
    onSurface = Color.White,
    surfaceVariant = CarbonAlto,
    onSurfaceVariant = Humo,
    outline = Color(0xFF2A2A32),
    error = RojoVivo
)

private val Tipografia = Typography(
    displayLarge = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Black,
        fontSize = 46.sp, lineHeight = 46.sp, letterSpacing = (-1.6).sp
    ),
    displayMedium = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Black,
        fontSize = 34.sp, lineHeight = 36.sp, letterSpacing = (-1.1).sp
    ),
    displaySmall = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Black,
        fontSize = 26.sp, lineHeight = 28.sp, letterSpacing = (-0.8).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.ExtraBold,
        fontSize = 21.sp, lineHeight = 25.sp, letterSpacing = (-0.4).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Bold,
        fontSize = 18.sp, lineHeight = 22.sp, letterSpacing = (-0.2).sp
    ),
    titleMedium = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Bold,
        fontSize = 16.sp, lineHeight = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp, lineHeight = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Normal,
        fontSize = 15.sp, lineHeight = 23.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Normal,
        fontSize = 13.5.sp, lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Normal,
        fontSize = 12.sp, lineHeight = 16.sp
    ),
    /**
     * Las etiquetitas en caja alta que van encima de cada dato
     * ("MEMBRESIA", "SIGUIENTE ENTRENO"). El tracking abierto es lo que las
     * hace legibles a ese tamano.
     */
    labelLarge = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Bold,
        fontSize = 10.5.sp, lineHeight = 13.sp, letterSpacing = 1.3.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Bold,
        fontSize = 9.5.sp, lineHeight = 12.sp, letterSpacing = 1.1.sp
    )
)

@Composable
fun FuerzaTheme(contenido: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaOscuro, typography = Tipografia) {
        Surface(Modifier.fillMaxSize(), color = Negro) { contenido() }
    }
}
