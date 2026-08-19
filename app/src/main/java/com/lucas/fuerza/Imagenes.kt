package com.lucas.fuerza

/**
 * Las imagenes de un ejercicio.
 *
 * [inicio] y [fin] son los dos fotogramas del movimiento, a 600x400. [mini] es
 * la misma foto de inicio a 240x160, y existe por una razon de memoria: cuando
 * pintas una miniatura de 52dp de alto, Android descomprime igualmente la imagen
 * entera, asi que una lista de ochenta y tres filas con la version grande se
 * come decenas de megabytes de bitmap sin ganar un pixel de calidad.
 */
data class Fotos(val inicio: Int, val fin: Int, val mini: Int)

/**
 * Los fotogramas de cada ejercicio, dentro del APK.
 *
 * Los 83 ejercicios completos ocupan unos 3,5 MB. Alternando los dos fotogramas
 * cada segundo se ve el movimiento, que es para lo que sirve una demostracion:
 * un video de doce segundos que hay que esperar a que cargue -- y que no carga
 * si el gimnasio esta en un sotano -- no ensena nada mas.
 *
 * Las fotos vienen del conjunto publico free-exercise-db. El repositorio se
 * publica bajo Unlicense, pero el origen de las imagenes no esta del todo claro
 * y hay hilos abiertos preguntandolo. Para una app que te instalas tu por tu
 * cuenta no hay problema; si algun dia la subes a Play, esto hay que resolverlo
 * antes -- y ahi si tendria sentido pagar una API de video con licencia limpia.
 */
