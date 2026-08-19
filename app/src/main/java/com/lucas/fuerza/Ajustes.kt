package com.lucas.fuerza

import android.content.Context
import androidx.annotation.DrawableRes

/**
 * Para que entrenas. Cambia las repeticiones que sugiere la app, nada mas.
 *
 * Cada uno lleva su dibujo porque los tres se eligen en la misma lista y de un
 * vistazo: el biceps, el que marca fuerza y el de mantenerse en movimiento.
 */
enum class Objetivo(
    val etiqueta: String,
    val corto: String,
    @DrawableRes val icono: Int
) {
    MUSCULO("Ganar musculo", "MUSCULO", R.drawable.ic_objetivo_musculo),
    FUERZA("Ganar fuerza", "FUERZA", R.drawable.ic_objetivo_fuerza),
    MANTENER("Mantenerme", "MANTENER", R.drawable.ic_objetivo_mantener)
}

/**
 * Lo poco que Fuerza recuerda ademas de los entrenos.
 *
 * Preferencias sueltas y nada mas: que rutina sigues, por que dia vas y como
 * quieres que avise el descanso. Los entrenos viven aparte, en [Almacen].
 */
class Ajustes(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)

    /** La rutina que sigues. null = vas por libre. */
    var rutinaId: String?
        get() = prefs.getString(CLAVE_RUTINA, null)
        set(valor) = prefs.edit().putString(CLAVE_RUTINA, valor).apply()

    /**
     * Por que dia de la rutina vas.
     *
     * Es un contador que solo avanza al terminar un entreno, no una fecha. Asi
     * si te saltas el martes, el martes que viene te toca lo que te tocaba: la
     * rutina no se descuadra por faltar un dia, que es lo que hace que la gente
     * la abandone.
     */
    var diaIndice: Int
        get() = prefs.getInt(CLAVE_DIA, 0)
        set(valor) = prefs.edit().putInt(CLAVE_DIA, valor.coerceAtLeast(0)).apply()

    fun avanzarDia(totalDias: Int) {
        if (totalDias > 0) diaIndice = (diaIndice + 1) % totalDias
    }

    var objetivo: Objetivo
        get() = runCatching { Objetivo.valueOf(prefs.getString(CLAVE_OBJETIVO, null) ?: "") }
            .getOrDefault(Objetivo.MUSCULO)
        set(valor) = prefs.edit().putString(CLAVE_OBJETIVO, valor.name).apply()

    /** Aviso con notificacion y vibracion al terminar el descanso. */
    var avisarDescanso: Boolean
        get() = prefs.getBoolean(CLAVE_AVISO, true)
        set(valor) = prefs.edit().putBoolean(CLAVE_AVISO, valor).apply()

    /**
     * Mantener la pantalla encendida durante el entreno.
     *
     * Suena a capricho y no lo es: apagar el bloqueo del telefono cada dos
     * minutos con las manos llenas de magnesio es lo que hace que la gente deje
     * de apuntar las series a mitad de sesion.
     */
    var pantallaEncendida: Boolean
        get() = prefs.getBoolean(CLAVE_PANTALLA, true)
        set(valor) = prefs.edit().putBoolean(CLAVE_PANTALLA, valor).apply()

    /** Cuanto suma o resta el boton rapido de peso. */
    var incrementoKg: Double
        get() = prefs.getFloat(CLAVE_INCREMENTO, 2.5f).toDouble()
        set(valor) = prefs.edit().putFloat(CLAVE_INCREMENTO, valor.toFloat()).apply()

    private companion object {
        const val ARCHIVO = "fuerza_ajustes"
        const val CLAVE_RUTINA = "rutina"
        const val CLAVE_DIA = "dia_indice"
        const val CLAVE_OBJETIVO = "objetivo"
        const val CLAVE_AVISO = "avisar_descanso"
        const val CLAVE_PANTALLA = "pantalla_encendida"
        const val CLAVE_INCREMENTO = "incremento_kg"
    }
}
