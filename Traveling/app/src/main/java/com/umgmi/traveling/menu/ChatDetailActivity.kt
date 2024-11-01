package com.umgmi.traveling.menu

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

class ChatDetailActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var chatId = ""
    private val messages = mutableStateListOf<Message>()
    private var messageText by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Obtener el email del usuario con quien se chatea
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: ""
        chatId = generateChatId(userEmail)

        loadMessages() // Cargar mensajes al iniciar

        setContent {
            ChatDetailScreen(userEmail)
        }
    }

    private fun generateChatId(otherUserEmail: String): String {
        val currentUserEmail = auth.currentUser?.email ?: ""
        // Crear un ID de chat único basado en los correos de los usuarios
        return if (currentUserEmail < otherUserEmail) {
            "$currentUserEmail|$otherUserEmail"
        } else {
            "$otherUserEmail|$currentUserEmail"
        }
    }

    private fun loadMessages() {
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("ChatDetailActivity", "Error al cargar mensajes: ", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    messages.clear() // Limpiar la lista de mensajes antes de agregar los nuevos
                    for (doc in snapshot.documents) {
                        val message = doc.toObject<Message>()
                        message?.let { messages.add(it) }
                    }
                    Log.d("ChatDetailActivity", "Mensajes cargados: ${messages.size}")
                }
            }
    }

    private fun sendMessage() {
        if (messageText.isNotBlank()) {
            val message = Message(senderId = auth.currentUser?.email ?: "", text = messageText)
            db.collection("chats").document(chatId)
                .collection("messages").add(message)
                .addOnSuccessListener {
                    messageText = "" // Limpiar el campo de texto después de enviar el mensaje
                }
        }
    }

    @Composable
    fun ChatDetailScreen(userEmail: String) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Chat con $userEmail",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                items(messages) { message ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "${message.senderId}: ${message.text}")
                            Text(text = message.timestamp.toString(), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Escribe un mensaje...") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        sendMessage()
                    }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { sendMessage() }, modifier = Modifier.fillMaxWidth()) {
                Text("Enviar")
            }
        }
    }
}
