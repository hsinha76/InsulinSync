package com.hsdroid.insulinsync.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsdroid.insulinsync.data.repository.InsulinRepository
import com.hsdroid.insulinsync.models.Insulin
import com.hsdroid.insulinsync.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsulinViewModel @Inject constructor(private val repository: InsulinRepository) : ViewModel() {

    private val response: MutableStateFlow<ApiState<List<Insulin>>> =
        MutableStateFlow(ApiState.EMPTY)

    val _response: StateFlow<ApiState<List<Insulin>>> = response

    init {
        getAllData()
    }

    fun addData(insulin: Insulin) {
        repository.insertData(insulin)
    }

    fun deleteData(insulin: Insulin) {
        repository.deleteData(insulin)
    }

    private fun getAllData() = viewModelScope.launch {
        repository.getAllData()
            .onStart { response.value = ApiState.LOADING }
            .catch { response.value = ApiState.FAILURE(it) }
            .collect { response.value = ApiState.SUCCESS(it) }
    }
}