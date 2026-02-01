package com.example.logins.data.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

// Función auxiliar para programar el backup
fun scheduleDailyBackup(context: Context) {

    // Definir restricciones (Las condiciones ideales)
    // Aquí decimos: "No arranques a menos que..."
    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true) // Que tenga batería suficiente
        .setRequiresDeviceIdle(true)    // Que el usuario no esté usando el móvil (opcional, ideal para backups pesados)
        .build()

    // Preparar la solicitud de trabajo
    // Definimos qué ejecutar (BackupWorker) y cada cuanto (24 horas)
    val backupRequest = PeriodicWorkRequestBuilder<BackupWorker>(24, TimeUnit.HOURS)
        .setConstraints(constraints) // Le adjuntamos las reglas de arriba
        .build()

    // encolar trabajo en el sistema
    // Usamos el WorkManager para registrar la tarea
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "CopiaSeguridadDiaria",       // Nombre único para identificar esta tarea
        ExistingPeriodicWorkPolicy.KEEP, // IMPORTANTE: Política de duplicados
        backupRequest
    )
}