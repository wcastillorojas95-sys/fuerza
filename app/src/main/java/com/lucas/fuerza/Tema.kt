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
 * La familia del texto: Google Sans.
 *
 * Es la letra de interfaz de Google, asi que el ojo ya la tiene aprendida de
 * leer notificaciones y ajustes todo el dia. Ni personalidad de mas ni de
 * menos, que es justo lo que se le pide a la letra con la que lees una tecnica
 * de ejercicio a media serie.
 *
 * Los .ttf originales pesan 2 MB cada uno porque traen medio alfabeto del
 * mundo. Los que van aqui estan recortados a lo que usa la app -- latino,
 * puntuacion, simbolos -- y bajan a 91 KB. Lo que se salga del recorte lo
 * dibuja la letra del sistema, sin romperse.
 */
val Cuerpo = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_semibold, FontWeight.SemiBold),
    Font(R.font.google_sans_bold, FontWeight.Bold)
)

/**
 * La familia de los titulares: Bricolage Grotesque.
 *
 * Una grotesca con arista, que es lo que hace que el titular no parezca la
 * misma letra que el parrafo de debajo. Solo va en lo grande y corto: nombres
 * de dia, kilos, la cuenta atras del descanso.
 *
 * Van los dos grosores que se usan y ni uno mas: cada grosor de sobra son 70 KB
 * dentro del APK que nadie llega a ver.
 */
val Titulares = FontFamily(
    Font(R.font.bricolage_bold, FontWeight.Bold),
    Font(R.font.bricolage_extrabold, FontWeight.ExtraBold)
)

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
    // Los titulares, en Bricolage. Los cuerpos son los de siempre: al contrario
    // que una condensada, esta ocupa el mismo ancho que la que habia.
    displayLarge = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp, lineHeight = 45.sp, letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.ExtraBold,
        fontSize = 34.sp, lineHeight = 40.sp, letterSpacing = (-0.4).sp
    ),
    displaySmall = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 33.sp, letterSpacing = (-0.3).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Bold,
        fontSize = 23.sp, lineHeight = 28.sp, letterSpacing = (-0.2).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Bold,
        fontSize = 19.sp, lineHeight = 24.sp
    ),
    // La cifra suelta: la cuenta atras del descanso y cualquier numero que haya
    // que leer de un vistazo desde el suelo del gimnasio.
    titleLarge = TextStyle(
        fontFamily = Titulares, fontWeight = FontWeight.Bold,
        fontSize = 20.sp, lineHeight = 22.sp
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