val FOTOGRAMAS: Map<String, Fotos> = mapOf(
    "abduccion_maquina" to Fotos(R.drawable.ej_abduccion_maquina_0, R.drawable.ej_abduccion_maquina_1, R.drawable.ej_abduccion_maquina_m),
    "aperturas_mancuerna" to Fotos(R.drawable.ej_aperturas_mancuerna_0, R.drawable.ej_aperturas_mancuerna_1, R.drawable.ej_aperturas_mancuerna_m),
    "aperturas_polea" to Fotos(R.drawable.ej_aperturas_polea_0, R.drawable.ej_aperturas_polea_1, R.drawable.ej_aperturas_polea_m),
    "bicho_muerto" to Fotos(R.drawable.ej_bicho_muerto_0, R.drawable.ej_bicho_muerto_1, R.drawable.ej_bicho_muerto_m),
    "buenos_dias" to Fotos(R.drawable.ej_buenos_dias_0, R.drawable.ej_buenos_dias_1, R.drawable.ej_buenos_dias_m),
    "bulgara" to Fotos(R.drawable.ej_bulgara_0, R.drawable.ej_bulgara_1, R.drawable.ej_bulgara_m),
    "colgarse_barra" to Fotos(R.drawable.ej_colgarse_barra_0, R.drawable.ej_colgarse_barra_1, R.drawable.ej_colgarse_barra_m),
    "crunch_polea" to Fotos(R.drawable.ej_crunch_polea_0, R.drawable.ej_crunch_polea_1, R.drawable.ej_crunch_polea_m),
    "curl_barra" to Fotos(R.drawable.ej_curl_barra_0, R.drawable.ej_curl_barra_1, R.drawable.ej_curl_barra_m),
    "curl_concentrado" to Fotos(R.drawable.ej_curl_concentrado_0, R.drawable.ej_curl_concentrado_1, R.drawable.ej_curl_concentrado_m),
    "curl_femoral_sentado" to Fotos(R.drawable.ej_curl_femoral_sentado_0, R.drawable.ej_curl_femoral_sentado_1, R.drawable.ej_curl_femoral_sentado_m),
    "curl_femoral_tumbado" to Fotos(R.drawable.ej_curl_femoral_tumbado_0, R.drawable.ej_curl_femoral_tumbado_1, R.drawable.ej_curl_femoral_tumbado_m),
    "curl_inclinado" to Fotos(R.drawable.ej_curl_inclinado_0, R.drawable.ej_curl_inclinado_1, R.drawable.ej_curl_inclinado_m),
    "curl_mancuernas" to Fotos(R.drawable.ej_curl_mancuernas_0, R.drawable.ej_curl_mancuernas_1, R.drawable.ej_curl_mancuernas_m),
    "curl_martillo" to Fotos(R.drawable.ej_curl_martillo_0, R.drawable.ej_curl_martillo_1, R.drawable.ej_curl_martillo_m),
    "curl_muneca" to Fotos(R.drawable.ej_curl_muneca_0, R.drawable.ej_curl_muneca_1, R.drawable.ej_curl_muneca_m),
    "curl_polea" to Fotos(R.drawable.ej_curl_polea_0, R.drawable.ej_curl_polea_1, R.drawable.ej_curl_polea_m),
    "curl_predicador" to Fotos(R.drawable.ej_curl_predicador_0, R.drawable.ej_curl_predicador_1, R.drawable.ej_curl_predicador_m),
    "dominadas" to Fotos(R.drawable.ej_dominadas_0, R.drawable.ej_dominadas_1, R.drawable.ej_dominadas_m),
    "dominadas_supinas" to Fotos(R.drawable.ej_dominadas_supinas_0, R.drawable.ej_dominadas_supinas_1, R.drawable.ej_dominadas_supinas_m),
    "elevacion_piernas" to Fotos(R.drawable.ej_elevacion_piernas_0, R.drawable.ej_elevacion_piernas_1, R.drawable.ej_elevacion_piernas_m),
    "elevaciones_frontales" to Fotos(R.drawable.ej_elevaciones_frontales_0, R.drawable.ej_elevaciones_frontales_1, R.drawable.ej_elevaciones_frontales_m),
    "elevaciones_lateral_polea" to Fotos(R.drawable.ej_elevaciones_lateral_polea_0, R.drawable.ej_elevaciones_lateral_polea_1, R.drawable.ej_elevaciones_lateral_polea_m),
    "elevaciones_laterales" to Fotos(R.drawable.ej_elevaciones_laterales_0, R.drawable.ej_elevaciones_laterales_1, R.drawable.ej_elevaciones_laterales_m),
    "encogimientos" to Fotos(R.drawable.ej_encogimientos_0, R.drawable.ej_encogimientos_1, R.drawable.ej_encogimientos_m),
    "extension_cuadriceps" to Fotos(R.drawable.ej_extension_cuadriceps_0, R.drawable.ej_extension_cuadriceps_1, R.drawable.ej_extension_cuadriceps_m),
    "extension_cuerda" to Fotos(R.drawable.ej_extension_cuerda_0, R.drawable.ej_extension_cuerda_1, R.drawable.ej_extension_cuerda_m),
    "extension_polea" to Fotos(R.drawable.ej_extension_polea_0, R.drawable.ej_extension_polea_1, R.drawable.ej_extension_polea_m),
    "extension_sobre_cabeza" to Fotos(R.drawable.ej_extension_sobre_cabeza_0, R.drawable.ej_extension_sobre_cabeza_1, R.drawable.ej_extension_sobre_cabeza_m),
    "face_pull" to Fotos(R.drawable.ej_face_pull_0, R.drawable.ej_face_pull_1, R.drawable.ej_face_pull_m),
    "flexiones" to Fotos(R.drawable.ej_flexiones_0, R.drawable.ej_flexiones_1, R.drawable.ej_flexiones_m),
    "fondos_banco" to Fotos(R.drawable.ej_fondos_banco_0, R.drawable.ej_fondos_banco_1, R.drawable.ej_fondos_banco_m),
    "fondos_pecho" to Fotos(R.drawable.ej_fondos_pecho_0, R.drawable.ej_fondos_pecho_1, R.drawable.ej_fondos_pecho_m),
    "gemelo_de_pie" to Fotos(R.drawable.ej_gemelo_de_pie_0, R.drawable.ej_gemelo_de_pie_1, R.drawable.ej_gemelo_de_pie_m),
    "gemelo_prensa" to Fotos(R.drawable.ej_gemelo_prensa_0, R.drawable.ej_gemelo_prensa_1, R.drawable.ej_gemelo_prensa_m),
    "gemelo_sentado" to Fotos(R.drawable.ej_gemelo_sentado_0, R.drawable.ej_gemelo_sentado_1, R.drawable.ej_gemelo_sentado_m),
    "hack" to Fotos(R.drawable.ej_hack_0, R.drawable.ej_hack_1, R.drawable.ej_hack_m),
    "hip_thrust" to Fotos(R.drawable.ej_hip_thrust_0, R.drawable.ej_hip_thrust_1, R.drawable.ej_hip_thrust_m),
    "hiperextensiones" to Fotos(R.drawable.ej_hiperextensiones_0, R.drawable.ej_hiperextensiones_1, R.drawable.ej_hiperextensiones_m),
    "jalon_agarre_neutro" to Fotos(R.drawable.ej_jalon_agarre_neutro_0, R.drawable.ej_jalon_agarre_neutro_1, R.drawable.ej_jalon_agarre_neutro_m),
    "jalon_pecho" to Fotos(R.drawable.ej_jalon_pecho_0, R.drawable.ej_jalon_pecho_1, R.drawable.ej_jalon_pecho_m),
    "nordic" to Fotos(R.drawable.ej_nordic_0, R.drawable.ej_nordic_1, R.drawable.ej_nordic_m),
    "pajaros" to Fotos(R.drawable.ej_pajaros_0, R.drawable.ej_pajaros_1, R.drawable.ej_pajaros_m),
    "pallof" to Fotos(R.drawable.ej_pallof_0, R.drawable.ej_pallof_1, R.drawable.ej_pallof_m),
    "paseo_granjero" to Fotos(R.drawable.ej_paseo_granjero_0, R.drawable.ej_paseo_granjero_1, R.drawable.ej_paseo_granjero_m),
    "patada_gluteo_polea" to Fotos(R.drawable.ej_patada_gluteo_polea_0, R.drawable.ej_patada_gluteo_polea_1, R.drawable.ej_patada_gluteo_polea_m),
    "patada_triceps" to Fotos(R.drawable.ej_patada_triceps_0, R.drawable.ej_patada_triceps_1, R.drawable.ej_patada_triceps_m),
    "peck_deck" to Fotos(R.drawable.ej_peck_deck_0, R.drawable.ej_peck_deck_1, R.drawable.ej_peck_deck_m),
    "peso_muerto" to Fotos(R.drawable.ej_peso_muerto_0, R.drawable.ej_peso_muerto_1, R.drawable.ej_peso_muerto_m),
    "peso_muerto_piernas_rectas" to Fotos(R.drawable.ej_peso_muerto_piernas_rectas_0, R.drawable.ej_peso_muerto_piernas_rectas_1, R.drawable.ej_peso_muerto_piernas_rectas_m),
    "peso_muerto_rumano" to Fotos(R.drawable.ej_peso_muerto_rumano_0, R.drawable.ej_peso_muerto_rumano_1, R.drawable.ej_peso_muerto_rumano_m),
    "plancha" to Fotos(R.drawable.ej_plancha_0, R.drawable.ej_plancha_1, R.drawable.ej_plancha_m),
    "prensa" to Fotos(R.drawable.ej_prensa_0, R.drawable.ej_prensa_1, R.drawable.ej_prensa_m),
    "press_arnold" to Fotos(R.drawable.ej_press_arnold_0, R.drawable.ej_press_arnold_1, R.drawable.ej_press_arnold_m),
    "press_banca" to Fotos(R.drawable.ej_press_banca_0, R.drawable.ej_press_banca_1, R.drawable.ej_press_banca_m),
    "press_banca_declinado" to Fotos(R.drawable.ej_press_banca_declinado_0, R.drawable.ej_press_banca_declinado_1, R.drawable.ej_press_banca_declinado_m),
    "press_banca_inclinado" to Fotos(R.drawable.ej_press_banca_inclinado_0, R.drawable.ej_press_banca_inclinado_1, R.drawable.ej_press_banca_inclinado_m),
    "press_cerrado" to Fotos(R.drawable.ej_press_cerrado_0, R.drawable.ej_press_cerrado_1, R.drawable.ej_press_cerrado_m),
    "press_frances" to Fotos(R.drawable.ej_press_frances_0, R.drawable.ej_press_frances_1, R.drawable.ej_press_frances_m),
    "press_hombro_mancuernas" to Fotos(R.drawable.ej_press_hombro_mancuernas_0, R.drawable.ej_press_hombro_mancuernas_1, R.drawable.ej_press_hombro_mancuernas_m),
    "press_hombro_maquina" to Fotos(R.drawable.ej_press_hombro_maquina_0, R.drawable.ej_press_hombro_maquina_1, R.drawable.ej_press_hombro_maquina_m),
    "press_incl_mancuernas" to Fotos(R.drawable.ej_press_incl_mancuernas_0, R.drawable.ej_press_incl_mancuernas_1, R.drawable.ej_press_incl_mancuernas_m),
    "press_mancuernas" to Fotos(R.drawable.ej_press_mancuernas_0, R.drawable.ej_press_mancuernas_1, R.drawable.ej_press_mancuernas_m),
    "press_maquina_pecho" to Fotos(R.drawable.ej_press_maquina_pecho_0, R.drawable.ej_press_maquina_pecho_1, R.drawable.ej_press_maquina_pecho_m),
    "press_militar" to Fotos(R.drawable.ej_press_militar_0, R.drawable.ej_press_militar_1, R.drawable.ej_press_militar_m),
    "puente_gluteo" to Fotos(R.drawable.ej_puente_gluteo_0, R.drawable.ej_puente_gluteo_1, R.drawable.ej_puente_gluteo_m),
    "pullover" to Fotos(R.drawable.ej_pullover_0, R.drawable.ej_pullover_1, R.drawable.ej_pullover_m),
    "pullover_polea" to Fotos(R.drawable.ej_pullover_polea_0, R.drawable.ej_pullover_polea_1, R.drawable.ej_pullover_polea_m),
    "remo_barra" to Fotos(R.drawable.ej_remo_barra_0, R.drawable.ej_remo_barra_1, R.drawable.ej_remo_barra_m),
    "remo_mancuerna" to Fotos(R.drawable.ej_remo_mancuerna_0, R.drawable.ej_remo_mancuerna_1, R.drawable.ej_remo_mancuerna_m),
    "remo_maquina" to Fotos(R.drawable.ej_remo_maquina_0, R.drawable.ej_remo_maquina_1, R.drawable.ej_remo_maquina_m),
    "remo_menton" to Fotos(R.drawable.ej_remo_menton_0, R.drawable.ej_remo_menton_1, R.drawable.ej_remo_menton_m),
    "remo_pendlay" to Fotos(R.drawable.ej_remo_pendlay_0, R.drawable.ej_remo_pendlay_1, R.drawable.ej_remo_pendlay_m),
    "remo_sentado_polea" to Fotos(R.drawable.ej_remo_sentado_polea_0, R.drawable.ej_remo_sentado_polea_1, R.drawable.ej_remo_sentado_polea_m),
    "remo_t" to Fotos(R.drawable.ej_remo_t_0, R.drawable.ej_remo_t_1, R.drawable.ej_remo_t_m),
    "rueda_abdominal" to Fotos(R.drawable.ej_rueda_abdominal_0, R.drawable.ej_rueda_abdominal_1, R.drawable.ej_rueda_abdominal_m),
    "sentadilla" to Fotos(R.drawable.ej_sentadilla_0, R.drawable.ej_sentadilla_1, R.drawable.ej_sentadilla_m),
    "sentadilla_frontal" to Fotos(R.drawable.ej_sentadilla_frontal_0, R.drawable.ej_sentadilla_frontal_1, R.drawable.ej_sentadilla_frontal_m),
    "sentadilla_goblet" to Fotos(R.drawable.ej_sentadilla_goblet_0, R.drawable.ej_sentadilla_goblet_1, R.drawable.ej_sentadilla_goblet_m),
    "sissy" to Fotos(R.drawable.ej_sissy_0, R.drawable.ej_sissy_1, R.drawable.ej_sissy_m),
    "step_up" to Fotos(R.drawable.ej_step_up_0, R.drawable.ej_step_up_1, R.drawable.ej_step_up_m),
    "swing_kettlebell" to Fotos(R.drawable.ej_swing_kettlebell_0, R.drawable.ej_swing_kettlebell_1, R.drawable.ej_swing_kettlebell_m),
    "zancadas" to Fotos(R.drawable.ej_zancadas_0, R.drawable.ej_zancadas_1, R.drawable.ej_zancadas_m)
)

/** Las fotos de un ejercicio, o null si es uno que no tiene. */
fun fotogramasDe(id: String): Fotos? = FOTOGRAMAS[id]
