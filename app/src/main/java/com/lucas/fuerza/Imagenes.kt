package com.lucas.fuerza

/**
 * La demostracion de un ejercicio.
 *
 * Conviene saber que hay debajo, porque no es lo que parece. Los GIF originales
 * de Gym visual no son una animacion del movimiento: son **dos poses** -- la de
 * inicio y la de final -- que se funden la una en la otra. El GIF esta un
 * segundo quieto en cada pose y dedica medio segundo a pasar de una a otra con
 * cinco cuadros de fundido. No hay ningun fotograma intermedio de verdad: el
 * cuadro de en medio son los dos cuerpos superpuestos al 50%.
 *
 * Asi que aqui se guardan solo las poses de verdad -- dos, o cuatro en los dos
 * ejercicios que las tienen -- y el fundido lo hace la app. Sale ganando por
 * todos lados: pesa cuatro veces menos que guardar los doce cuadros, el fundido
 * va a la velocidad de la pantalla en vez de a cinco pasos, y no arrastra el
 * emborronado de la paleta de 256 colores del GIF.
 *
 * [tira] lleva las poses una al lado de otra, cuadradas. [mini] es la primera
 * suelta, para las listas. [esperas] son los milisegundos que se esta quieto en
 * cada pose y [transito] los que tarda en pasar a la siguiente.
 */
class Fotos(
    val tira: Int,
    val mini: Int,
    val esperas: List<Int>,
    val transito: Int = TRANSITO
) {
    /** Cuantas poses de verdad tiene. */
    val poses: Int = esperas.size

    /** Lo que dura la vuelta entera, en milisegundos. */
    val ciclo: Int = esperas.sum() + poses * transito

    /**
     * Donde esta el movimiento a los [milis] milisegundos de haber arrancado.
     *
     * La parte entera es la pose que se ve; la decimal, lo que lleva fundido
     * hacia la siguiente. Devolver un solo numero deja el reproductor en tres
     * lineas y sin logica de tiempos.
     */
    fun posicionEn(milis: Long): Float {
        var t = (milis % ciclo).toInt()
        for (i in esperas.indices) {
            if (t < esperas[i]) return i.toFloat()
            t -= esperas[i]
            if (t < transito) return i + t / transito.toFloat()
            t -= transito
        }
        return 0f
    }
}

/** Medio segundo de fundido entre pose y pose, que es el del GIF original. */
const val TRANSITO = 500

/** Un segundo quieto en cada una de las dos poses. Es lo normal. */
private val DOS_POSES = listOf(1000, 1000)

/** Cuatro poses: los dos ejercicios que van y vuelven pasando por el medio. */
private val CUATRO_POSES = listOf(1000, 500, 500, 1000)

/**
 * Las demostraciones, dentro del APK.
 *
 * Cuerpo gris con los musculos que trabajan en rojo, sobre fondo blanco.
 *
 * Las imagenes son propiedad de Gym visual y vienen del conjunto de datos de
 * hasaneyldrm/exercises-dataset, que las redistribuye con permiso escrito del
 * autor bajo dos condiciones: 180x180 como maximo, y que se vea el credito. Por
 * eso la app lo muestra debajo de cada demostracion, y por eso las imagenes no
 * se escalan mas alla de su tamano original.
 *
 *   (c) Gym visual - https://gymvisual.com/
 *
 * Los datos del catalogo -- nombres, musculos, material -- son otra cosa y van
 * bajo licencia MIT.
 */
