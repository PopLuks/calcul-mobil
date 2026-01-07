package com.example.lab8

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.lab8.ui.theme.Lab8Theme

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"

    // Register for notification permission result
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "Notification permission granted")
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "Notification permission denied")
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d(TAG, "MainActivity created")

        // Request notification permission for Android 13+
        checkAndRequestNotificationPermission()

        setContent {
            Lab8Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SoundServiceControl(
                        modifier = Modifier.padding(innerPadding),
                        onStartService = { startSoundService() },
                        onStopService = { stopSoundService() }
                    )
                }
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d(TAG, "Notification permission already granted")
                }
                else -> {
                    // Request permission
                    Log.d(TAG, "Requesting notification permission")
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun startSoundService() {
        Log.d(TAG, "Starting sound service")
        try {
            val intent = Intent(this, SoundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error starting service: ${e.message}", e)
            Toast.makeText(this, "Error starting service: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun stopSoundService() {
        Log.d(TAG, "Stopping sound service")
        try {
            val intent = Intent(this, SoundService::class.java)
            stopService(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping service: ${e.message}", e)
            Toast.makeText(this, "Error stopping service: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun SoundServiceControl(
    modifier: Modifier = Modifier,
    onStartService: () -> Unit = {},
    onStopService: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎵 Sound Service Controller",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Visual status indicator
        Text(
            text = "📱 Status:",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Apasă START pentru sunet 🔊",
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Button(
                onClick = onStartService,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text("▶️ START", fontSize = 16.sp)
            }

            Button(
                onClick = onStopService,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text("⏹️ STOP", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Instructions
        Column(
            modifier = Modifier.padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📋 Cum să testezi:",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "1️⃣ Ridică volumul emulatorului\n" +
                       "2️⃣ Apasă START\n" +
                       "3️⃣ Ar trebui să AUZI ringtone-ul 🔊\n" +
                       "4️⃣ Vezi notificarea în status bar\n" +
                       "5️⃣ Apasă STOP pentru a opri",
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SoundServiceControlPreview() {
    Lab8Theme {
        SoundServiceControl()
    }
}