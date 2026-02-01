package com.example.logins.data.repository

import android.content.Context
import com.example.logins.data.crypto.CryptoManager
import java.io.File

class SecureNotesRepository(private val context: Context) {

    private val notesDir: File = context.filesDir

    // 🔐 GUARDAR (NUEVA CONTRASEÑA)
    fun savePassword(family: String, password: String) {
        val plainText = "$family|$password"
        val encryptedData = CryptoManager.encrypt(plainText)

        val fileName = "pwd_${System.currentTimeMillis()}.dat"
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(encryptedData)
        }
    }

    // 📋 OBTENER TODAS (DESENCRIPTA AL LEER)
    fun getPasswords(): List<PasswordEntry> {
        return notesDir.listFiles()?.mapNotNull { file ->
            try {
                val decrypted = CryptoManager.decrypt(file.readBytes())
                val parts = decrypted.split("|")

                if (parts.size == 2) {
                    PasswordEntry(
                        id = file.name,
                        family = parts[0],
                        password = parts[1]
                    )
                } else null
            } catch (e: Exception) {
                null
            }
        } ?: emptyList()
    }

    // 🔍 OBTENER UNA POR ID
    fun getPasswordById(id: String): PasswordEntry? {
        val file = File(notesDir, id)
        if (!file.exists()) return null

        val decrypted = CryptoManager.decrypt(file.readBytes())
        val parts = decrypted.split("|")

        return if (parts.size == 2) {
            PasswordEntry(id, parts[0], parts[1])
        } else null
    }

    // ✏️ EDITAR (BORRAR + CREAR)
    fun updatePassword(entry: PasswordEntry) {
        deletePassword(entry.id)
        savePassword(entry.family, entry.password)
    }

    // 🗑️ ELIMINAR
    fun deletePassword(id: String) {
        File(notesDir, id).delete()
    }
}
