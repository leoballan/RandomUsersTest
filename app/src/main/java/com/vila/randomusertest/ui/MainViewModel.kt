package com.vila.randomusertest.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vila.randomusertest.domain.models.*
import com.vila.randomusertest.domain.usecases.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getUsersUseCase: GetUsersUseCase)
    :ViewModel() {

    init {
        fetchUserWithStateFlow()
    }

    private val _usersStateFlow =
        MutableStateFlow<WebResponse<List<User>>>(WebResponse.Init())
    val usersStateFlow = _usersStateFlow.asSharedFlow()

    private val _selectedUser = MutableStateFlow(User())
    val selectedUser = _selectedUser.asStateFlow()

    private val _progressSharedFlow = MutableSharedFlow<Boolean>()
    val progressSharedFlow = _progressSharedFlow.asSharedFlow()

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList = _userList.asStateFlow()

    private var listTemp = mutableListOf<User>()

    fun setProgressState(state:Boolean){
        viewModelScope.launch {
            _progressSharedFlow.emit(state)
        }
    }

    fun setSelectedUser(user: User){
        _selectedUser.value = user
    }

    fun fetchUserWithStateFlow(){
        viewModelScope.launch {
            getUsersUseCase.invoke().collect {_usersStateFlow.value = it}
        }
    }

    fun resetUsersStateFlow(){
        _usersStateFlow.value = WebResponse.Init()
    }

    fun filterList(text: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val list = listTemp.filter { it.userName.contains(text) }
                _userList.value = list
            }
        }
    }

    fun resetFilter(){
        _userList.value = listTemp
    }

    fun setListTemp(list:List<User>){
        listTemp = list as MutableList<User>
        _userList.value = listTemp
    }

}