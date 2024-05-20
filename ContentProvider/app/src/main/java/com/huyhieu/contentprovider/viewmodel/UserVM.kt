package com.huyhieu.contentprovider.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huyhieu.contentprovider.App
import com.huyhieu.contentprovider.content_provider.UserContentProvider
import com.huyhieu.contentprovider.room.entity.User
import kotlinx.coroutines.launch

class UserVM: ViewModel() {

    val users = App.ins.db?.userDao()?.getAllUsers()
    val cp = UserContentProvider()

    fun insertUser(user: User) {
        viewModelScope.launch {
            App.ins.db?.userDao()?.insertUser(user)
        }
    }
    fun deleteUser(user: User?) {
        user ?: return
        viewModelScope.launch {
            App.ins.db?.userDao()?.deleteUser(user)
        }
    }
}