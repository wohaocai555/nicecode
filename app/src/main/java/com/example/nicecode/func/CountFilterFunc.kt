package com.example.nicecode

private const val countNumberLength = 4

internal data class SpecifiedSumFilterCondition(
    val positions: List<Boolean> = List(countNumberLength) { false },
    val sumDigits: String = "",
    val keep: Boolean = false,
)

internal data class RepeatValueFilterCondition(
    val positions: List<Boolean> = List(countNumberLength) { false },
    val keep: Boolean = false,
)

internal fun applyCountSecondPageFilters(
    baseResults: List<String>,
    positionDigitSets: List<String>,
    positionDigitKeepFlags: List<Boolean>,
    undeterminedCount: String,
    undeterminedSumDigit: String,
    undeterminedKeep: Boolean,
    specifiedSumConditions: List<SpecifiedSumFilterCondition>,
    largePositions: List<Boolean>,
    largeKeep: Boolean,
    primePositions: List<Boolean>,
    primeKeep: Boolean,
    oddPositions: List<Boolean>,
    oddKeep: Boolean,
    repeatConditions: List<RepeatValueFilterCondition>,
    confirmFixedPositions: List<Boolean>,
    confirmFixedSeparateDisplay: Boolean,
): List<String> {
    if (baseResults.isEmpty()) {
        return emptyList()
    }

    val filteredResults = baseResults.toMutableSet()

    applyPositionDigitFilters(
        filteredResults = filteredResults,
        positionDigitSets = positionDigitSets,
        positionDigitKeepFlags = positionDigitKeepFlags
    )
    applyUndeterminedFilters(
        filteredResults = filteredResults,
        undeterminedCount = undeterminedCount,
        undeterminedSumDigit = undeterminedSumDigit,
        undeterminedKeep = undeterminedKeep
    )
    specifiedSumConditions.forEach { condition ->
        applySpecifiedSumFilter(
            filteredResults = filteredResults,
            condition = condition
        )
    }
    applyPropertyFilters(
        filteredResults = filteredResults,
        largePositions = largePositions,
        largeKeep = largeKeep,
        primePositions = primePositions,
        primeKeep = primeKeep,
        oddPositions = oddPositions,
        oddKeep = oddKeep
    )
    repeatConditions.forEach { condition ->
        applyRepeatValueFilter(
            filteredResults = filteredResults,
            condition = condition
        )
    }
    val confirmedResults = applyConfirmFixedPositions(
        filteredResults = filteredResults,
        selectedPositions = confirmFixedPositions
    )

    if (confirmedResults.isEmpty()) {
        return filteredResults.toList().sorted()
    }

    if (confirmFixedSeparateDisplay) {
        return confirmedResults.toList().sorted()
    }

    filteredResults.addAll(confirmedResults)
    val numericResults = filteredResults.filterNot { result -> 'X' in result }.sorted()
    val confirmedResultList = filteredResults.filter { result -> 'X' in result }.sorted()
    return numericResults + confirmedResultList
}

private fun applyPositionDigitFilters(
    filteredResults: MutableSet<String>,
    positionDigitSets: List<String>,
    positionDigitKeepFlags: List<Boolean>,
) {
    positionDigitSets.forEachIndexed { index, digitSetText ->
        if (index >= countNumberLength || digitSetText.isEmpty()) {
            return@forEachIndexed
        }

        val digitSet = digitSetText.toSet()
        val keepMatchedResults = positionDigitKeepFlags.getOrElse(index) { false }

        filteredResults.removeAll { result ->
            val positionDigit = result.getOrNull(index) ?: return@removeAll true
            val isMatched = positionDigit in digitSet
            shouldDeleteResult(
                isMatched = isMatched,
                keepMatchedResults = keepMatchedResults
            )
        }
    }
}

private fun applyUndeterminedFilters(
    filteredResults: MutableSet<String>,
    undeterminedCount: String,
    undeterminedSumDigit: String,
    undeterminedKeep: Boolean,
) {
    if (undeterminedCount.isEmpty() || undeterminedSumDigit.isEmpty()) {
        return
    }

    val selectedCount = undeterminedCount.toIntOrNull() ?: return
    val targetDigits = parseTargetDigitSet(undeterminedSumDigit)
    if (selectedCount !in 1..countNumberLength || targetDigits.isEmpty()) {
        return
    }

    filteredResults.removeAll { result ->
        val isMatched = matchesUndeterminedFilter(
            result = result,
            selectedCount = selectedCount,
            targetDigits = targetDigits
        )
        shouldDeleteResult(
            isMatched = isMatched,
            keepMatchedResults = undeterminedKeep
        )
    }
}

private fun matchesUndeterminedFilter(
    result: String,
    selectedCount: Int,
    targetDigits: Set<Int>,
): Boolean {
    val digits = result.mapNotNull { char -> char.digitToIntOrNull() }
    if (digits.size != countNumberLength) {
        return false
    }

    if (selectedCount == 1) {
        return digits.any { digit -> digit in targetDigits }
    }

    fun dfs(
        startIndex: Int,
        currentCount: Int,
        currentSum: Int,
    ): Boolean {
        if (currentCount == selectedCount) {
            return currentSum % 10 in targetDigits
        }

        for (index in startIndex until countNumberLength) {
            if (dfs(index + 1, currentCount + 1, currentSum + digits[index])) {
                return true
            }
        }

        return false
    }

    return dfs(
        startIndex = 0,
        currentCount = 0,
        currentSum = 0
    )
}