val FOTOGRAMAS: Map<String, Fotos> = mapOf(
    "abduccion_maquina" to Fotos(R.drawable.ej_abduccion_maquina_tira, R.drawable.ej_abduccion_maquina_m, DOS_POSES),
    "aperturas_mancuerna" to Fotos(R.drawable.ej_aperturas_mancuerna_tira, R.drawable.ej_aperturas_mancuerna_m, DOS_POSES),
    "buenos_dias" to Fotos(R.drawable.ej_buenos_dias_tira, R.drawable.ej_buenos_dias_m, DOS_POSES),
    "curl_barra" to Fotos(R.drawable.ej_curl_barra_tira, R.drawable.ej_curl_barra_m, DOS_POSES),
    "curl_concentrado" to Fotos(R.drawable.ej_curl_concentrado_tira, R.drawable.ej_curl_concentrado_m, DOS_POSES),
    "curl_femoral_tumbado" to Fotos(R.drawable.ej_curl_femoral_tumbado_tira, R.drawable.ej_curl_femoral_tumbado_m, DOS_POSES),
    "curl_inclinado" to Fotos(R.drawable.ej_curl_inclinado_tira, R.drawable.ej_curl_inclinado_m, DOS_POSES),
    "curl_muneca" to Fotos(R.drawable.ej_curl_muneca_tira, R.drawable.ej_curl_muneca_m, DOS_POSES),
    "curl_polea" to Fotos(R.drawable.ej_curl_polea_tira, R.drawable.ej_curl_polea_m, DOS_POSES),
    "curl_predicador" to Fotos(R.drawable.ej_curl_predicador_tira, R.drawable.ej_curl_predicador_m, DOS_POSES),
    "dominadas_supinas" to Fotos(R.drawable.ej_dominadas_supinas_tira, R.drawable.ej_dominadas_supinas_m, DOS_POSES),
    "elevaciones_frontales" to Fotos(R.drawable.ej_elevaciones_frontales_tira, R.drawable.ej_elevaciones_frontales_m, DOS_POSES),
    "elevaciones_lateral_polea" to Fotos(R.drawable.ej_elevaciones_lateral_polea_tira, R.drawable.ej_elevaciones_lateral_polea_m, DOS_POSES),
    "elevaciones_laterales" to Fotos(R.drawable.ej_elevaciones_laterales_tira, R.drawable.ej_elevaciones_laterales_m, DOS_POSES),
    "encogimientos" to Fotos(R.drawable.ej_encogimientos_tira, R.drawable.ej_encogimientos_m, DOS_POSES),
    "extension_cuerda" to Fotos(R.drawable.ej_extension_cuerda_tira, R.drawable.ej_extension_cuerda_m, DOS_POSES),
    "extension_polea" to Fotos(R.drawable.ej_extension_polea_tira, R.drawable.ej_extension_polea_m, DOS_POSES),
    "extension_sobre_cabeza" to Fotos(R.drawable.ej_extension_sobre_cabeza_tira, R.drawable.ej_extension_sobre_cabeza_m, DOS_POSES),
    "fondos_banco" to Fotos(R.drawable.ej_fondos_banco_tira, R.drawable.ej_fondos_banco_m, DOS_POSES),
    "gemelo_prensa" to Fotos(R.drawable.ej_gemelo_prensa_tira, R.drawable.ej_gemelo_prensa_m, DOS_POSES),
    "hack" to Fotos(R.drawable.ej_hack_tira, R.drawable.ej_hack_m, DOS_POSES),
    "jalon_agarre_neutro" to Fotos(R.drawable.ej_jalon_agarre_neutro_tira, R.drawable.ej_jalon_agarre_neutro_m, DOS_POSES),
    "nordic" to Fotos(R.drawable.ej_nordic_tira, R.drawable.ej_nordic_m, DOS_POSES),
    "pajaros" to Fotos(R.drawable.ej_pajaros_tira, R.drawable.ej_pajaros_m, DOS_POSES),
    "paseo_granjero" to Fotos(R.drawable.ej_paseo_granjero_tira, R.drawable.ej_paseo_granjero_m, CUATRO_POSES),
    "patada_gluteo_polea" to Fotos(R.drawable.ej_patada_gluteo_polea_tira, R.drawable.ej_patada_gluteo_polea_m, DOS_POSES),
    "patada_triceps" to Fotos(R.drawable.ej_patada_triceps_tira, R.drawable.ej_patada_triceps_m, DOS_POSES),
    "peck_deck" to Fotos(R.drawable.ej_peck_deck_tira, R.drawable.ej_peck_deck_m, DOS_POSES),
    "peso_muerto" to Fotos(R.drawable.ej_peso_muerto_tira, R.drawable.ej_peso_muerto_m, DOS_POSES),
    "peso_muerto_piernas_rectas" to Fotos(R.drawable.ej_peso_muerto_piernas_rectas_tira, R.drawable.ej_peso_muerto_piernas_rectas_m, DOS_POSES),
    "press_arnold" to Fotos(R.drawable.ej_press_arnold_tira, R.drawable.ej_press_arnold_m, DOS_POSES),
    "press_banca_declinado" to Fotos(R.drawable.ej_press_banca_declinado_tira, R.drawable.ej_press_banca_declinado_m, DOS_POSES),
    "press_banca_inclinado" to Fotos(R.drawable.ej_press_banca_inclinado_tira, R.drawable.ej_press_banca_inclinado_m, DOS_POSES),
    "press_cerrado" to Fotos(R.drawable.ej_press_cerrado_tira, R.drawable.ej_press_cerrado_m, DOS_POSES),
    "press_frances" to Fotos(R.drawable.ej_press_frances_tira, R.drawable.ej_press_frances_m, DOS_POSES),
    "press_hombro_mancuernas" to Fotos(R.drawable.ej_press_hombro_mancuernas_tira, R.drawable.ej_press_hombro_mancuernas_m, DOS_POSES),
    "press_hombro_maquina" to Fotos(R.drawable.ej_press_hombro_maquina_tira, R.drawable.ej_press_hombro_maquina_m, DOS_POSES),
    "press_mancuernas" to Fotos(R.drawable.ej_press_mancuernas_tira, R.drawable.ej_press_mancuernas_m, DOS_POSES),
    "press_militar" to Fotos(R.drawable.ej_press_militar_tira, R.drawable.ej_press_militar_m, DOS_POSES),
    "puente_gluteo" to Fotos(R.drawable.ej_puente_gluteo_tira, R.drawable.ej_puente_gluteo_m, CUATRO_POSES),
    "pullover" to Fotos(R.drawable.ej_pullover_tira, R.drawable.ej_pullover_m, DOS_POSES),
    "remo_maquina" to Fotos(R.drawable.ej_remo_maquina_tira, R.drawable.ej_remo_maquina_m, DOS_POSES),
    "remo_menton" to Fotos(R.drawable.ej_remo_menton_tira, R.drawable.ej_remo_menton_m, DOS_POSES),
    "remo_pendlay" to Fotos(R.drawable.ej_remo_pendlay_tira, R.drawable.ej_remo_pendlay_m, DOS_POSES),
    "sentadilla_frontal" to Fotos(R.drawable.ej_sentadilla_frontal_tira, R.drawable.ej_sentadilla_frontal_m, DOS_POSES),
    "sentadilla_goblet" to Fotos(R.drawable.ej_sentadilla_goblet_tira, R.drawable.ej_sentadilla_goblet_m, DOS_POSES),
    "sissy" to Fotos(R.drawable.ej_sissy_tira, R.drawable.ej_sissy_m, DOS_POSES),
    "step_up" to Fotos(R.drawable.ej_step_up_tira, R.drawable.ej_step_up_m, DOS_POSES),
    "swing_kettlebell" to Fotos(R.drawable.ej_swing_kettlebell_tira, R.drawable.ej_swing_kettlebell_m, DOS_POSES)
)

