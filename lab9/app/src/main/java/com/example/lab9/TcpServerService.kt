package com.example.lab9

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class TcpServerService : Service() {

    private val TAG = "TcpServerService"
    private var serverSocket: ServerSocket? = null
    private var isRunning = false

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            isRunning = true
            thread {
                startServer()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        serverSocket?.close()
        Log.d(TAG, "Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startServer() {
        try {
            serverSocket = ServerSocket(8080)
            Log.d(TAG, "Server started on port 8080")
            while (isRunning) {
                val clientSocket = serverSocket?.accept()
                clientSocket?.let {
                    thread {
                        handleClient(it)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Server error", e)
        } finally {
            serverSocket?.close()
        }
    }

    private fun handleClient(socket: Socket) {
        try {
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val writer = PrintWriter(socket.getOutputStream(), true)
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                Log.d(TAG, "Received: $line")
                writer.println("Echo: $line")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Client handling error", e)
        } finally {
            socket.close()
        }
    }
}
