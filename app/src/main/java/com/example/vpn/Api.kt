package com.example.vpn

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object Api {
    private val client = OkHttpClient()

    suspend fun getIpAddress(): String? {
        return withContext(Dispatchers.IO) { // Выполняем в потоке IO
            val request = Request.Builder()
                .url("https://api.ipify.org")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.string() // Возвращаем тело ответа как строку
                } else {
                    null
                }
            } catch (e: IOException) {
                null
            }
        }
    }
}