/** El credito que exige la licencia de las imagenes. Va visible en pantalla. */
const val CREDITO_IMAGENES = "\u00a9 Gym visual - gymvisual.com"

/** Fuente de los MP4 locales. La marca tambien permanece dentro del video. */
const val CREDITO_VIDEOS = "Video: MuscleWiki - musclewiki.com"

/** Las fotos de un ejercicio, o null si es uno que no tiene. */
fun fotogramasDe(id: String): Fotos? = FOTOGRAMAS[id]

// ------------------------------------------------------------------ video ---

/**
 * La demostracion en video de un ejercicio.
 *
 * [video] es el archivo dentro del APK y [mini] un cuadro suelto para las
 * listas, sacado del propio video.
 */
class DemoVideo(val video: Int, val mini: Int)

/**
 * Los ejercicios que tienen video de verdad.
 *
 * Un video pesa unos cientos de KB y sustituye por completo al dibujo de dos poses:
 * ahi hay una persona haciendo el movimiento entero, con su ritmo, y no dos
 * fotos fundiendose. Cuando un ejercicio esta aqui, su entrada en [FOTOGRAMAS]
 * sobra y se quita.
 *
 * Van dentro del APK, sin streaming y sin depender de nadie: siguen funcionando
 * en un gimnasio de sotano, que es la razon por la que las demostraciones no se
 * bajan de internet.
 */
