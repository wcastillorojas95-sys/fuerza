package com.lucas.fuerza

/**
 * La demostracion animada de un ejercicio.
 *
 * [tira] es una tira horizontal con los [fotogramas] cuadros del movimiento uno
 * detras de otro, y [mini] es el primer cuadro suelto para las listas.
 *
 * Van asi, y no como GIF, porque un GIF en Compose obliga a meter Coil con su
 * decodificador -- una dependencia mas y un decodificador distinto segun la
 * version de Android. Con una tira se carga un unico bitmap y se dibuja el
 * trozo que toca en cada momento: cero librerias nuevas y funciona desde
 * Android 8.
 */
data class Fotos(val tira: Int, val mini: Int, val fotogramas: Int)

/**
 * Las demostraciones, dentro del APK.
 *
 * Cuerpo gris con los musculos que trabajan en rojo, doce cuadros por ejercicio.
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
    "abduccion_maquina" to Fotos(R.drawable.ej_abduccion_maquina_tira, R.drawable.ej_abduccion_maquina_m, 12),
    "aperturas_mancuerna" to Fotos(R.drawable.ej_aperturas_mancuerna_tira, R.drawable.ej_aperturas_mancuerna_m, 12),
    "aperturas_polea" to Fotos(R.drawable.ej_aperturas_polea_tira, R.drawable.ej_aperturas_polea_m, 12),
    "bicho_muerto" to Fotos(R.drawable.ej_bicho_muerto_tira, R.drawable.ej_bicho_muerto_m, 12),
    "buenos_dias" to Fotos(R.drawable.ej_buenos_dias_tira, R.drawable.ej_buenos_dias_m, 12),
    "bulgara" to Fotos(R.drawable.ej_bulgara_tira, R.drawable.ej_bulgara_m, 12),
    "crunch_polea" to Fotos(R.drawable.ej_crunch_polea_tira, R.drawable.ej_crunch_polea_m, 12),
    "curl_barra" to Fotos(R.drawable.ej_curl_barra_tira, R.drawable.ej_curl_barra_m, 12),
    "curl_concentrado" to Fotos(R.drawable.ej_curl_concentrado_tira, R.drawable.ej_curl_concentrado_m, 12),
    "curl_femoral_sentado" to Fotos(R.drawable.ej_curl_femoral_sentado_tira, R.drawable.ej_curl_femoral_sentado_m, 12),
    "curl_femoral_tumbado" to Fotos(R.drawable.ej_curl_femoral_tumbado_tira, R.drawable.ej_curl_femoral_tumbado_m, 12),
    "curl_inclinado" to Fotos(R.drawable.ej_curl_inclinado_tira, R.drawable.ej_curl_inclinado_m, 12),
    "curl_mancuernas" to Fotos(R.drawable.ej_curl_mancuernas_tira, R.drawable.ej_curl_mancuernas_m, 12),
    "curl_martillo" to Fotos(R.drawable.ej_curl_martillo_tira, R.drawable.ej_curl_martillo_m, 12),
    "curl_muneca" to Fotos(R.drawable.ej_curl_muneca_tira, R.drawable.ej_curl_muneca_m, 12),
    "curl_polea" to Fotos(R.drawable.ej_curl_polea_tira, R.drawable.ej_curl_polea_m, 12),
    "curl_predicador" to Fotos(R.drawable.ej_curl_predicador_tira, R.drawable.ej_curl_predicador_m, 12),
    "dominadas" to Fotos(R.drawable.ej_dominadas_tira, R.drawable.ej_dominadas_m, 12),
    "dominadas_supinas" to Fotos(R.drawable.ej_dominadas_supinas_tira, R.drawable.ej_dominadas_supinas_m, 12),
    "elevacion_piernas" to Fotos(R.drawable.ej_elevacion_piernas_tira, R.drawable.ej_elevacion_piernas_m, 12),
    "elevaciones_frontales" to Fotos(R.drawable.ej_elevaciones_frontales_tira, R.drawable.ej_elevaciones_frontales_m, 12),
    "elevaciones_lateral_polea" to Fotos(R.drawable.ej_elevaciones_lateral_polea_tira, R.drawable.ej_elevaciones_lateral_polea_m, 12),
    "elevaciones_laterales" to Fotos(R.drawable.ej_elevaciones_laterales_tira, R.drawable.ej_elevaciones_laterales_m, 12),
    "encogimientos" to Fotos(R.drawable.ej_encogimientos_tira, R.drawable.ej_encogimientos_m, 12),
    "extension_cuadriceps" to Fotos(R.drawable.ej_extension_cuadriceps_tira, R.drawable.ej_extension_cuadriceps_m, 12),
    "extension_cuerda" to Fotos(R.drawable.ej_extension_cuerda_tira, R.drawable.ej_extension_cuerda_m, 12),
    "extension_polea" to Fotos(R.drawable.ej_extension_polea_tira, R.drawable.ej_extension_polea_m, 12),
    "extension_sobre_cabeza" to Fotos(R.drawable.ej_extension_sobre_cabeza_tira, R.drawable.ej_extension_sobre_cabeza_m, 12),
    "face_pull" to Fotos(R.drawable.ej_face_pull_tira, R.drawable.ej_face_pull_m, 12),
    "flexiones" to Fotos(R.drawable.ej_flexiones_tira, R.drawable.ej_flexiones_m, 12),
    "fondos_banco" to Fotos(R.drawable.ej_fondos_banco_tira, R.drawable.ej_fondos_banco_m, 12),
    "fondos_pecho" to Fotos(R.drawable.ej_fondos_pecho_tira, R.drawable.ej_fondos_pecho_m, 12),
    "gemelo_de_pie" to Fotos(R.drawable.ej_gemelo_de_pie_tira, R.drawable.ej_gemelo_de_pie_m, 12),
    "gemelo_prensa" to Fotos(R.drawable.ej_gemelo_prensa_tira, R.drawable.ej_gemelo_prensa_m, 12),
    "gemelo_sentado" to Fotos(R.drawable.ej_gemelo_sentado_tira, R.drawable.ej_gemelo_sentado_m, 12),
    "hack" to Fotos(R.drawable.ej_hack_tira, R.drawable.ej_hack_m, 12),
    "hip_thrust" to Fotos(R.drawable.ej_hip_thrust_tira, R.drawable.ej_hip_thrust_m, 12),
    "hiperextensiones" to Fotos(R.drawable.ej_hiperextensiones_tira, R.drawable.ej_hiperextensiones_m, 12),
    "jalon_agarre_neutro" to Fotos(R.drawable.ej_jalon_agarre_neutro_tira, R.drawable.ej_jalon_agarre_neutro_m, 12),
    "jalon_pecho" to Fotos(R.drawable.ej_jalon_pecho_tira, R.drawable.ej_jalon_pecho_m, 12),
    "nordic" to Fotos(R.drawable.ej_nordic_tira, R.drawable.ej_nordic_m, 12),
    "pajaros" to Fotos(R.drawable.ej_pajaros_tira, R.drawable.ej_pajaros_m, 12),
    "pallof" to Fotos(R.drawable.ej_pallof_tira, R.drawable.ej_pallof_m, 12),
    "paseo_granjero" to Fotos(R.drawable.ej_paseo_granjero_tira, R.drawable.ej_paseo_granjero_m, 12),
    "patada_gluteo_polea" to Fotos(R.drawable.ej_patada_gluteo_polea_tira, R.drawable.ej_patada_gluteo_polea_m, 12),
    "patada_triceps" to Fotos(R.drawable.ej_patada_triceps_tira, R.drawable.ej_patada_triceps_m, 12),
    "peck_deck" to Fotos(R.drawable.ej_peck_deck_tira, R.drawable.ej_peck_deck_m, 12),
    "peso_muerto" to Fotos(R.drawable.ej_peso_muerto_tira, R.drawable.ej_peso_muerto_m, 12),
    "peso_muerto_piernas_rectas" to Fotos(R.drawable.ej_peso_muerto_piernas_rectas_tira, R.drawable.ej_peso_muerto_piernas_rectas_m, 12),
    "peso_muerto_rumano" to Fotos(R.drawable.ej_peso_muerto_rumano_tira, R.drawable.ej_peso_muerto_rumano_m, 12),
    "plancha" to Fotos(R.drawable.ej_plancha_tira, R.drawable.ej_plancha_m, 12),
    "prensa" to Fotos(R.drawable.ej_prensa_tira, R.drawable.ej_prensa_m, 12),
    "press_arnold" to Fotos(R.drawable.ej_press_arnold_tira, R.drawable.ej_press_arnold_m, 12),
    "press_banca" to Fotos(R.drawable.ej_press_banca_tira, R.drawable.ej_press_banca_m, 12),
    "press_banca_declinado" to Fotos(R.drawable.ej_press_banca_declinado_tira, R.drawable.ej_press_banca_declinado_m, 12),
    "press_banca_inclinado" to Fotos(R.drawable.ej_press_banca_inclinado_tira, R.drawable.ej_press_banca_inclinado_m, 12),
    "press_cerrado" to Fotos(R.drawable.ej_press_cerrado_tira, R.drawable.ej_press_cerrado_m, 12),
    "press_frances" to Fotos(R.drawable.ej_press_frances_tira, R.drawable.ej_press_frances_m, 12),
    "press_hombro_mancuernas" to Fotos(R.drawable.ej_press_hombro_mancuernas_tira, R.drawable.ej_press_hombro_mancuernas_m, 12),
    "press_hombro_maquina" to Fotos(R.drawable.ej_press_hombro_maquina_tira, R.drawable.ej_press_hombro_maquina_m, 12),
    "press_incl_mancuernas" to Fotos(R.drawable.ej_press_incl_mancuernas_tira, R.drawable.ej_press_incl_mancuernas_m, 12),
    "press_mancuernas" to Fotos(R.drawable.ej_press_mancuernas_tira, R.drawable.ej_press_mancuernas_m, 12),
    "press_maquina_pecho" to Fotos(R.drawable.ej_press_maquina_pecho_tira, R.drawable.ej_press_maquina_pecho_m, 12),
    "press_militar" to Fotos(R.drawable.ej_press_militar_tira, R.drawable.ej_press_militar_m, 12),
    "puente_gluteo" to Fotos(R.drawable.ej_puente_gluteo_tira, R.drawable.ej_puente_gluteo_m, 12),
    "pullover" to Fotos(R.drawable.ej_pullover_tira, R.drawable.ej_pullover_m, 12),
    "pullover_polea" to Fotos(R.drawable.ej_pullover_polea_tira, R.drawable.ej_pullover_polea_m, 12),
    "remo_barra" to Fotos(R.drawable.ej_remo_barra_tira, R.drawable.ej_remo_barra_m, 12),
    "remo_mancuerna" to Fotos(R.drawable.ej_remo_mancuerna_tira, R.drawable.ej_remo_mancuerna_m, 12),
    "remo_maquina" to Fotos(R.drawable.ej_remo_maquina_tira, R.drawable.ej_remo_maquina_m, 12),
    "remo_menton" to Fotos(R.drawable.ej_remo_menton_tira, R.drawable.ej_remo_menton_m, 12),
    "remo_pendlay" to Fotos(R.drawable.ej_remo_pendlay_tira, R.drawable.ej_remo_pendlay_m, 12),
    "remo_sentado_polea" to Fotos(R.drawable.ej_remo_sentado_polea_tira, R.drawable.ej_remo_sentado_polea_m, 12),
    "remo_t" to Fotos(R.drawable.ej_remo_t_tira, R.drawable.ej_remo_t_m, 12),
    "rueda_abdominal" to Fotos(R.drawable.ej_rueda_abdominal_tira, R.drawable.ej_rueda_abdominal_m, 12),
    "sentadilla" to Fotos(R.drawable.ej_sentadilla_tira, R.drawable.ej_sentadilla_m, 12),
    "sentadilla_frontal" to Fotos(R.drawable.ej_sentadilla_frontal_tira, R.drawable.ej_sentadilla_frontal_m, 12),
    "sentadilla_goblet" to Fotos(R.drawable.ej_sentadilla_goblet_tira, R.drawable.ej_sentadilla_goblet_m, 12),
    "sissy" to Fotos(R.drawable.ej_sissy_tira, R.drawable.ej_sissy_m, 12),
    "step_up" to Fotos(R.drawable.ej_step_up_tira, R.drawable.ej_step_up_m, 12),
    "swing_kettlebell" to Fotos(R.drawable.ej_swing_kettlebell_tira, R.drawable.ej_swing_kettlebell_m, 12),
    "zancadas" to Fotos(R.drawable.ej_zancadas_tira, R.drawable.ej_zancadas_m, 12)
)

/** El credito que exige la licencia de las imagenes. Va visible en pantalla. */
const val CREDITO_IMAGENES = "\u00a9 Gym visual - gymvisual.com"

/** Las fotos de un ejercicio, o null si es uno que no tiene. */
fun fotogramasDe(id: String): Fotos? = FOTOGRAMAS[id]
