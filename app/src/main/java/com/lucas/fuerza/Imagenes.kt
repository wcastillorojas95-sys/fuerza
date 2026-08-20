package com.lucas.fuerza

/**
 * Formato heredado para una demostracion por poses.
 *
 * Se conserva como respaldo del componente visual, aunque el catalogo actual
 * usa exclusivamente [DemoVideo].
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

/**
 * Compatibilidad con la antigua demostracion por poses.
 *
 * El mapa queda vacio porque todos los ejercicios del catalogo usan video local.
 */
val FOTOGRAMAS: Map<String, Fotos> = emptyMap()

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
 * Los videos de los 103 ejercicios del catalogo.
 *
 * Cada archivo muestra a una persona haciendo el movimiento completo y sustituye
 * cualquier demostracion antigua por poses.
 *
 * Van dentro del APK, sin streaming y sin depender de nadie: siguen funcionando
 * en un gimnasio de sotano, que es la razon por la que las demostraciones no se
 * bajan de internet.
 */
val VIDEOS: Map<String, DemoVideo> = mapOf(
    "press_frances" to DemoVideo(R.raw.press_frances, R.drawable.vm_press_frances),
    "extension_polea" to DemoVideo(R.raw.extension_polea, R.drawable.vm_extension_polea),
    "extension_cuerda" to DemoVideo(R.raw.extension_cuerda, R.drawable.vm_extension_cuerda),
    "fondos_banco" to DemoVideo(R.raw.fondos_banco, R.drawable.vm_fondos_banco),
    "press_cerrado" to DemoVideo(R.raw.press_cerrado, R.drawable.vm_press_cerrado),
    "patada_triceps" to DemoVideo(R.raw.patada_triceps, R.drawable.vm_patada_triceps),
    "extension_sobre_cabeza" to DemoVideo(R.raw.extension_sobre_cabeza, R.drawable.vm_extension_sobre_cabeza),
    "extension_triceps_cuerda_sobre_cabeza" to DemoVideo(R.raw.extension_triceps_cuerda_sobre_cabeza, R.drawable.vm_extension_triceps_cuerda_sobre_cabeza),
    "extension_triceps_unilateral_cuerda" to DemoVideo(R.raw.extension_triceps_unilateral_cuerda, R.drawable.vm_extension_triceps_unilateral_cuerda),
    "flexiones_diamante" to DemoVideo(R.raw.flexiones_diamante, R.drawable.vm_flexiones_diamante),
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
    "curl_martillo_polea" to DemoVideo(R.raw.curl_martillo_polea, R.drawable.vm_curl_martillo_polea),
    "press_banca_inclinado" to DemoVideo(R.raw.press_banca_inclinado, R.drawable.vm_press_banca_inclinado),
    "press_banca_declinado" to DemoVideo(R.raw.press_banca_declinado, R.drawable.vm_press_banca_declinado),
    "press_mancuernas" to DemoVideo(R.raw.press_mancuernas, R.drawable.vm_press_mancuernas),
    "aperturas_mancuerna" to DemoVideo(R.raw.aperturas_mancuerna, R.drawable.vm_aperturas_mancuerna),
    "peck_deck" to DemoVideo(R.raw.peck_deck, R.drawable.vm_peck_deck),
    "pullover" to DemoVideo(R.raw.pullover, R.drawable.vm_pullover),
    "dominadas_supinas" to DemoVideo(R.raw.dominadas_supinas, R.drawable.vm_dominadas_supinas),
    "jalon_agarre_neutro" to DemoVideo(R.raw.jalon_agarre_neutro, R.drawable.vm_jalon_agarre_neutro),
    "remo_pendlay" to DemoVideo(R.raw.remo_pendlay, R.drawable.vm_remo_pendlay),
    "remo_maquina" to DemoVideo(R.raw.remo_maquina, R.drawable.vm_remo_maquina),
    "peso_muerto" to DemoVideo(R.raw.peso_muerto, R.drawable.vm_peso_muerto),
    "encogimientos" to DemoVideo(R.raw.encogimientos, R.drawable.vm_encogimientos),
    "buenos_dias" to DemoVideo(R.raw.buenos_dias, R.drawable.vm_buenos_dias),
    "press_militar" to DemoVideo(R.raw.press_militar, R.drawable.vm_press_militar),
    "press_hombro_mancuernas" to DemoVideo(R.raw.press_hombro_mancuernas, R.drawable.vm_press_hombro_mancuernas),
    "press_arnold" to DemoVideo(R.raw.press_arnold, R.drawable.vm_press_arnold),
    "elevaciones_laterales" to DemoVideo(R.raw.elevaciones_laterales, R.drawable.vm_elevaciones_laterales),
    "elevaciones_lateral_polea" to DemoVideo(R.raw.elevaciones_lateral_polea, R.drawable.vm_elevaciones_lateral_polea),
    "elevaciones_frontales" to DemoVideo(R.raw.elevaciones_frontales, R.drawable.vm_elevaciones_frontales),
    "pajaros" to DemoVideo(R.raw.pajaros, R.drawable.vm_pajaros),
    "press_hombro_maquina" to DemoVideo(R.raw.press_hombro_maquina, R.drawable.vm_press_hombro_maquina),
    "remo_menton" to DemoVideo(R.raw.remo_menton, R.drawable.vm_remo_menton),
    "curl_barra" to DemoVideo(R.raw.curl_barra, R.drawable.vm_curl_barra),
    "curl_predicador" to DemoVideo(R.raw.curl_predicador, R.drawable.vm_curl_predicador),
    "curl_inclinado" to DemoVideo(R.raw.curl_inclinado, R.drawable.vm_curl_inclinado),
    "curl_polea" to DemoVideo(R.raw.curl_polea, R.drawable.vm_curl_polea),
    "curl_concentrado" to DemoVideo(R.raw.curl_concentrado, R.drawable.vm_curl_concentrado),
    "sentadilla_frontal" to DemoVideo(R.raw.sentadilla_frontal, R.drawable.vm_sentadilla_frontal),
    "hack" to DemoVideo(R.raw.hack, R.drawable.vm_hack),
    "sentadilla_goblet" to DemoVideo(R.raw.sentadilla_goblet, R.drawable.vm_sentadilla_goblet),
    "step_up" to DemoVideo(R.raw.step_up, R.drawable.vm_step_up),
    "sissy" to DemoVideo(R.raw.sissy, R.drawable.vm_sissy),
    "curl_femoral_tumbado" to DemoVideo(R.raw.curl_femoral_tumbado, R.drawable.vm_curl_femoral_tumbado),
    "peso_muerto_piernas_rectas" to DemoVideo(R.raw.peso_muerto_piernas_rectas, R.drawable.vm_peso_muerto_piernas_rectas),
    "puente_gluteo" to DemoVideo(R.raw.puente_gluteo, R.drawable.vm_puente_gluteo),
    "patada_gluteo_polea" to DemoVideo(R.raw.patada_gluteo_polea, R.drawable.vm_patada_gluteo_polea),
    "abduccion_maquina" to DemoVideo(R.raw.abduccion_maquina, R.drawable.vm_abduccion_maquina),
    "nordic" to DemoVideo(R.raw.nordic, R.drawable.vm_nordic),
    "swing_kettlebell" to DemoVideo(R.raw.swing_kettlebell, R.drawable.vm_swing_kettlebell),
    "gemelo_prensa" to DemoVideo(R.raw.gemelo_prensa, R.drawable.vm_gemelo_prensa),
    "paseo_granjero" to DemoVideo(R.raw.paseo_granjero, R.drawable.vm_paseo_granjero),
    "curl_muneca" to DemoVideo(R.raw.curl_muneca, R.drawable.vm_curl_muneca),
    "colgarse_barra" to DemoVideo(R.raw.colgarse_barra, R.drawable.vm_colgarse_barra)
)

/** El video de un ejercicio, o null si el id no pertenece al catalogo. */
fun videoDe(id: String): DemoVideo? = VIDEOS[id]
