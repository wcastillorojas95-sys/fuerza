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

val Cuerpo = FontFamily(
    Font(R.font.barlow_regular, FontWeight.Normal),
    Font(R.font.barlow_medium, FontWeight.Medium),
    Font(R.font.barlow_semibold, FontWeight.SemiBold),
    Font(R.font.barlow_bold, FontWeight.Bold),
    Font(R.font.barlow_extrabold, FontWeight.ExtraBold)
)

/** Una sola familia sans, limpia y amable como la referencia visual. */
val Titulares = Cuerpo

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
    displayLarge = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp, lineHeight = 45.sp, letterSpacing = (-0.7).sp
    ),
    displayMedium = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.ExtraBold,
        fontSize = 34.sp, lineHeight = 40.sp, letterSpacing = (-0.5).sp
    ),
    displaySmall = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 33.sp, letterSpacing = (-0.35).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Bold,
        fontSize = 23.sp, lineHeight = 28.sp, letterSpacing = (-0.2).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Bold,
        fontSize = 19.sp, lineHeight = 24.sp
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
