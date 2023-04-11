package com.huyhieu.mydocuments.ui.fragments.navigation.home.components

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonArray
import com.huyhieu.library.utils.logDebug
import com.huyhieu.mydocuments.base.BaseVM
import com.huyhieu.mydocuments.models.MenuForm
import com.huyhieu.mydocuments.models.github.CommitForm
import com.huyhieu.mydocuments.utils.readAssets
import com.huyhieu.mydocuments.utils.toJson
import com.huyhieu.mydocuments.utils.toMutableListData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

class HomeVM @Inject constructor() : BaseVM() {
    val menuLiveData = MutableLiveData<MutableList<MenuForm>?>()

    fun loadMenuFromAsset() {
        val menuJson = readAssets("menu.json")
        val lstMenu = menuJson.toMutableListData<MenuForm>() ?: mutableListOf()
        menuLiveData.postValue(lstMenu)
    }

    val githubCommittedLiveData = MutableLiveData<List<CommitForm>>()
    fun fetchGitHubCommitted() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = gitHubAPIService.getCommit()
                logDebug(response.body().toJson())
                githubCommittedLiveData.postValue(response.body())
            } catch (ex: Exception) {
                logDebug(ex.message)
            }
        }
    }
}
