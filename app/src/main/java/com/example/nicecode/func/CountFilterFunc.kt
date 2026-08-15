package com.example.nicecode

private const val countNumberLength = 4

internal fun applyCountSecondPageFilters(
    baseResults: List<String>,
    positionDigitSets: List<String>,
    positionDigitKeepFlags: List<Boolean>,
    undeterminedCount: String,
    undeterminedSumDigit: String,
    undeterminedKeep: Boolean,
    specifiedPositions: List<Boolean>,
    specifiedSumDigit: String,
    specifiedKeep: Boolean,
    largeCount: String,
    largeCountKeep: Boolean,
    primeCount: String,
    primeCountKeep: Boolean,
    oddCount: String,
    oddCountKeep: Boolean,
    repeatPositions: List<Boolean>,
    repeatKeep: Boolean,
    confirmFixedCount: String,
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
    applySpecifiedSumFilters(
        filteredResults = filteredResults,
        specifiedPositions = specifiedPositions,
        specifiedSumDigit = specifiedSumDigit,
        specifiedKeep = specifiedKeep
    )
    applyPropertyFilters(
        filteredResults = filteredResults,
        largeCount = largeCount,
        largeCountKeep = largeCountKeep,
        primeCount = primeCount,
        primeCountKeep = primeCountKeep,
        oddCount = oddCount,
        oddCountKeep = oddCountKeep
    )
    applyRepeatValueFilters(
        filteredResults = filteredResults,
        repeatPositions = repeatPositions,
        repeatKeep = repeatKeep
    )
    val confirmedResults = applyConfirmFixedCount(
        filteredResults = filteredResults,
        confirmFixedCount = confirmFixedCount
    )

    val numericResults = filteredResults.toList().sorted()
    val confirmedResultList = confirmedResults.toList().sorted()
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

private fun applySpecifiedSumFilters(
    filteredResults: MutableSet<String>,
    specifiedPositions: List<Boolean>,
    specifiedSumDigit: String,
    specifiedKeep: Boolean,
) {
    val selectedIndexes = specifiedPositions.mapIndexedNotNull { index, isSelected ->
        index.takeIf { isSelected && index < countNumberLength }
    }
    if (selectedIndexes.isEmpty() || specifiedSumDigit.isEmpty()) {
        return
    }

    val targetDigits = parseTargetDigitSet(specifiedSumDigit)
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
            keepMatchedResults = specifiedKeep
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
    largeCount: String,
    largeCountKeep: Boolean,
    primeCount: String,
    primeCountKeep: Boolean,
    oddCount: String,
    oddCountKeep: Boolean,
) {
    applyPropertyCountFilter(
        filteredResults = filteredResults,
        expectedCountText = largeCount,
        keepMatchedResults = largeCountKeep
    ) { digit ->
        digit >= 5
    }
    applyPropertyCountFilter(
        filteredResults = filteredResults,
        expectedCountText = primeCount,
        keepMatchedResults = primeCountKeep
    ) { digit ->
        digit == 1 || digit == 2 || digit == 3 || digit == 5 || digit == 7
    }
    applyPropertyCountFilter(
        filteredResults = filteredResults,
        expectedCountText = oddCount,
        keepMatchedResults = oddCountKeep
    ) { digit ->
        digit % 2 != 0
    }
}

private fun applyPropertyCountFilter(
    filteredResults: MutableSet<String>,
    expectedCountText: String,
    keepMatchedResults: Boolean,
    predicate: (Int) -> Boolean,
) {
    if (expectedCountText.isEmpty()) {
        return
    }

    val expectedCount = expectedCountText.toIntOrNull() ?: return
    if (expectedCount !in 0..countNumberLength) {
        return
    }

    filteredResults.removeAll { result ->
        val digits = result.mapNotNull { char -> char.digitToIntOrNull() }
        if (digits.size != countNumberLength) {
            return@removeAll true
        }

        val matchedCount = digits.count(predicate)
        val isMatched = matchedCount == expectedCount
        shouldDeleteResult(
            isMatched = isMatched,
            keepMatchedResults = keepMatchedResults
        )
    }
}

private fun applyRepeatValueFilters(
    filteredResults: MutableSet<String>,
    repeatPositions: List<Boolean>,
    repeatKeep: Boolean,
) {
    val selectedIndexes = repeatPositions.mapIndexedNotNull { index, isSelected ->
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
            keepMatchedResults = repeatKeep
        )
    }
}

private fun applyConfirmFixedCount(
    filteredResults: MutableSet<String>,
    confirmFixedCount: String,
): Set<String> {
    if (confirmFixedCount.isEmpty()) {
        return emptySet()
    }

    val fixedCount = confirmFixedCount.toIntOrNull() ?: return emptySet()
    if (fixedCount !in 0..3) {
        return emptySet()
    }

    val replaceCount = countNumberLength - fixedCount
    val expandedResults = linkedSetOf<String>()

    filteredResults.forEach { result ->
        if (result.length != countNumberLength) {
            return@forEach
        }

        appendConfirmedPatterns(
            source = result,
            replaceCount = replaceCount,
            startIndex = 0,
            selectedIndexes = mutableListOf(),
            resultCollector = expandedResults
        )
    }

    return expandedResults
}

private fun appendConfirmedPatterns(
    source: String,
    replaceCount: Int,
    startIndex: Int,
    selectedIndexes: MutableList<Int>,
    resultCollector: MutableSet<String>,
) {
    if (selectedIndexes.size == replaceCount) {
        val chars = source.toCharArray()
        selectedIndexes.forEach { index ->
            chars[index] = 'X'
        }
        resultCollector += chars.concatToString()
        return
    }

    for (index in startIndex until countNumberLength) {
        selectedIndexes += index
        appendConfirmedPatterns(
            source = source,
            replaceCount = replaceCount,
            startIndex = index + 1,
            selectedIndexes = selectedIndexes,
            resultCollector = resultCollector
        )
        selectedIndexes.removeAt(selectedIndexes.lastIndex)
    }
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
