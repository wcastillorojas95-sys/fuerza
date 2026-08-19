package com.lucas.fuerza

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/**
 * El aviso de fin de descanso.
 *
 * Va con AlarmManager y no con un servicio en primer plano. Un servicio
 * obligaria, desde Android 14, a declarar un tipo de servicio -- y ninguno de
 * los que existen describe bien "contar noventa segundos" -- ademas de pelearse
 * con los limites de ejecucion en segundo plano.
 *
 * setAlarmClock, en cambio, es exacto al segundo, atraviesa el modo Doze con la
 * pantalla apagada y no pide ningun permiso especial: Android lo trata como un
 * despertador, que es exactamente lo que es. El precio es el iconito de alarma
 * en la barra de estado, que ademas hace de recordatorio visual de que el
 * descanso esta corriendo.
 */
class AvisoDescanso : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        vibrar(context)
        notificar(context, intent.getStringExtra(EXTRA_EJERCICIO))
    }

    private fun vibrar(context: Context) {
        val vibrador = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val gestor = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            gestor?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
        // Dos toques cortos y uno largo: se distingue de una notificacion normal
        // aunque el telefono este en el bolsillo.
        val patron = longArrayOf(0, 180, 120, 180, 120, 420)
        vibrador?.vibrate(VibrationEffect.createWaveform(patron, -1))
    }

    private fun notificar(context: Context, ejercicio: String?) {
        crearCanal(context)
        val abrir = PendingIntent.getActivity(
            context, 0,
            Intent(context, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val aviso = androidx.core.app.NotificationCompat.Builder(context, CANAL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Se acabo el descanso")
            .setContentText(ejercicio?.let { "Siguiente serie de $it" } ?: "A por la siguiente serie")
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setCategory(androidx.core.app.NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(abrir)
            .build()

        val puedeNotificar = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        if (puedeNotificar) {
            NotificationManagerCompat.from(context).notify(ID_AVISO, aviso)
        }
    }

    companion object {
        private const val CANAL = "descansos"
        private const val ID_AVISO = 1
        private const val EXTRA_EJERCICIO = "ejercicio"

        private fun crearCanal(context: Context) {
            val gestor = context.getSystemService(NotificationManager::class.java) ?: return
            if (gestor.getNotificationChannel(CANAL) != null) return
            gestor.createNotificationChannel(
                NotificationChannel(
                    CANAL,
                    context.getString(R.string.canal_descanso),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = context.getString(R.string.canal_descanso_desc)
                    enableVibration(false) // la vibracion la pone el receptor, con su patron
                }
            )
        }

        private fun pendiente(context: Context, ejercicio: String?): PendingIntent =
            PendingIntent.getBroadcast(
                context, 42,
                Intent(context, AvisoDescanso::class.java).putExtra(EXTRA_EJERCICIO, ejercicio),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        /** Programa el aviso para dentro de [segundos]. */
        fun programar(context: Context, segundos: Int, ejercicio: String?) {
            crearCanal(context)
            val gestor = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val cuando = System.currentTimeMillis() + segundos * 1000L
            val abrir = PendingIntent.getActivity(
                context, 1, Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            runCatching {
                gestor.setAlarmClock(AlarmManager.AlarmClockInfo(cuando, abrir), pendiente(context, ejercicio))
            }
        }

        /** Cancela el aviso pendiente, si lo hay. */
        fun cancelar(context: Context) {
            val gestor = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            runCatching { gestor.cancel(pendiente(context, null)) }
            runCatching { NotificationManagerCompat.from(context).cancel(ID_AVISO) }
        }
    }
}
