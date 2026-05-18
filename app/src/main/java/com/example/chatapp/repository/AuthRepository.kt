package com.example.chatapp.repository

import com.example.chatapp.model.ChatRoom
import com.example.chatapp.model.Message
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun register(name: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid
                val user = User(uid = uid, name = name, email = email)

                db.collection("users").document(uid).set(user)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
            .addOnFailureListener { onResult(false) }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            onResult(false)
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            onResult(it.isSuccessful)
        }
    }

    fun getUsers(onResult: (List<User>) -> Unit) {
        db.collection("users").get()
            .addOnSuccessListener {
                val users = it.toObjects(User::class.java)
                onResult(users)
            }
    }

    fun getUser(uid: String, onResult: (User) -> Unit) {
        db.collection("users").document(uid).addSnapshotListener { value, _ ->
            val user = value?.toObject(User::class.java)
            if (user != null) {
                onResult(user)
            }
        }
    }

    fun sendMessage(receiverId: String, receiverName: String, text: String) {
        val senderId = auth.currentUser!!.uid

        val message = Message(
            senderId = senderId,
            receiverId = receiverId,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        db.collection("messages").add(message)

        val myChat = ChatRoom(
            uid = receiverId,
            name = receiverName,
            lastMessage = text,
            timestamp = System.currentTimeMillis()
        )
        db.collection("chat_list").document(senderId).collection("users").document(receiverId).set(myChat)

        db.collection("users").document(senderId).get()
            .addOnSuccessListener { senderDoc ->
                val myName = senderDoc.getString("name") ?: ""

                val receiverChat = ChatRoom(
                    uid = senderId,
                    name = myName,
                    lastMessage = text,
                    timestamp = System.currentTimeMillis()
                )
                db.collection("chat_list").document(receiverId).collection("users").document(senderId).set(receiverChat)
            }
    }

    fun getMessages(receiverId: String, onResult: (List<Message>) -> Unit) {
        val senderId = auth.currentUser!!.uid

        db.collection("messages").addSnapshotListener { value, _ ->
            if (value == null) {
                onResult(emptyList())
                return@addSnapshotListener
            }

            val messages = value.toObjects(Message::class.java)
            val filtered = messages.filter {
                (it.senderId == senderId && it.receiverId == receiverId) || (it.senderId == receiverId && it.receiverId == senderId)
            }
            val sorted = filtered.sortedBy { it.timestamp }
            onResult(sorted)
        }
    }

    fun getChatList(onResult: (List<ChatRoom>) -> Unit) {
        val myId = auth.currentUser!!.uid

        db.collection("chat_list").document(myId).collection("users").addSnapshotListener { value, _ ->
            if (value == null) {
                onResult(emptyList())
                return@addSnapshotListener
            }

            val chats = value.toObjects(ChatRoom::class.java).sortedByDescending { it.timestamp }
            onResult(chats)
        }
    }

    fun updateStatus(isOnline: Boolean) {
        val user = auth.currentUser ?: return
        val uid = user.uid

        db.collection("users").document(uid).update("online", isOnline)
    }

    fun markMessagesSeen(receiverId: String) {
        val myId = auth.currentUser!!.uid

        db.collection("messages").get()
            .addOnSuccessListener { snapshot ->
                for (doc in snapshot.documents) {
                    val message = doc.toObject(Message::class.java)
                    if (message != null && message.senderId == receiverId && message.receiverId == myId) {
                        doc.reference.update("seen", true)
                    }
                }
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser(onResult: (User) -> Unit) {
        val uid = auth.currentUser!!.uid

        db.collection("users").document(uid).addSnapshotListener { value, _ ->
            if (value != null) {
                val user = value.toObject(User::class.java)
                if (user != null) {
                    onResult(user)
                }
            }
        }
    }
}