package com.lucas.fuerza

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * Tus rutinas, las que te montas tu.
 *
 * Un archivo JSON en la carpeta privada de la app, con el mismo criterio que
 * [Almacen]: se escribe en un temporal y se renombra, que es atomico, asi que
 * un apagon a mitad de guardado te deja el archivo anterior entero y nunca uno
 * a medias.
 *
 * Una rutina tuya es **un dia con sus ejercicios**. No hay dias multiples a
 * proposito: montar un Dia 1, un Dia 2 y un Dia 3 antes de poder entrenar es
 * justo el trabajo que hace que la gente cierre la app y se ponga a levantar
 * sin apuntar nada. Si quieres dos dias, te haces dos rutinas.
 */
class Biblioteca(context: Context) {

    private val archivo = File(context.applicationContext.filesDir, "mis_rutinas.json")

    /** Cache en memoria. Son cuatro rutinas, no hace falta mas ceremonia. */
    private var cache: List<Rutina>? = null

    /** Las tuyas, de la mas nueva a la mas vieja. */
    fun mias(): List<Rutina> {
        cache?.let { return it }
        val leidas = leer()
        cache = leidas
        registrarPropias(leidas)
        return leidas
    }

    /**
     * Guarda una rutina nueva con los ejercicios que hayas elegido.
     *
     * Las series y las repeticiones salen de [bloquePorDefecto]: lo pesado a
     * cuatro por seis-ocho y lo de aislar a tres por diez-doce. Se pueden
     * cambiar durante el entreno, que es cuando de verdad sabes si te sobra
     * una serie.
     */
    fun guardar(nombre: String, ejercicios: List<String>): Rutina {
        val limpio = nombre.trim().ifBlank { "Rutina sin nombre" }
        val nueva = construir(
            id = "mia_" + System.currentTimeMillis(),
            nombre = limpio,
            bloques = ejercicios.map { bloquePorDefecto(it) }
        )
        val todas = listOf(nueva) + mias()
        escribir(todas)
        cache = todas
        registrarPropias(todas)
        return nueva
    }

    fun borrar(id: String) {
        val todas = mias().filterNot { it.id == id }
        escribir(todas)
        cache = todas
        registrarPropias(todas)
    }

    // ------------------------------------------------------------- disco ---

    private fun leer(): List<Rutina> {
        if (!archivo.exists()) return emptyList()
        return runCatching {
            val arr = JSONArray(archivo.readText())
            (0 until arr.length()).mapNotNull { i -> deJson(arr.getJSONObject(i)) }
        }.getOrDefault(emptyList())
    }

    private fun escribir(lista: List<Rutina>) {
        val arr = JSONArray()
        lista.forEach { arr.put(aJson(it)) }
        val temporal = File(archivo.parentFile, archivo.name + ".tmp")
        temporal.writeText(arr.toString())
        temporal.renameTo(archivo)
    }

    private fun aJson(r: Rutina): JSONObject {
        val ejercicios = JSONArray()
        r.dias.firstOrNull()?.bloques?.forEach { b ->
            ejercicios.put(
                JSONObject()
                    .put("ejercicio", b.ejercicioId)
                    .put("series", b.series)
                    .put("min", b.repsMin)
                    .put("max", b.repsMax)
                    .put("descanso", b.descansoSeg)
            )
        }
        return JSONObject()
            .put("id", r.id)
            .put("nombre", r.nombre)
            .put("ejercicios", ejercicios)
    }

    /**
     * Una rutina guardada vuelve a la vida.
     *
     * Los ejercicios que ya no existan en el catalogo se caen sin ruido: es
     * mejor una rutina con cinco de seis que una pantalla en blanco.
     */
    private fun deJson(o: JSONObject): Rutina? {
        val id = o.optString("id").ifBlank { return null }
        val nombre = o.optString("nombre").ifBlank { "Rutina sin nombre" }
        val arr = o.optJSONArray("ejercicios") ?: JSONArray()
        val bloques = (0 until arr.length()).mapNotNull { i ->
            val b = arr.getJSONObject(i)
            val ejercicio = b.optString("ejercicio")
            if (ejercicioDe(ejercicio) == null) null
            else Bloque(
                ejercicioId = ejercicio,
                series = b.optInt("series", 3),
                repsMin = b.optInt("min", 8),
                repsMax = b.optInt("max", 12),
                descansoSeg = b.optInt("descanso", 90)
            )
        }
        if (bloques.isEmpty()) return null
        return construir(id, nombre, bloques)
    }
}

/** Lo que se propone al meter un ejercicio en una rutina tuya. */
fun bloquePorDefecto(ejercicioId: String): Bloque {
    val e = ejercicioDe(ejercicioId)
    return if (e?.compuesto == true) Bloque(ejercicioId, 4, 6, 8, 150)
    else Bloque(ejercicioId, 3, 10, 12, 75)
}

/**
 * Monta la [Rutina] a partir de sus ejercicios.
 *
 * El foco y la foto no se preguntan: se deducen de los musculos que mas se
 * repiten. Una rutina de seis ejercicios de biceps se llama "Biceps" y sale con
 * la foto de biceps sin que tengas que elegir nada.
 */
private fun construir(id: String, nombre: String, bloques: List<Bloque>): Rutina {
    val musculos = bloques.mapNotNull { ejercicioDe(it.ejercicioId)?.musculo }
    val mandan = musculos.groupingBy { it }.eachCount()
        .entries.sortedByDescending { it.value }.take(2).map { it.key }

    return Rutina(
        id = id,
        nombre = nombre,
        resumen = "Tuya. ${bloques.size} ejercicios, " +
            "${bloques.sumOf { it.series }} series en total.",
        diasSemana = 1,
        dias = listOf(
            DiaRutina(
                nombre = nombre,
                foco = mandan.joinToString(" y ") { it.etiqueta }.ifBlank { "Tu seleccion" },
                bloques = bloques,
                foto = fotoDe(mandan.firstOrNull())
            )
        ),
        foto = fotoDe(mandan.firstOrNull()),
        propia = true
    )
}

/** Que foto de las seis le toca a un musculo. */
private fun fotoDe(musculo: Musculo?): Int = when (musculo) {
    Musculo.PECHO -> R.drawable.foto_pecho
    Musculo.ESPALDA, Musculo.TRAPECIO -> R.drawable.foto_espalda
    Musculo.BICEPS -> R.drawable.foto_biceps
    Musculo.TRICEPS -> R.drawable.foto_triceps
    Musculo.CUADRICEPS, Musculo.FEMORAL, Musculo.GLUTEO, Musculo.GEMELO -> R.drawable.foto_piernas
    else -> R.drawable.foto_full_body
}
