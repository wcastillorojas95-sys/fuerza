package com.lucas.fuerza

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.time.LocalDate

/** Una serie hecha: el peso que moviste y cuantas veces. */
data class Serie(val kg: Double, val reps: Int) {
    /** Volumen de la serie. Kilos por repeticion, la cuenta de toda la vida. */
    val volumen: Double get() = kg * reps

    /**
     * Repeticion maxima estimada por la formula de Epley.
     *
     * Es una estimacion, no una medida: por encima de diez repeticiones se va
     * bastante arriba. Vale para comparar una serie con otra del mismo
     * ejercicio, que es justo para lo que se usa aqui.
     */
    val rmEstimado: Double get() = if (reps <= 1) kg else kg * (1.0 + reps / 30.0)
}

/** Todas las series que hiciste hoy de un ejercicio concreto. */
data class EjercicioSesion(val ejercicioId: String, val series: List<Serie>) {
    val volumen: Double get() = series.sumOf { it.volumen }
    val mejor: Serie? get() = series.maxByOrNull { it.rmEstimado }
}

/**
 * Un entreno.
 *
 * [id] es el instante en que empezo, en milisegundos, y hace de clave: dos
 * entrenos no pueden empezar en el mismo milisegundo. [terminada] en false
 * marca la sesion que sigue abierta, que es como la app sabe que estabas a
 * medias si cierras la pantalla y vuelves.
 */
data class Sesion(
    val id: Long,
    val fecha: String,
    val rutinaId: String?,
    val diaNombre: String,
    val ejercicios: List<EjercicioSesion>,
    val duracionSeg: Long,
    val terminada: Boolean
) {
    val volumen: Double get() = ejercicios.sumOf { it.volumen }
    val totalSeries: Int get() = ejercicios.sumOf { it.series.size }
    val vacia: Boolean get() = totalSeries == 0
}

/**
 * Donde viven los entrenos.
 *
 * Un archivo JSON en la carpeta privada de la app y nada mas. No hay Room, y no
 * es por pereza: un ano entrenando cinco dias por semana son doscientas
 * cincuenta sesiones, unos pocos cientos de kilobytes que caben de sobra en
 * memoria. Una base de datos aqui solo anadiria un procesador de anotaciones,
 * migraciones que mantener y una dependencia mas que puede romper la
 * compilacion.
 *
 * Se escribe primero en un archivo temporal y despues se renombra. Renombrar es
 * atomico en el sistema de archivos, asi que si el telefono se apaga a mitad de
 * guardado te quedas con el archivo anterior entero, nunca con uno a medias.
 */
class Almacen(context: Context) {

    private val archivo = File(context.applicationContext.filesDir, "entrenos.json")

    /** Cache en memoria. La lista siempre va de mas reciente a mas antigua. */
    private var cache: MutableList<Sesion> = cargar()

    fun sesiones(): List<Sesion> = cache

    /** Las sesiones ya cerradas, que son las que cuentan para el progreso. */
    fun terminadas(): List<Sesion> = cache.filter { it.terminada && !it.vacia }

    /** La sesion a medias, si la hay. Solo puede haber una. */
    fun abierta(): Sesion? = cache.firstOrNull { !it.terminada }

    fun guardar(sesion: Sesion) {
        val i = cache.indexOfFirst { it.id == sesion.id }
        if (i >= 0) cache[i] = sesion else cache.add(0, sesion)
        cache.sortByDescending { it.id }
        escribir()
    }

    fun borrar(id: Long) {
        cache.removeAll { it.id == id }
        escribir()
    }

    // ------------------------------------------------------------ consultas ---

    /** Kilos totales movidos en un dia. */
    fun volumenDe(dia: LocalDate): Double =
        terminadas().filter { it.fecha == dia.toString() }.sumOf { it.volumen }

    fun sesionesDe(dia: LocalDate): List<Sesion> =
        terminadas().filter { it.fecha == dia.toString() }

    /**
     * El historico de un ejercicio, de mas antiguo a mas reciente.
     *
     * Devuelve un punto por sesion con la mejor serie de ese dia. Es lo que
     * pinta la grafica de progresion: no interesa cada serie suelta, interesa
     * si el techo sube semana a semana.
     */
    fun historicoDe(ejercicioId: String): List<Pair<String, Serie>> =
        terminadas()
            .mapNotNull { s ->
                s.ejercicios.firstOrNull { it.ejercicioId == ejercicioId }?.mejor?.let { s.fecha to it }
            }
            .reversed()

    /** El record: la serie con el mejor maximo estimado de toda la historia. */
    fun recordDe(ejercicioId: String): Serie? =
        terminadas()
            .flatMap { s -> s.ejercicios.filter { it.ejercicioId == ejercicioId } }
            .flatMap { it.series }
            .maxByOrNull { it.rmEstimado }

