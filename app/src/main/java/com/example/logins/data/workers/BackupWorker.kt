package com.example.logins.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.logins.data.repository.SecureNotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BackupWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Accedemos al repositorio
                // Nota: En una app real usaríamos inyección de dependencias (Hilt/Koin)
                // Aquí lo instanciamos manualmente por simplicidad
                val repository = SecureNotesRepository(applicationContext)
                val passwords = repository.getPasswords()

                if (passwords.isEmpty()) {
                    return@withContext Result.success()
                }

                // 2. Simulamos el Backup (ej. contar cuántas hay y loguearlo)
                // Aquí podrías copiar los archivos a una carpeta externa o subir a drive
                Log.d("BackupWorker", "Iniciando backup de ${passwords.size} contraseñas...")

                // Simulación de tarea pesada
                Thread.sleep(2000)

                Log.d("BackupWorker", "Backup completado con éxito.")
                Result.success()
            } catch (e: Exception) {
                Log.e("BackupWorker", "Error en backup", e)
                Result.retry() // WorkManager volverá a intentarlo más tarde si falla
            }
        }
    }
}