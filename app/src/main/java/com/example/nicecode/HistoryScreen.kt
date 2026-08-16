package com.example.nicecode

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.Modifier

@Composable
internal fun HistoryScreen(
    historyViewModel: HistoryViewModel,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    var expandedItem by remember { mutableStateOf<Int?>(null) }
    val historyPreviewDays = historyViewModel.historyPreviewDays.collectAsState().value

    AppSectionScreen(
        title = "\u8fd1\u671f\u9753\u7801",
        modifier = modifier
    ) {
        HistoryPageContent(
            days = historyPreviewDays,
            expandedItem = expandedItem,
            onToggleDay = { index ->
                expandedItem = expandedItem.toggleExclusiveExpanded(index)
            },
            onCopyRecord = { record ->
                clipboardManager.setText(AnnotatedString(record.copyText))
            }
        )
    }
}