    /**
     * Lo que hiciste la ultima vez en este ejercicio.
     *
     * Es el dato mas util de toda la app y por eso aparece en la propia pantalla
     * de registro: entrenar es acordarse de que pusiste 60 por 8 el martes
     * pasado, y ese es exactamente el dato que nadie recuerda.
     */
    fun ultimaVezDe(ejercicioId: String, excluyendo: Long = 0L): EjercicioSesion? =
        terminadas()
            .firstOrNull { it.id != excluyendo && it.ejercicios.any { e -> e.ejercicioId == ejercicioId } }
            ?.ejercicios?.firstOrNull { it.ejercicioId == ejercicioId }

    /** Ejercicios distintos que has registrado alguna vez, los mas frecuentes primero. */
    fun ejerciciosUsados(): List<String> =
        terminadas()
            .flatMap { s -> s.ejercicios.map { it.ejercicioId } }
            .groupingBy { it }.eachCount()
            .entries.sortedByDescending { it.value }
            .map { it.key }

    /**
     * Semanas seguidas entrenando, contando hacia atras desde esta.
     *
     * Una semana cuenta si tiene al menos un entreno. La racha se mide en
     * semanas y no en dias a proposito: nadie entrena siete dias, y una racha
     * diaria en una app de gimnasio solo sirve para hacerte sentir mal los
     * martes de descanso.
     */
    fun rachaSemanas(hoy: LocalDate = LocalDate.now()): Int {
        val dias = terminadas().map { it.fecha }.toSet()
        var racha = 0
        var lunes = hoy.minusDays(((hoy.dayOfWeek.value + 6) % 7).toLong())
        while (true) {
            val hay = (0L..6L).any { dias.contains(lunes.plusDays(it).toString()) }
            if (!hay) {
                // La semana en curso todavia puede completarse: no rompe la racha.
                if (racha == 0 && lunes.plusDays(6) >= hoy) { lunes = lunes.minusWeeks(1); continue }
                return racha
            }
            racha++
            lunes = lunes.minusWeeks(1)
            if (racha > 520) return racha
        }
    }

    // --------------------------------------------------------------- disco ---

    private fun cargar(): MutableList<Sesion> {
        if (!archivo.exists()) return mutableListOf()
        return try {
            val raiz = JSONArray(archivo.readText())
            val lista = ArrayList<Sesion>(raiz.length())
            for (i in 0 until raiz.length()) lista.add(leerSesion(raiz.getJSONObject(i)))
            lista.sortByDescending { it.id }
            lista
        } catch (e: Exception) {
            // Un archivo corrupto no puede impedir que la app abra. Se aparta con
            // otro nombre por si algun dia quieres rescatarlo a mano.
            runCatching { archivo.renameTo(File(archivo.parentFile, "entrenos-roto.json")) }
            mutableListOf()
        }
    }

    private fun escribir() {
        val raiz = JSONArray()
        cache.forEach { raiz.put(escribirSesion(it)) }
        val tmp = File(archivo.parentFile, "entrenos.tmp")
        tmp.writeText(raiz.toString())
        tmp.renameTo(archivo)
    }

    private fun leerSesion(o: JSONObject): Sesion {
        val ejs = o.optJSONArray("ejercicios") ?: JSONArray()
        val lista = ArrayList<EjercicioSesion>(ejs.length())
        for (i in 0 until ejs.length()) {
            val eo = ejs.getJSONObject(i)
            val sa = eo.optJSONArray("series") ?: JSONArray()
            val series = ArrayList<Serie>(sa.length())
            for (j in 0 until sa.length()) {
                val so = sa.getJSONObject(j)
                series.add(Serie(so.optDouble("kg", 0.0), so.optInt("reps", 0)))
            }
            lista.add(EjercicioSesion(eo.optString("id"), series))
        }
        return Sesion(
            id = o.optLong("inicio"),
            fecha = o.optString("fecha"),
            rutinaId = if (o.isNull("rutina")) null else o.optString("rutina"),
            diaNombre = o.optString("dia"),
            ejercicios = lista,
            duracionSeg = o.optLong("duracion"),
            terminada = o.optBoolean("terminada", true)
        )
    }

    private fun escribirSesion(s: Sesion): JSONObject {
        val ejs = JSONArray()
        s.ejercicios.forEach { e ->
            val sa = JSONArray()
            e.series.forEach { sa.put(JSONObject().put("kg", it.kg).put("reps", it.reps)) }
            ejs.put(JSONObject().put("id", e.ejercicioId).put("series", sa))
        }
        return JSONObject()
            .put("inicio", s.id)
            .put("fecha", s.fecha)
            .put("rutina", s.rutinaId ?: JSONObject.NULL)
            .put("dia", s.diaNombre)
            .put("duracion", s.duracionSeg)
            .put("terminada", s.terminada)
            .put("ejercicios", ejs)
    }
}
