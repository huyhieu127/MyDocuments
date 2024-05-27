package com.huyhieu.mydocuments.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huyhieu.domain.enitities.User
import com.huyhieu.domain.interactor.GetUserByIdUseCase
import com.huyhieu.domain.interactor.InsertUserUseCase
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class TesVM @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val insertUserUseCase: InsertUserUseCase
) : ViewModel() {


    var pushNotify: MutableLiveData<String> = MutableLiveData("")
    fun pushMessage(msg: String) {
        pushNotify.postValue(msg)
    }

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun getUser(userId: Long) {
        viewModelScope.launch {
            _user.value = getUserByIdUseCase(userId)
        }
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            val id = insertUserUseCase(user)
            if (id != null) {
                getUser(id)
            }
        }
    }
}