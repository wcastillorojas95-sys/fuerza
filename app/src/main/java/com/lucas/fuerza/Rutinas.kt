package com.lucas.fuerza

/**
 * Un ejercicio dentro de un dia de rutina, con sus series y su descanso.
 *
 * Las repeticiones van en rango y no en numero fijo a proposito. Un 8-10
 * significa "sube el peso cuando llegues a 10 con las tres series"; un 10 a
 * secas no te dice cuando progresar, y progresar es de lo que va esto.
 */
data class Bloque(
    val ejercicioId: String,
    val series: Int,
    val repsMin: Int,
    val repsMax: Int,
    val descansoSeg: Int
) {
    val rango: String get() = if (repsMin == repsMax) "$repsMin" else "$repsMin-$repsMax"
}

/**
 * Un dia de la rutina.
 *
 * [foto] es la imagen de la cabecera cuando abres el dia. Va por musculo
 * dominante y no una por dia: seis fotos cubren las diez jornadas de las tres
 * rutinas, y repetir la de piernas en los tres dias de pierna no molesta a
 * nadie -- lo que molesta es un APK con diez fotos casi iguales.
 */
data class DiaRutina(
    val nombre: String,
    val foco: String,
    val bloques: List<Bloque>,
    @androidx.annotation.DrawableRes val foto: Int? = null,
    /** true si te la montaste tu. Las de serie no se pueden borrar. */
    val propia: Boolean = false
)

/**
 * Cuarenta segundos por serie.
 *
 * Es lo que se tarda de verdad en hacer una serie contando montar el peso,
 * colocarse y desmontar. No sale de ningun sitio mas que de cronometrarlo, y
 * por eso lo que calcula con esto va siempre con un "unos" delante.
 */
private const val SEGUNDOS_POR_SERIE = 40

/** Lo que dura el dia, aproximadamente, en minutos. */
val DiaRutina.minutos: Int
    get() = bloques.sumOf { it.series * (SEGUNDOS_POR_SERIE + it.descansoSeg) } / 60

/**
 * Una rutina completa.
 *
 * [diasSemana] es cuantos dias pide, que no siempre coincide con cuantos dias
 * distintos tiene: el empuje/tiron/pierna son tres dias que se repiten dos
 * veces.
 */
data class Rutina(
    val id: String,
    val nombre: String,
    val resumen: String,
    val diasSemana: Int,
    val dias: List<DiaRutina>,
    /**
     * La foto de la portada, si la hay.
     *
     * A null la tarjeta se dibuja con el degradado de la casa. Es lo que hay
     * hasta que entren las fotos: la app no tiene permiso de internet, asi que
     * o van dentro del APK o no van.
     */
    @androidx.annotation.DrawableRes val foto: Int? = null
)

/**
 * Las tres rutinas que vienen de serie.
 *
 * No hay veinte. Tres bien elegidas cubren de principiante a avanzado, y la
 * eleccion entre ellas es la unica decision que de verdad importa: cuantos dias
 * a la semana vas a aparecer de verdad por el gimnasio.
 */
