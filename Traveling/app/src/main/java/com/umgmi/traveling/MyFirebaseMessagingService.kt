package com.umgmi.traveling

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.umgmi.traveling.menu.Confir // Importa la actividad que deseas abrir al hacer clic en la notificación

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Manejar el mensaje recibido
        Log.d("FCM Message", "Message received: ${remoteMessage.notification?.title}, ${remoteMessage.notification?.body}")

        // Manejo de la notificación
        remoteMessage.notification?.let {
            showNotification(it.title, it.body)
        }

        // Manejo de datos personalizados (si es necesario)
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCM Data", "Data payload: ${remoteMessage.data}")
            // Aquí puedes extraer datos personalizados si los necesitas
            // Por ejemplo: val reservationId = remoteMessage.data["reservation_id"]
        }
    }

    private fun showNotification(title: String?, message: String?) {
        val intent = Intent(this, Confir::class.java).apply {
            // Aquí puedes agregar datos extras si los necesitas
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Actualiza aquí
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "reservas_channel_id"
        val channelName = "Reservas"

        // Crear el canal de notificación para Android O y superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Canal para notificaciones de reservas"
                // Configura el sonido y la vibración del canal si es necesario
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Cambia el ícono según tu preferencia
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // Sonido de notificación
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000)) // Vibrar cada segundo

        notificationManager.notify(0, notificationBuilder.build()) // 0 es el ID de la notificación
    }
}
