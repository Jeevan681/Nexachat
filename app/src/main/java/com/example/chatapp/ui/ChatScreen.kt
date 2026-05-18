package com.example.chatapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatapp.model.Message
import com.example.chatapp.model.User
import com.example.chatapp.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(receiverId: String, receiverName: String) {
    val viewModel = AuthViewModel()
    var user by remember { mutableStateOf(User()) }

    LaunchedEffect(Unit) {
        viewModel.markMessagesSeen(receiverId)
        viewModel.getUser(receiverId) { user = it }
    }

    val myId = FirebaseAuth.getInstance().currentUser!!.uid
    var text by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(emptyList<Message>()) }
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.getMessages(receiverId) { messages = it }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(user.name)
                            Text(
                                text = if (user.online) "online" else "offline",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(8.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(items = messages, key = { _, item -> item.timestamp }) { index, message ->
                    val currentDate = getDateLabel(message.timestamp)
                    val previousDate = if (index > 0) getDateLabel(messages[index - 1].timestamp) else ""

                    if (currentDate != previousDate) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text(text = currentDate, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    val isMe = message.senderId == myId

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.padding(4.dp).widthIn(max = 250.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = message.text,
                                    color = if (isMe) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(message.timestamp))
                                Text(text = time, style = MaterialTheme.typography.bodySmall)

                                if (isMe) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = if (message.seen) "✓✓" else "✓", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    placeholder = { Text("Message") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (text.isNotBlank()) {
                            viewModel.sendMessage(receiverId, receiverName, text)
                            text = ""
                        }
                    },
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Send")
                }
            }
        }
    }
}

fun getDateLabel(time: Long): String {
    val today = Calendar.getInstance()
    val messageDate = Calendar.getInstance().apply { timeInMillis = time }

    if (today.get(Calendar.DAY_OF_YEAR) == messageDate.get(Calendar.DAY_OF_YEAR)) {
        return "Today"
    }

    today.add(Calendar.DAY_OF_YEAR, -1)
    if (today.get(Calendar.DAY_OF_YEAR) == messageDate.get(Calendar.DAY_OF_YEAR)) {
        return "Yesterday"
    }

    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(time))
}