val RUTINAS: List<Rutina> = listOf(
    Rutina(
        id = "fullbody",
        nombre = "Cuerpo completo",
        resumen = "Tres dias a la semana, todo el cuerpo cada dia. Es lo que mas rinde cuando llevas menos de un ano entrenando: cada musculo recibe tres estimulos por semana en vez de uno.",
        diasSemana = 3,
        foto = R.drawable.foto_full_body,
        dias = listOf(
            DiaRutina(
                nombre = "Dia A",
                foto = R.drawable.foto_pecho,
                foco = "Empuje dominante",
                bloques = listOf(
                    Bloque("sentadilla", 3, 5, 8, 180),
                    Bloque("press_banca", 3, 5, 8, 180),
                    Bloque("remo_barra", 3, 8, 10, 120),
                    Bloque("press_hombro_mancuernas", 3, 10, 12, 90),
                    Bloque("curl_femoral_tumbado", 3, 10, 12, 75),
                    Bloque("plancha", 3, 30, 45, 60)
                )
            ),
            DiaRutina(
                nombre = "Dia B",
                foto = R.drawable.foto_piernas,
                foco = "Bisagra de cadera",
                bloques = listOf(
                    Bloque("peso_muerto", 3, 4, 6, 210),
                    Bloque("press_incl_mancuernas", 3, 8, 10, 120),
                    Bloque("dominadas", 3, 6, 10, 120),
                    Bloque("prensa", 3, 10, 12, 120),
                    Bloque("elevaciones_laterales", 3, 12, 15, 60),
                    Bloque("rueda_abdominal", 3, 8, 12, 60)
                )
            ),
            DiaRutina(
                nombre = "Dia C",
                foto = R.drawable.foto_biceps,
                foco = "Volumen y accesorios",
                bloques = listOf(
                    Bloque("sentadilla_frontal", 3, 6, 8, 180),
                    Bloque("press_banca_inclinado", 3, 8, 10, 150),
                    Bloque("jalon_pecho", 3, 10, 12, 90),
                    Bloque("hip_thrust", 3, 8, 12, 120),
                    Bloque("curl_barra", 3, 10, 12, 60),
                    Bloque("extension_cuerda", 3, 12, 15, 60)
                )
            )
        )
    ),
    Rutina(
        id = "torso_pierna",
        nombre = "Torso / Pierna",
        resumen = "Cuatro dias: dos de torso y dos de pierna. El punto dulce entre frecuencia y volumen, y el que mejor encaja si entrenas de lunes a jueves.",
        diasSemana = 4,
        foto = R.drawable.foto_pecho,
        dias = listOf(
            DiaRutina(
                nombre = "Torso 1",
                foto = R.drawable.foto_pecho,
                foco = "Pecho y hombro",
                bloques = listOf(
                    Bloque("press_banca", 4, 5, 8, 180),
                    Bloque("remo_barra", 4, 8, 10, 120),
                    Bloque("press_hombro_mancuernas", 3, 8, 12, 90),
                    Bloque("jalon_agarre_neutro", 3, 10, 12, 90),
                    Bloque("elevaciones_laterales", 3, 12, 15, 60),
                    Bloque("extension_polea", 3, 10, 12, 60),
                    Bloque("curl_mancuernas", 3, 10, 12, 60)
                )
            ),
            DiaRutina(
                nombre = "Pierna 1",
                foto = R.drawable.foto_piernas,
                foco = "Cuadriceps",
                bloques = listOf(
                    Bloque("sentadilla", 4, 5, 8, 210),
                    Bloque("prensa", 3, 10, 12, 120),
                    Bloque("bulgara", 3, 8, 10, 90),
                    Bloque("curl_femoral_sentado", 3, 10, 12, 75),
                    Bloque("gemelo_de_pie", 4, 12, 15, 60),
                    Bloque("crunch_polea", 3, 12, 15, 60)
                )
            ),
            DiaRutina(
                nombre = "Torso 2",
                foto = R.drawable.foto_espalda,
                foco = "Espalda y brazos",
                bloques = listOf(
                    Bloque("dominadas", 4, 6, 10, 150),
                    Bloque("press_incl_mancuernas", 4, 8, 10, 120),
                    Bloque("remo_sentado_polea", 3, 10, 12, 90),
                    Bloque("press_militar", 3, 6, 8, 120),
                    Bloque("face_pull", 3, 15, 20, 60),
                    Bloque("curl_martillo", 3, 10, 12, 60),
                    Bloque("press_frances", 3, 10, 12, 60)
                )
            ),
            DiaRutina(
                nombre = "Pierna 2",
                foto = R.drawable.foto_piernas,
                foco = "Cadena posterior",
                bloques = listOf(
                    Bloque("peso_muerto_rumano", 4, 6, 8, 180),
                    Bloque("hip_thrust", 3, 8, 12, 120),
                    Bloque("zancadas", 3, 10, 12, 90),
                    Bloque("extension_cuadriceps", 3, 12, 15, 60),
                    Bloque("gemelo_sentado", 4, 12, 15, 60),
                    Bloque("elevacion_piernas", 3, 10, 15, 60)
                )
            )
        )
    ),
    Rutina(
        id = "ppl",
        nombre = "Empuje / Tiron / Pierna",
        resumen = "Seis dias, dos vueltas por semana. Solo tiene sentido si duermes bien y comes en serio; si no, tres dias bien hechos rinden mas que seis a medias.",
        diasSemana = 6,
        foto = R.drawable.foto_espalda,
        dias = listOf(
            DiaRutina(
                nombre = "Empuje",
                foto = R.drawable.foto_triceps,
                foco = "Pecho, hombro y triceps",
                bloques = listOf(
                    Bloque("press_banca", 4, 5, 8, 180),
                    Bloque("press_incl_mancuernas", 3, 8, 10, 120),
                    Bloque("press_hombro_maquina", 3, 10, 12, 90),
                    Bloque("aperturas_polea", 3, 12, 15, 60),
                    Bloque("elevaciones_laterales", 4, 12, 20, 60),
                    Bloque("extension_cuerda", 3, 12, 15, 60)
                )
            ),
            DiaRutina(
                nombre = "Tiron",
                foto = R.drawable.foto_espalda,
                foco = "Espalda y biceps",
                bloques = listOf(
                    Bloque("dominadas", 4, 6, 10, 150),
                    Bloque("remo_barra", 4, 8, 10, 120),
                    Bloque("jalon_agarre_neutro", 3, 10, 12, 90),
                    Bloque("remo_mancuerna", 3, 10, 12, 90),
                    Bloque("face_pull", 3, 15, 20, 60),
                    Bloque("curl_barra", 3, 8, 12, 60),
                    Bloque("curl_martillo", 3, 10, 12, 60)
                )
            ),
            DiaRutina(
                nombre = "Pierna",
                foto = R.drawable.foto_piernas,
                foco = "Todo el tren inferior",
                bloques = listOf(
                    Bloque("sentadilla", 4, 5, 8, 210),
                    Bloque("peso_muerto_rumano", 3, 8, 10, 150),
                    Bloque("prensa", 3, 10, 12, 120),
                    Bloque("curl_femoral_tumbado", 3, 10, 12, 75),
                    Bloque("gemelo_de_pie", 4, 12, 15, 60),
                    Bloque("plancha", 3, 30, 60, 60)
                )
            )
        )
    )
)

/**
 * Las rutinas que te has montado tu, cargadas de disco.
 *
 * Es una variable de modulo y no un parametro que se pasa por diez sitios a
 * proposito: [rutinaDe] la usan pantallas que no tienen -- ni deberian tener --
 * acceso al almacenamiento. [Biblioteca] la mantiene al dia cada vez que lee o
 * escribe, que son los dos unicos momentos en que puede cambiar.
 */
private var propias: List<Rutina> = emptyList()

internal fun registrarPropias(lista: List<Rutina>) {
    propias = lista
}

/** Las tres de serie y las tuyas, en ese orden. */
val TODAS_LAS_RUTINAS: List<Rutina> get() = RUTINAS + propias

fun rutinaDe(id: String): Rutina? = TODAS_LAS_RUTINAS.firstOrNull { it.id == id }
