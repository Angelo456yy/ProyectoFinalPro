package com.umgmi.traveling.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ChatListScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val chatList = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        currentUser?.let { user ->
            db.collection("chats")
                .whereArrayContains("participants", user.uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    chatList.clear()
                    snapshot.documents.forEach { doc ->
                        chatList.add(doc.id)
                    }
                }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Chats Activos", modifier = Modifier.padding(bottom = 16.dp))

        chatList.forEach { chatId ->
            Text(
                text = "Chat: $chatId",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("chatScreen/$chatId")
                    }
                    .padding(8.dp)
            )
        }
    }
}
