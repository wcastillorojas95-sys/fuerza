package com.lucas.fuerza

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
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
 * La familia del texto: Figtree.
 *
 * Geometrica, de caja alta y con las formas muy abiertas. Debajo de una
 * condensada tan cerrada como Anton hace falta justo eso: una letra que respire
 * y que se lea de un vistazo con el movil apoyado en el banco.
 *
 * Van los tres grosores que se usan y ni uno mas.
 */
val Cuerpo = FontFamily(
    Font(R.font.figtree_regular, FontWeight.Normal),
    Font(R.font.figtree_semibold, FontWeight.SemiBold),
    Font(R.font.figtree_bold, FontWeight.Bold)
)

/**
 * La familia de los titulares: Anton.
 *
 * Condensada y muy negra, de cartel. Aguanta bien en grande y fatal en
 * pequeno, asi que solo se usa en lo corto: nombres de dia, kilos, la cuenta
 * atras del descanso. Nunca en un parrafo.
 *
 * Viene con un unico grosor -- por eso todos los estilos piden
 * [FontWeight.Normal]. Si pidieran negrita, Android se la inventaria engordando
 * el trazo y en una letra ya de por si negra eso se convierte en una mancha.
 */
val Titulares = FontFamily(Font(R.font.anton_regular, FontWeight.Normal))

// ---------------------------------------------------------------- colores ---

/** Nombres conservados para no mezclar el rediseño con cambios de dominio. */
val Negro = Color(0xFFF5F6FA)
val Carbon = Color(0xFFFFFFFF)
val CarbonAlto = Color(0xFFF0F1F6)
val Rojo = Color(0xFF7146E8)
val RojoVivo = Color(0xFF815AF0)
val RojoHondo = Color(0xFFE9E1FF)
val Tinta = Color(0xFF151826)
val Humo = Color(0xFF686D7C)
val HumoTenue = Color(0xFF9A9EAA)
val Linea = Color(0xFFE3E5EC)
val VioletaSuave = Color(0xFFF0EBFF)
val SobreAcento = Color.White
val Verde = Color(0xFF20A66A)

/**
 * El degradado lavanda de las cabeceras del tema claro.
 *
 * Se usa detras de los titulares grandes. Sustituye a la fotografia de la
 * referencia sin arrastrar el problema de la fotografia, que es de donde sale.
 * Si algun dia quieres poner tus propias fotos, el sitio es [Cabecera] en
 * Componentes.kt.
 */
val DegradadoCabecera = Brush.verticalGradient(
    0f to Color(0xFFEAE3FF),
    0.58f to Color(0xFFF4F0FF),
    1f to Negro
)

private val EsquemaClaro = lightColorScheme(
    primary = Rojo,
    onPrimary = SobreAcento,
    secondary = RojoVivo,
    background = Negro,
    onBackground = Tinta,
    surface = Carbon,
    onSurface = Tinta,
    surfaceVariant = CarbonAlto,
    onSurfaceVariant = Humo,
    outline = Linea,
    error = RojoVivo
)

private val Tipografia = Typography(
    // Los titulares, en Anton. Suben un punto respecto a lo que habia porque
    // Anton es estrecha y al mismo cuerpo ocupa menos ancho. El espaciado va
    // ligeramente positivo: apretada de fabrica, si ademas la cierras se
    // emborrona.
    displayLarge = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Normal,
        fontSize = 42.sp, lineHeight = 46.sp, letterSpacing = 0.2.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Normal,
        fontSize = 36.sp, lineHeight = 40.sp, letterSpacing = 0.2.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Normal,
        fontSize = 30.sp, lineHeight = 34.sp, letterSpacing = 0.15.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Normal,
        fontSize = 24.sp, lineHeight = 28.sp, letterSpacing = 0.1.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Normal,
        fontSize = 20.sp, lineHeight = 25.sp
    ),
    // La cifra suelta: la cuenta atras del descanso y cualquier numero que haya
    // que leer de un vistazo desde el suelo del gimnasio.
    titleLarge = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Normal,
        fontSize = 21.sp, lineHeight = 23.sp
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
    labelLarge = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Bold,
        fontSize = 11.sp, lineHeight = 14.sp, letterSpacing = 0.7.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Cuerpo, fontWeight = FontWeight.Bold,
        fontSize = 10.sp, lineHeight = 13.sp, letterSpacing = 0.6.sp
    )
)

@Composable
fun FuerzaTheme(contenido: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaClaro, typography = Tipografia) {
        Surface(Modifier.fillMaxSize(), color = Negro) { contenido() }
    }
}
