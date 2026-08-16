package com.example.nicecode

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

internal class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val historyRepository = HistoryRepository(application)
    private val currentDate = MutableStateFlow(LocalDate.now())

    val historyPreviewDays: StateFlow<List<HistoryPreviewDay>> =
        currentDate
            .flatMapLatest { date ->
                historyRepository.observeRecentRecords(date).map { records ->
                    buildHistoryPreviewDays(
                        records = records,
                        currentDate = date
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = buildHistoryPreviewDays(
                    records = emptyList<HistoryRecordEntity>(),
                    currentDate = currentDate.value
                )
            )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val today = LocalDate.now()
            currentDate.value = today
            historyRepository.cleanupExpiredRecords(currentDate = today)
        }
    }

    fun recordHistory(
        results: List<String>,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val today = LocalDate.now()
            currentDate.value = today
            historyRepository.recordHistory(
                results = results,
                currentDate = today
            )
        }
    }
}
