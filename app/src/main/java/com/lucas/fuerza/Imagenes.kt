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

/**
 * La plancha se para en la postura buena, no en la de entrar.
 *
 * Es el unico ejercicio del conjunto que invierte la espera, y tiene sentido:
 * en una plancha lo que hay que ensenar es el aguante, no como te colocas.
 */
private val PLANCHA = listOf(100, 1000)

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
    "aperturas_polea" to Fotos(R.drawable.ej_aperturas_polea_tira, R.drawable.ej_aperturas_polea_m, DOS_POSES),
    "bicho_muerto" to Fotos(R.drawable.ej_bicho_muerto_tira, R.drawable.ej_bicho_muerto_m, DOS_POSES),
    "buenos_dias" to Fotos(R.drawable.ej_buenos_dias_tira, R.drawable.ej_buenos_dias_m, DOS_POSES),
    "bulgara" to Fotos(R.drawable.ej_bulgara_tira, R.drawable.ej_bulgara_m, DOS_POSES),
    "crunch_polea" to Fotos(R.drawable.ej_crunch_polea_tira, R.drawable.ej_crunch_polea_m, DOS_POSES),
    "curl_barra" to Fotos(R.drawable.ej_curl_barra_tira, R.drawable.ej_curl_barra_m, DOS_POSES),
    "curl_concentrado" to Fotos(R.drawable.ej_curl_concentrado_tira, R.drawable.ej_curl_concentrado_m, DOS_POSES),
    "curl_femoral_sentado" to Fotos(R.drawable.ej_curl_femoral_sentado_tira, R.drawable.ej_curl_femoral_sentado_m, DOS_POSES),
    "curl_femoral_tumbado" to Fotos(R.drawable.ej_curl_femoral_tumbado_tira, R.drawable.ej_curl_femoral_tumbado_m, DOS_POSES),
    "curl_inclinado" to Fotos(R.drawable.ej_curl_inclinado_tira, R.drawable.ej_curl_inclinado_m, DOS_POSES),
    "curl_mancuernas" to Fotos(R.drawable.ej_curl_mancuernas_tira, R.drawable.ej_curl_mancuernas_m, DOS_POSES),
    "curl_martillo" to Fotos(R.drawable.ej_curl_martillo_tira, R.drawable.ej_curl_martillo_m, DOS_POSES),
    "curl_muneca" to Fotos(R.drawable.ej_curl_muneca_tira, R.drawable.ej_curl_muneca_m, DOS_POSES),
    "curl_polea" to Fotos(R.drawable.ej_curl_polea_tira, R.drawable.ej_curl_polea_m, DOS_POSES),
    "curl_predicador" to Fotos(R.drawable.ej_curl_predicador_tira, R.drawable.ej_curl_predicador_m, DOS_POSES),
    "dominadas" to Fotos(R.drawable.ej_dominadas_tira, R.drawable.ej_dominadas_m, DOS_POSES),
    "dominadas_supinas" to Fotos(R.drawable.ej_dominadas_supinas_tira, R.drawable.ej_dominadas_supinas_m, DOS_POSES),
    "elevacion_piernas" to Fotos(R.drawable.ej_elevacion_piernas_tira, R.drawable.ej_elevacion_piernas_m, DOS_POSES),
    "elevaciones_frontales" to Fotos(R.drawable.ej_elevaciones_frontales_tira, R.drawable.ej_elevaciones_frontales_m, DOS_POSES),
    "elevaciones_lateral_polea" to Fotos(R.drawable.ej_elevaciones_lateral_polea_tira, R.drawable.ej_elevaciones_lateral_polea_m, DOS_POSES),
    "elevaciones_laterales" to Fotos(R.drawable.ej_elevaciones_laterales_tira, R.drawable.ej_elevaciones_laterales_m, DOS_POSES),
    "encogimientos" to Fotos(R.drawable.ej_encogimientos_tira, R.drawable.ej_encogimientos_m, DOS_POSES),
    "extension_cuadriceps" to Fotos(R.drawable.ej_extension_cuadriceps_tira, R.drawable.ej_extension_cuadriceps_m, DOS_POSES),
    "extension_cuerda" to Fotos(R.drawable.ej_extension_cuerda_tira, R.drawable.ej_extension_cuerda_m, DOS_POSES),
    "extension_polea" to Fotos(R.drawable.ej_extension_polea_tira, R.drawable.ej_extension_polea_m, DOS_POSES),
    "extension_sobre_cabeza" to Fotos(R.drawable.ej_extension_sobre_cabeza_tira, R.drawable.ej_extension_sobre_cabeza_m, DOS_POSES),
    "face_pull" to Fotos(R.drawable.ej_face_pull_tira, R.drawable.ej_face_pull_m, DOS_POSES),
    "flexiones" to Fotos(R.drawable.ej_flexiones_tira, R.drawable.ej_flexiones_m, DOS_POSES),
    "fondos_banco" to Fotos(R.drawable.ej_fondos_banco_tira, R.drawable.ej_fondos_banco_m, DOS_POSES),
    "fondos_pecho" to Fotos(R.drawable.ej_fondos_pecho_tira, R.drawable.ej_fondos_pecho_m, DOS_POSES),
    "gemelo_de_pie" to Fotos(R.drawable.ej_gemelo_de_pie_tira, R.drawable.ej_gemelo_de_pie_m, DOS_POSES),
    "gemelo_prensa" to Fotos(R.drawable.ej_gemelo_prensa_tira, R.drawable.ej_gemelo_prensa_m, DOS_POSES),
    "gemelo_sentado" to Fotos(R.drawable.ej_gemelo_sentado_tira, R.drawable.ej_gemelo_sentado_m, DOS_POSES),
    "hack" to Fotos(R.drawable.ej_hack_tira, R.drawable.ej_hack_m, DOS_POSES),
    "hip_thrust" to Fotos(R.drawable.ej_hip_thrust_tira, R.drawable.ej_hip_thrust_m, DOS_POSES),
    "hiperextensiones" to Fotos(R.drawable.ej_hiperextensiones_tira, R.drawable.ej_hiperextensiones_m, DOS_POSES),
    "jalon_agarre_neutro" to Fotos(R.drawable.ej_jalon_agarre_neutro_tira, R.drawable.ej_jalon_agarre_neutro_m, DOS_POSES),
    "jalon_pecho" to Fotos(R.drawable.ej_jalon_pecho_tira, R.drawable.ej_jalon_pecho_m, DOS_POSES),
    "nordic" to Fotos(R.drawable.ej_nordic_tira, R.drawable.ej_nordic_m, DOS_POSES),
    "pajaros" to Fotos(R.drawable.ej_pajaros_tira, R.drawable.ej_pajaros_m, DOS_POSES),
    "pallof" to Fotos(R.drawable.ej_pallof_tira, R.drawable.ej_pallof_m, DOS_POSES),
    "paseo_granjero" to Fotos(R.drawable.ej_paseo_granjero_tira, R.drawable.ej_paseo_granjero_m, CUATRO_POSES),
    "patada_gluteo_polea" to Fotos(R.drawable.ej_patada_gluteo_polea_tira, R.drawable.ej_patada_gluteo_polea_m, DOS_POSES),
    "patada_triceps" to Fotos(R.drawable.ej_patada_triceps_tira, R.drawable.ej_patada_triceps_m, DOS_POSES),
    "peck_deck" to Fotos(R.drawable.ej_peck_deck_tira, R.drawable.ej_peck_deck_m, DOS_POSES),
    "peso_muerto" to Fotos(R.drawable.ej_peso_muerto_tira, R.drawable.ej_peso_muerto_m, DOS_POSES),
    "peso_muerto_piernas_rectas" to Fotos(R.drawable.ej_peso_muerto_piernas_rectas_tira, R.drawable.ej_peso_muerto_piernas_rectas_m, DOS_POSES),
    "peso_muerto_rumano" to Fotos(R.drawable.ej_peso_muerto_rumano_tira, R.drawable.ej_peso_muerto_rumano_m, DOS_POSES),
    "plancha" to Fotos(R.drawable.ej_plancha_tira, R.drawable.ej_plancha_m, PLANCHA),
    "prensa" to Fotos(R.drawable.ej_prensa_tira, R.drawable.ej_prensa_m, DOS_POSES),
    "press_arnold" to Fotos(R.drawable.ej_press_arnold_tira, R.drawable.ej_press_arnold_m, DOS_POSES),
    "press_banca" to Fotos(R.drawable.ej_press_banca_tira, R.drawable.ej_press_banca_m, DOS_POSES),
    "press_banca_declinado" to Fotos(R.drawable.ej_press_banca_declinado_tira, R.drawable.ej_press_banca_declinado_m, DOS_POSES),
    "press_banca_inclinado" to Fotos(R.drawable.ej_press_banca_inclinado_tira, R.drawable.ej_press_banca_inclinado_m, DOS_POSES),
    "press_cerrado" to Fotos(R.drawable.ej_press_cerrado_tira, R.drawable.ej_press_cerrado_m, DOS_POSES),
    "press_frances" to Fotos(R.drawable.ej_press_frances_tira, R.drawable.ej_press_frances_m, DOS_POSES),
    "press_hombro_mancuernas" to Fotos(R.drawable.ej_press_hombro_mancuernas_tira, R.drawable.ej_press_hombro_mancuernas_m, DOS_POSES),
    "press_hombro_maquina" to Fotos(R.drawable.ej_press_hombro_maquina_tira, R.drawable.ej_press_hombro_maquina_m, DOS_POSES),
    "press_incl_mancuernas" to Fotos(R.drawable.ej_press_incl_mancuernas_tira, R.drawable.ej_press_incl_mancuernas_m, DOS_POSES),
    "press_mancuernas" to Fotos(R.drawable.ej_press_mancuernas_tira, R.drawable.ej_press_mancuernas_m, DOS_POSES),
    "press_maquina_pecho" to Fotos(R.drawable.ej_press_maquina_pecho_tira, R.drawable.ej_press_maquina_pecho_m, DOS_POSES),
    "press_militar" to Fotos(R.drawable.ej_press_militar_tira, R.drawable.ej_press_militar_m, DOS_POSES),
    "puente_gluteo" to Fotos(R.drawable.ej_puente_gluteo_tira, R.drawable.ej_puente_gluteo_m, CUATRO_POSES),
    "pullover" to Fotos(R.drawable.ej_pullover_tira, R.drawable.ej_pullover_m, DOS_POSES),
    "pullover_polea" to Fotos(R.drawable.ej_pullover_polea_tira, R.drawable.ej_pullover_polea_m, DOS_POSES),
    "remo_barra" to Fotos(R.drawable.ej_remo_barra_tira, R.drawable.ej_remo_barra_m, DOS_POSES),
    "remo_mancuerna" to Fotos(R.drawable.ej_remo_mancuerna_tira, R.drawable.ej_remo_mancuerna_m, DOS_POSES),
    "remo_maquina" to Fotos(R.drawable.ej_remo_maquina_tira, R.drawable.ej_remo_maquina_m, DOS_POSES),
    "remo_menton" to Fotos(R.drawable.ej_remo_menton_tira, R.drawable.ej_remo_menton_m, DOS_POSES),
    "remo_pendlay" to Fotos(R.drawable.ej_remo_pendlay_tira, R.drawable.ej_remo_pendlay_m, DOS_POSES),
    "remo_sentado_polea" to Fotos(R.drawable.ej_remo_sentado_polea_tira, R.drawable.ej_remo_sentado_polea_m, DOS_POSES),
    "remo_t" to Fotos(R.drawable.ej_remo_t_tira, R.drawable.ej_remo_t_m, DOS_POSES),
    "rueda_abdominal" to Fotos(R.drawable.ej_rueda_abdominal_tira, R.drawable.ej_rueda_abdominal_m, DOS_POSES),
    "sentadilla" to Fotos(R.drawable.ej_sentadilla_tira, R.drawable.ej_sentadilla_m, DOS_POSES),
    "sentadilla_frontal" to Fotos(R.drawable.ej_sentadilla_frontal_tira, R.drawable.ej_sentadilla_frontal_m, DOS_POSES),
    "sentadilla_goblet" to Fotos(R.drawable.ej_sentadilla_goblet_tira, R.drawable.ej_sentadilla_goblet_m, DOS_POSES),
    "sissy" to Fotos(R.drawable.ej_sissy_tira, R.drawable.ej_sissy_m, DOS_POSES),
    "step_up" to Fotos(R.drawable.ej_step_up_tira, R.drawable.ej_step_up_m, DOS_POSES),
    "swing_kettlebell" to Fotos(R.drawable.ej_swing_kettlebell_tira, R.drawable.ej_swing_kettlebell_m, DOS_POSES),
    "zancadas" to Fotos(R.drawable.ej_zancadas_tira, R.drawable.ej_zancadas_m, DOS_POSES)
)

/** El credito que exige la licencia de las imagenes. Va visible en pantalla. */
const val CREDITO_IMAGENES = "\u00a9 Gym visual - gymvisual.com"

/** Las fotos de un ejercicio, o null si es uno que no tiene. */
fun fotogramasDe(id: String): Fotos? = FOTOGRAMAS[id]
