package com.example.nicecode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun HistoryScreenContent(
    days: List<HistoryPreviewDay>,
    expandedItem: Int?,
    onToggleDay: (Int) -> Unit,
    onCopyRecord: (HistoryPreviewRecord) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        days.forEachIndexed { index, day ->
            HistoryDayCard(
                day = day,
                expanded = expandedItem == index,
                onToggle = { onToggleDay(index) },
                onCopyRecord = onCopyRecord
            )
        }
    }
}

@Composable
private fun HistoryDayCard(
    day: HistoryPreviewDay,
    expanded: Boolean,
    onToggle: () -> Unit,
    onCopyRecord: (HistoryPreviewRecord) -> Unit,
) {
    ExpandableSectionCard(
        title = day.date,
        expanded = expanded,
        onToggle = onToggle,
        contentPadding = PaddingValues(0.dp),
        contentSpacing = 0.dp
    ) {
        if (day.records.isEmpty()) {
            HistoryResultRow(
                resultPreview = "\u6682\u65e0\u8bb0\u5f55",
                onCopyClick = null
            )
        } else {
            day.records.forEach { record ->
                HistoryResultRow(
                    resultPreview = record.previewText,
                    onCopyClick = { onCopyRecord(record) }
                )
            }
        }
    }
}

@Composable
internal fun HistoryResultRow(
    resultPreview: String,
    onCopyClick: (() -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 12.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = Color.White.copy(alpha = 0.72f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = resultPreview,
                color = FortuneTextPrimary,
                fontSize = 18.sp,
                lineHeight = 24.sp
            )
        }
        if (onCopyClick != null) {
            TextButton(onClick = onCopyClick) {
                Text(
                    text = "\u590d\u5236",
                    color = FortuneHeaderRed,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
