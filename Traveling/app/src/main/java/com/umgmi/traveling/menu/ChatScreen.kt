package com.umgmi.traveling.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val messages by viewModel.messages.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Lista de mensajes
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(messages) { message ->
                MessageItem(message)
            }
        }

        // Campo de entrada de mensaje
        MessageInput(onSend = { text ->
            coroutineScope.launch {
                viewModel.sendMessage(text, senderId = "user1") // Cambia "user1" por el ID de usuario actual
            }
        })
    }
}

@Composable
fun MessageItem(message: Message) {
    Text(
        text = message.text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun MessageInput(onSend: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Escribe un mensaje") },
            maxLines = 1,
            singleLine = true
        )
        IconButton(
            onClick = {
                if (text.isNotEmpty()) {
                    onSend(text)
                    text = "" // Limpiar el campo de entrada
                }
            },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(Icons.Default.Send, contentDescription = "Enviar Mensaje")
        }
    }
}
