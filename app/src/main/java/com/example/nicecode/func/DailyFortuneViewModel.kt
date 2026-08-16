package com.example.nicecode

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class DailyFortuneViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _dailyFortune = MutableStateFlow<DailyFortune?>(null)
    val dailyFortune: StateFlow<DailyFortune?> = _dailyFortune.asStateFlow()

    init {
        loadDailyFortuneIfNeeded()
    }

    fun loadDailyFortuneIfNeeded() {
        if (_dailyFortune.value != null) {
            return
        }

        viewModelScope.launch {
            _dailyFortune.value = loadTodayFortune(getApplication())
        }
    }
}