private fun applySpecifiedSumFilter(
    filteredResults: MutableSet<String>,
    condition: SpecifiedSumFilterCondition,
) {
    val selectedIndexes = condition.positions.mapIndexedNotNull { index, isSelected ->
        index.takeIf { isSelected && index < countNumberLength }
    }
    if (selectedIndexes.isEmpty() || condition.sumDigits.isEmpty()) {
        return
    }

    val targetDigits = parseTargetDigitSet(condition.sumDigits)
    if (targetDigits.isEmpty()) {
        return
    }

    filteredResults.removeAll { result ->
        val isMatched = matchesSpecifiedSumFilter(
            result = result,
            selectedIndexes = selectedIndexes,
            targetDigits = targetDigits
        )
        shouldDeleteResult(
            isMatched = isMatched,
            keepMatchedResults = condition.keep
        )
    }
}

private fun matchesSpecifiedSumFilter(
    result: String,
    selectedIndexes: List<Int>,
    targetDigits: Set<Int>,
): Boolean {
    val digits = result.mapNotNull { char -> char.digitToIntOrNull() }
    if (digits.size != countNumberLength) {
        return false
    }

    if (selectedIndexes.size == 1) {
        return digits[selectedIndexes.first()] in targetDigits
    }

    val digitSum = selectedIndexes.sumOf { index -> digits[index] }
    return digitSum % 10 in targetDigits
}

private fun applyPropertyFilters(
    filteredResults: MutableSet<String>,
    largePositions: List<Boolean>,
    largeKeep: Boolean,
    primePositions: List<Boolean>,
    primeKeep: Boolean,
    oddPositions: List<Boolean>,
    oddKeep: Boolean,
) {
    applyPropertyPositionFilter(
        filteredResults = filteredResults,
        selectedPositions = largePositions,
        keepMatchedResults = largeKeep
    ) { digit ->
        digit >= 5
    }
    applyPropertyPositionFilter(
        filteredResults = filteredResults,
        selectedPositions = primePositions,
        keepMatchedResults = primeKeep
    ) { digit ->
        digit == 1 || digit == 2 || digit == 3 || digit == 5 || digit == 7
    }
    applyPropertyPositionFilter(
        filteredResults = filteredResults,
        selectedPositions = oddPositions,
        keepMatchedResults = oddKeep
    ) { digit ->
        digit % 2 != 0
    }
}

private fun applyPropertyPositionFilter(
    filteredResults: MutableSet<String>,
    selectedPositions: List<Boolean>,
    keepMatchedResults: Boolean,
    predicate: (Int) -> Boolean,
) {
    val selectedIndexes = selectedPositions.mapIndexedNotNull { index, isSelected ->
        index.takeIf { isSelected && index < countNumberLength }
    }
    if (selectedIndexes.isEmpty()) {
        return
    }

    val selectedIndexSet = selectedIndexes.toSet()

    filteredResults.removeAll { result ->
        val digits = result.mapNotNull { char -> char.digitToIntOrNull() }
        if (digits.size != countNumberLength) {
            return@removeAll true
        }

        val isMatched = digits.indices.all { index ->
            val hasProperty = predicate(digits[index])
            if (index in selectedIndexSet) hasProperty else !hasProperty
        }
        shouldDeleteResult(
            isMatched = isMatched,
            keepMatchedResults = keepMatchedResults
        )
    }
}

private fun applyRepeatValueFilter(
    filteredResults: MutableSet<String>,
    condition: RepeatValueFilterCondition,
) {
    val selectedIndexes = condition.positions.mapIndexedNotNull { index, isSelected ->
        index.takeIf { isSelected && index < countNumberLength }
    }
    if (selectedIndexes.size < 2) {
        return
    }

    filteredResults.removeAll { result ->
        val selectedDigits = selectedIndexes.mapNotNull { index ->
            result.getOrNull(index)
        }
        if (selectedDigits.size != selectedIndexes.size) {
            return@removeAll true
        }

        val isMatched = selectedDigits.toSet().size < selectedDigits.size
        shouldDeleteResult(
            isMatched = isMatched,
            keepMatchedResults = condition.keep
        )
    }
}

private fun applyConfirmFixedPositions(
    filteredResults: MutableSet<String>,
    selectedPositions: List<Boolean>,
): Set<String> {
    val selectedIndexes = selectedPositions.mapIndexedNotNull { index, isSelected ->
        index.takeIf { isSelected && index < countNumberLength }
    }
    if (selectedIndexes.isEmpty()) {
        return emptySet()
    }

    val expandedResults = linkedSetOf<String>()

    filteredResults.forEach { result ->
        if (result.length != countNumberLength) {
            return@forEach
        }

        appendConfirmedPatterns(
            source = result,
            selectedIndexes = selectedIndexes,
            resultCollector = expandedResults
        )
    }

    return expandedResults
}

private fun appendConfirmedPatterns(
    source: String,
    selectedIndexes: List<Int>,
    resultCollector: MutableSet<String>,
) {
    if (source.length != countNumberLength) {
        return
    }

    val chars = source.toCharArray()
    selectedIndexes.forEach { index ->
        chars[index] = 'X'
    }
    resultCollector += chars.concatToString()
}

private fun shouldDeleteResult(
    isMatched: Boolean,
    keepMatchedResults: Boolean,
): Boolean {
    return if (keepMatchedResults) {
        !isMatched
    } else {
        isMatched
    }
}

private fun parseTargetDigitSet(value: String): Set<Int> {
    return value.mapNotNull { char ->
        char.digitToIntOrNull()?.takeIf { digit -> digit in 0..9 }
    }.toSet()
}
