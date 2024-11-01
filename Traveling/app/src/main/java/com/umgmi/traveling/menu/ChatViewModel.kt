package com.umgmi.traveling.menu

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.firebase.firestore.ktx.toObject

class ChatViewModel(private val chatId: String) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    init {
        loadMessages()
    }

    private fun loadMessages() {
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    val messagesList = it.documents.mapNotNull { doc ->
                        doc.toObject<Message>()
                    }
                    _messages.value = messagesList
                }
            }
    }

    fun sendMessage(text: String, senderId: String) {
        val message = Message(text = text, senderId = senderId)
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)
    }
}
