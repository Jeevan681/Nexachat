package com.example.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.chatapp.model.ChatRoom
import com.example.chatapp.model.User
import com.example.chatapp.repository.AuthRepository
import com.example.chatapp.model.Message

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    fun register(name: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        repo.register(name, email, password, onResult)
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        repo.login(email, password, onResult)
    }

    fun getUsers(onResult: (List<User>) -> Unit) {
        repo.getUsers(onResult)
    }

    fun sendMessage(receiverId: String, receiverName: String, text: String) {
        repo.sendMessage(receiverId, receiverName, text)
    }

    fun getMessages(receiverId: String, onResult: (List<Message>) -> Unit) {
        repo.getMessages(receiverId, onResult)
    }

    fun getChatList(onResult: (List<ChatRoom>) -> Unit) {
        repo.getChatList(onResult)
    }

    fun updateStatus(isOnline: Boolean) {
        repo.updateStatus(isOnline)
    }

    fun getUser(uid: String, onResult: (User) -> Unit) {
        repo.getUser(uid, onResult)
    }

    fun markMessagesSeen(receiverId: String) {
        repo.markMessagesSeen(receiverId)
    }

    fun logout() {
        repo.logout()
    }

    fun getCurrentUser(onResult: (User) -> Unit) {
        repo.getCurrentUser(onResult)
    }
}