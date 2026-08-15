package com.example.nicecode

import androidx.compose.runtime.Composable

@Composable
internal fun HistoryPageContent(
    days: List<HistoryPreviewDay>,
    expandedItem: Int?,
    onToggleDay: (Int) -> Unit,
    onCopyRecord: (HistoryPreviewRecord) -> Unit,
) {
    HistoryScreenContent(
        days = days,
        expandedItem = expandedItem,
        onToggleDay = onToggleDay,
        onCopyRecord = onCopyRecord
    )
}