val VIDEOS: Map<String, DemoVideo> = mapOf(
    "sentadilla" to DemoVideo(R.raw.sentadilla, R.drawable.vm_sentadilla),
    "peso_muerto_rumano" to DemoVideo(R.raw.peso_muerto_rumano, R.drawable.vm_peso_muerto_rumano),
    "bulgara" to DemoVideo(R.raw.bulgara, R.drawable.vm_bulgara),
    "prensa" to DemoVideo(R.raw.prensa, R.drawable.vm_prensa),
    "extension_cuadriceps" to DemoVideo(R.raw.extension_cuadriceps, R.drawable.vm_extension_cuadriceps),
    "curl_femoral_sentado" to DemoVideo(R.raw.curl_femoral_sentado, R.drawable.vm_curl_femoral_sentado),
    "hip_thrust" to DemoVideo(R.raw.hip_thrust, R.drawable.vm_hip_thrust),
    "zancadas" to DemoVideo(R.raw.zancadas, R.drawable.vm_zancadas),
    "gemelo_de_pie" to DemoVideo(R.raw.gemelo_de_pie, R.drawable.vm_gemelo_de_pie),
    "gemelo_sentado" to DemoVideo(R.raw.gemelo_sentado, R.drawable.vm_gemelo_sentado),
    "plancha" to DemoVideo(R.raw.plancha, R.drawable.vm_plancha),
    "rueda_abdominal" to DemoVideo(R.raw.rueda_abdominal, R.drawable.vm_rueda_abdominal),
    "elevacion_piernas" to DemoVideo(R.raw.elevacion_piernas, R.drawable.vm_elevacion_piernas),
    "crunch_polea" to DemoVideo(R.raw.crunch_polea, R.drawable.vm_crunch_polea),
    "pallof" to DemoVideo(R.raw.pallof, R.drawable.vm_pallof),
    "bicho_muerto" to DemoVideo(R.raw.bicho_muerto, R.drawable.vm_bicho_muerto),
    "giro_ruso_mancuerna" to DemoVideo(R.raw.giro_ruso_mancuerna, R.drawable.vm_giro_ruso_mancuerna),
    "elevaciones_piernas_tumbado" to DemoVideo(R.raw.elevaciones_piernas_tumbado, R.drawable.vm_elevaciones_piernas_tumbado),
    "crunch_abdominal" to DemoVideo(R.raw.crunch_abdominal, R.drawable.vm_crunch_abdominal),
    "plancha_lateral" to DemoVideo(R.raw.plancha_lateral, R.drawable.vm_plancha_lateral),
    "dominadas" to DemoVideo(R.raw.dominadas, R.drawable.vm_dominadas),
    "jalon_pecho" to DemoVideo(R.raw.jalon_pecho, R.drawable.vm_jalon_pecho),
    "remo_barra" to DemoVideo(R.raw.remo_barra, R.drawable.vm_remo_barra),
    "remo_mancuerna" to DemoVideo(R.raw.remo_mancuerna, R.drawable.vm_remo_mancuerna),
    "remo_sentado_polea" to DemoVideo(R.raw.remo_sentado_polea, R.drawable.vm_remo_sentado_polea),
    "remo_t" to DemoVideo(R.raw.remo_t, R.drawable.vm_remo_t),
    "remo_invertido" to DemoVideo(R.raw.remo_invertido, R.drawable.vm_remo_invertido),
    "pullover_polea" to DemoVideo(R.raw.pullover_polea, R.drawable.vm_pullover_polea),
    "face_pull" to DemoVideo(R.raw.face_pull, R.drawable.vm_face_pull),
    "hiperextensiones" to DemoVideo(R.raw.hiperextensiones, R.drawable.vm_hiperextensiones),
    "press_banca" to DemoVideo(R.raw.press_banca, R.drawable.vm_press_banca),
    "press_suelo_mancuernas" to DemoVideo(R.raw.press_suelo_mancuernas, R.drawable.vm_press_suelo_mancuernas),
    "press_incl_mancuernas" to DemoVideo(R.raw.press_incl_mancuernas, R.drawable.vm_press_incl_mancuernas),
    "flexiones_declinadas" to DemoVideo(R.raw.flexiones_declinadas, R.drawable.vm_flexiones_declinadas),
    "press_maquina_pecho" to DemoVideo(R.raw.press_maquina_pecho, R.drawable.vm_press_maquina_pecho),
    "press_pecho_banda" to DemoVideo(R.raw.press_pecho_banda, R.drawable.vm_press_pecho_banda),
    "aperturas_polea" to DemoVideo(R.raw.aperturas_polea, R.drawable.vm_aperturas_polea),
    "aperturas_banda_unilateral" to DemoVideo(R.raw.aperturas_banda_unilateral, R.drawable.vm_aperturas_banda_unilateral),
    "flexiones" to DemoVideo(R.raw.flexiones, R.drawable.vm_flexiones),
    "flexiones_banda" to DemoVideo(R.raw.flexiones_banda, R.drawable.vm_flexiones_banda),
    "fondos_pecho" to DemoVideo(R.raw.fondos_pecho, R.drawable.vm_fondos_pecho),
    "flexiones_diamante_peso" to DemoVideo(R.raw.flexiones_diamante_peso, R.drawable.vm_flexiones_diamante_peso),
    "curl_mancuernas" to DemoVideo(R.raw.curl_mancuernas, R.drawable.vm_curl_mancuernas),
    "curl_martillo" to DemoVideo(R.raw.curl_martillo, R.drawable.vm_curl_martillo),
    "curl_inverso_mancuernas" to DemoVideo(R.raw.curl_inverso_mancuernas, R.drawable.vm_curl_inverso_mancuernas),
    "curl_giratorio_polea" to DemoVideo(R.raw.curl_giratorio_polea, R.drawable.vm_curl_giratorio_polea),
    "curl_invertido_barra" to DemoVideo(R.raw.curl_invertido_barra, R.drawable.vm_curl_invertido_barra),
    "curl_polea_alta" to DemoVideo(R.raw.curl_polea_alta, R.drawable.vm_curl_polea_alta),
    "curl_bayesian" to DemoVideo(R.raw.curl_bayesian, R.drawable.vm_curl_bayesian),
    "curl_martillo_polea" to DemoVideo(R.raw.curl_martillo_polea, R.drawable.vm_curl_martillo_polea)
)

/** El video de un ejercicio, o null si todavia no tiene. */
fun videoDe(id: String): DemoVideo? = VIDEOS[id]
