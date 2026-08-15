package com.example.nicecode

import java.time.LocalDate

internal data class HistoryPreviewRecord(
    val id: Long,
    val previewText: String,
    val copyText: String,
)

internal data class HistoryPreviewDay(
    val date: String,
    val records: List<HistoryPreviewRecord>,
)

internal fun buildHistoryPreviewDays(
    records: List<HistoryRecordEntity>,
    currentDate: LocalDate = LocalDate.now(),
): List<HistoryPreviewDay> {
    val groupedRecords = records.groupBy { it.date }

    return (0..4).map { index ->
        val date = currentDate.minusDays(index.toLong())
        val dateKey = date.toString()
        val previewRecords = groupedRecords[dateKey].orEmpty().take(3).map { record ->
            HistoryPreviewRecord(
                id = record.id,
                previewText = buildHistoryPreviewText(record.resultList),
                copyText = record.resultList.joinToString(",")
            )
        }

        HistoryPreviewDay(
            date = "${date.monthValue}\u6708${date.dayOfMonth}\u65e5",
            records = previewRecords
        )
    }
}

private fun buildHistoryPreviewText(resultList: List<String>): String {
    if (resultList.isEmpty()) {
        return "\u6682\u65e0\u8bb0\u5f55"
    }

    val preview = resultList.take(4).joinToString(",")
    return if (resultList.size > 4) "$preview..." else preview
}
