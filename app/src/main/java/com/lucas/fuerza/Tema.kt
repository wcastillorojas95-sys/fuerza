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
 *  - Titulares: Bebas Neue. Es una condensada de caja alta -- no tiene
 *    minusculas, y no le hacen falta: los titulares de esta app van todos en
 *    mayusculas. Un solo peso, asi que la familia se declara con Normal y todos
 *    los estilos grandes lo usan; pedirle un Bold haria que Android se inventara
 *    uno engordando los trazos, que es lo que hace que una condensada se vea
 *    sucia.
 *  - Cuerpo y datos: Barlow. Es la companera natural de la Bebas -- misma
 *    familia grotesca, algo estrechada, aire deportivo -- y a diferencia de la
 *    Bebas trae seis pesos y numeros de altura uniforme, que es lo que hace
 *    falta cuando media pantalla son kilos y repeticiones.
 */
val Titulares = FontFamily(Font(R.font.bebas_regular, FontWeight.Normal))

val Cuerpo = FontFamily(
    Font(R.font.barlow_regular, FontWeight.Normal),
    Font(R.font.barlow_medium, FontWeight.Medium),
    Font(R.font.barlow_semibold, FontWeight.SemiBold),
    Font(R.font.barlow_bold, FontWeight.Bold),
    Font(R.font.barlow_extrabold, FontWeight.ExtraBold)
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
    /*
     * Los tamanos grandes suben respecto a la version anterior porque la Bebas
     * es mucho mas estrecha: al mismo cuerpo ocupa un tercio menos de ancho y se
     * queda pequena. El interlineado va casi igual al tamano porque en caja alta
     * no hay nada que baje de la linea base.
     */
    displayLarge = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Normal,
        fontSize = 54.sp, lineHeight = 52.sp, letterSpacing = 0.5.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Normal,
        fontSize = 41.sp, lineHeight = 41.sp, letterSpacing = 0.4.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Normal,
        fontSize = 31.sp, lineHeight = 32.sp, letterSpacing = 0.3.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Normal,
        fontSize = 25.sp, lineHeight = 26.sp, letterSpacing = 0.3.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Normal,
        fontSize = 20.sp, lineHeight = 22.sp, letterSpacing = 0.4.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Bold,
        fontSize = 16.sp, lineHeight = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.SemiBold,
        fontSize = 14.5.sp, lineHeight = 19.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Normal,
        fontSize = 15.sp, lineHeight = 23.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Normal,
        fontSize = 12.5.sp, lineHeight = 17.sp
    ),
    /**
     * Las etiquetitas en caja alta que van encima de cada dato
     * ("RUTINA", "SIGUIENTE ENTRENO"). Van en Barlow y no en Bebas: a diez
     * puntos, la Bebas se cierra tanto que deja de leerse.
     */
    labelLarge = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Bold,
        fontSize = 11.sp, lineHeight = 14.sp, letterSpacing = 1.4.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Bold,
        fontSize = 10.sp, lineHeight = 13.sp, letterSpacing = 1.2.sp
    )
)

@Composable
fun FuerzaTheme(contenido: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaOscuro, typography = Tipografia) {
        Surface(Modifier.fillMaxSize(), color = Negro) { contenido() }
    }
}
