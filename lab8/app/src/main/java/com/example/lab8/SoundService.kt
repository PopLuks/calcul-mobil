package com.example.lab8

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat

class SoundService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private val TAG = "SoundService"
    private val CHANNEL_ID = "SoundServiceChannel"
    private val NOTIFICATION_ID = 1

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")
        Log.d(TAG, "========================================")
        Log.d(TAG, "🎵 STARTING SOUND SERVICE")
        Log.d(TAG, "========================================")

        try {
            // Create notification for foreground service
            val notification = createNotification()
            Log.d(TAG, "✅ Notification created")

            // Start foreground with proper type for Android 14+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
                Log.d(TAG, "✅ Started as foreground service (Android 14+)")
            } else {
                startForeground(NOTIFICATION_ID, notification)
                Log.d(TAG, "✅ Started as foreground service")
            }

            // Initialize and start MediaPlayer with default ringtone
            val ringtoneUri = Settings.System.DEFAULT_RINGTONE_URI
            Log.d(TAG, "📱 Ringtone URI: $ringtoneUri")

            if (ringtoneUri != null) {
                mediaPlayer = MediaPlayer.create(this, ringtoneUri)
                Log.d(TAG, "🎵 MediaPlayer created: ${mediaPlayer != null}")

                mediaPlayer?.let { player ->
                    player.isLooping = true
                    Log.d(TAG, "🔁 Looping enabled")

                    player.start()
                    Log.d(TAG, "========================================")
                    Log.d(TAG, "🔊 MEDIAPLAY IS NOW PLAYING!")
                    Log.d(TAG, "🔊 YOU SHOULD HEAR THE RINGTONE NOW!")
                    Log.d(TAG, "========================================")

                    Toast.makeText(this, "🔊 Service Started - LISTEN FOR SOUND!", Toast.LENGTH_LONG).show()
                } ?: run {
                    Log.e(TAG, "❌ Failed to create MediaPlayer")
                    Toast.makeText(this, "❌ Failed to create media player", Toast.LENGTH_LONG).show()
                    stopSelf()
                }
            } else {
                Log.e(TAG, "❌ No default ringtone found")
                Toast.makeText(this, "❌ No ringtone available", Toast.LENGTH_LONG).show()
                stopSelf()
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error starting service: ${e.message}", e)
            Toast.makeText(this, "❌ Error: ${e.message}", Toast.LENGTH_LONG).show()
            stopSelf()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "========================================")
        Log.d(TAG, "🛑 STOPPING SOUND SERVICE")
        Log.d(TAG, "========================================")

        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                    Log.d(TAG, "⏹️ MediaPlayer stopped")
                }
                player.release()
                Log.d(TAG, "✅ MediaPlayer released")
            }
            mediaPlayer = null

            Log.d(TAG, "========================================")
            Log.d(TAG, "🔇 SOUND SHOULD BE STOPPED NOW")
            Log.d(TAG, "========================================")

            Toast.makeText(this, "🔇 Service Stopped - Sound OFF", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error stopping service: ${e.message}", e)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Sound Service Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Channel for Sound Service"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sound Service")
            .setContentText("Playing ringtone...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }
}

