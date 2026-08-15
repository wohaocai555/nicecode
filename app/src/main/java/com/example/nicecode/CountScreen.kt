package com.example.nicecode

import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private enum class CountPage {
    FIRST,
    SECOND,
    THIRD,
}

private sealed interface CountEditTarget {
    data class PageOneGroup(val index: Int) : CountEditTarget
    data object MainGroupParticipationCount : CountEditTarget
    data class PositionDigitSet(val index: Int) : CountEditTarget
    data object UndeterminedCount : CountEditTarget
    data object UndeterminedSumDigit : CountEditTarget
    data object SpecifiedSumDigit : CountEditTarget
    data object LargeCount : CountEditTarget
    data object PrimeCount : CountEditTarget
    data object OddCount : CountEditTarget
    data object ConfirmFixedCount : CountEditTarget
}

@Composable
internal fun CountScreen(
    historyViewModel: HistoryViewModel,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    var currentPage by remember { mutableStateOf(CountPage.FIRST) }
    var pageOneGroupValues by remember { mutableStateOf(List(4) { "" }) }
    var mainGroupIndex by remember { mutableStateOf(0) }
    var mainGroupParticipationCount by remember { mutableStateOf("") }
    var basePermutationResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var displayedResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var positionDigitSets by remember { mutableStateOf(List(4) { "" }) }
    var positionDigitKeepFlags by remember { mutableStateOf(List(4) { false }) }
    var undeterminedCount by remember { mutableStateOf("") }
    var undeterminedSumDigit by remember { mutableStateOf("") }
    var undeterminedKeep by remember { mutableStateOf(false) }
    var specifiedPositions by remember { mutableStateOf(List(4) { false }) }
    var specifiedSumDigit by remember { mutableStateOf("") }
    var specifiedKeep by remember { mutableStateOf(false) }
    var largeCount by remember { mutableStateOf("") }
    var largeCountKeep by remember { mutableStateOf(false) }
    var primeCount by remember { mutableStateOf("") }
    var primeCountKeep by remember { mutableStateOf(false) }
    var oddCount by remember { mutableStateOf("") }
    var oddCountKeep by remember { mutableStateOf(false) }
    var repeatPositions by remember { mutableStateOf(List(4) { false }) }
    var repeatKeep by remember { mutableStateOf(false) }
    var confirmFixedCount by remember { mutableStateOf("") }
    var expandedSection by remember { mutableStateOf<Int?>(null) }
    var editingTarget by remember { mutableStateOf<CountEditTarget?>(null) }
    var keyboardWarning by remember { mutableStateOf<String?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var startErrorMessage by remember { mutableStateOf<String?>(null) }

    AppSectionScreen(
        title = "\u6311\u9009\u9753\u7801",
        modifier = modifier
    ) {
        when (currentPage) {
            CountPage.FIRST -> {
                CountFirstPage(
                    values = pageOneGroupValues,
                    mainGroupIndex = mainGroupIndex,
                    mainGroupParticipationCount = mainGroupParticipationCount,
                    onInputClick = { index ->
                        keyboardWarning = null
                        editingTarget = CountEditTarget.PageOneGroup(index)
                    },
                    onMainGroupParticipationClick = {
                        keyboardWarning = null
                        editingTarget = CountEditTarget.MainGroupParticipationCount
                    },
                    onMainGroupSelect = { index ->
                        mainGroupIndex = index
                    },
                    onStartClick = {
                        startErrorMessage = validateCountPermutationInputs(
                            groupValues = pageOneGroupValues,
                            mainGroupIndex = mainGroupIndex,
                            mainGroupParticipationCountText = mainGroupParticipationCount
                        )
                        if (startErrorMessage == null) {
                            showConfirmDialog = true
                        }
                    }
                )
            }

            CountPage.SECOND -> {
                CountSecondPage(
                    positionDigitSets = positionDigitSets,
                    positionDigitKeepFlags = positionDigitKeepFlags,
                    undeterminedCount = undeterminedCount,
                    undeterminedSumDigit = undeterminedSumDigit,
                    undeterminedKeep = undeterminedKeep,
                    specifiedPositions = specifiedPositions,
                    specifiedSumDigit = specifiedSumDigit,
                    specifiedKeep = specifiedKeep,
                    largeCount = largeCount,
                    largeCountKeep = largeCountKeep,
                    primeCount = primeCount,
                    primeCountKeep = primeCountKeep,
                    oddCount = oddCount,
                    oddCountKeep = oddCountKeep,
                    repeatPositions = repeatPositions,
                    repeatKeep = repeatKeep,
                    confirmFixedCount = confirmFixedCount,
                    expandedSection = expandedSection,
                    onBackClick = { currentPage = CountPage.FIRST },
                    onToggleSection = { index ->
                        expandedSection = expandedSection.toggleExclusiveExpanded(index)
                    },
                    onPositionDigitClick = { index ->
                        keyboardWarning = null
                        editingTarget = CountEditTarget.PositionDigitSet(index)
                    },
                    onPositionKeepChange = { index, checked ->
                        positionDigitKeepFlags =
                            positionDigitKeepFlags.toMutableList().also { values ->
                                values[index] = checked
                            }
                    },
                    onUndeterminedCountClick = {
                        keyboardWarning = null
                        editingTarget = CountEditTarget.UndeterminedCount
                    },
                    onUndeterminedSumClick = {
                        keyboardWarning = null
                        editingTarget = CountEditTarget.UndeterminedSumDigit
                    },
                    onUndeterminedKeepChange = { undeterminedKeep = it },
                    onSpecifiedPositionChange = { index, checked ->
                        specifiedPositions =
                            specifiedPositions.toMutableList().also { values ->
                                values[index] = checked
                            }
                    },
                    onSpecifiedSumClick = {
                        keyboardWarning = null
                        editingTarget = CountEditTarget.SpecifiedSumDigit
                    },
                    onSpecifiedKeepChange = { specifiedKeep = it },
                    onLargeCountClick = {
                        keyboardWarning = null
                        editingTarget = CountEditTarget.LargeCount
                    },
                    onLargeCountKeepChange = { largeCountKeep = it },
                    onPrimeCountClick = {
                        keyboardWarning = null
                        editingTarget = CountEditTarget.PrimeCount
                    },
                    onPrimeCountKeepChange = { primeCountKeep = it },
                    onOddCountClick = {
                        keyboardWarning = null
                        editingTarget = CountEditTarget.OddCount
                    },
                    onOddCountKeepChange = { oddCountKeep = it },
                    onRepeatPositionChange = { index, checked ->
                        repeatPositions =
                            repeatPositions.toMutableList().also { values ->
                                values[index] = checked
                            }
                    },
                    onRepeatKeepChange = { repeatKeep = it },
                    onConfirmFixedCountClick = {
                        keyboardWarning = null
                        editingTarget = CountEditTarget.ConfirmFixedCount
                    },
                    onStartFilterClick = {
                        val resultsToDisplay = applyCountSecondPageFilters(
                            baseResults = basePermutationResults,
                            positionDigitSets = positionDigitSets,
                            positionDigitKeepFlags = positionDigitKeepFlags,
                            undeterminedCount = undeterminedCount,
                            undeterminedSumDigit = undeterminedSumDigit,
                            undeterminedKeep = undeterminedKeep,
                            specifiedPositions = specifiedPositions,
                            specifiedSumDigit = specifiedSumDigit,
                            specifiedKeep = specifiedKeep,
                            largeCount = largeCount,
                            largeCountKeep = largeCountKeep,
                            primeCount = primeCount,
                            primeCountKeep = primeCountKeep,
                            oddCount = oddCount,
                            oddCountKeep = oddCountKeep,
                            repeatPositions = repeatPositions,
                            repeatKeep = repeatKeep,
                            confirmFixedCount = confirmFixedCount
                        )
                        displayedResults = resultsToDisplay
                        historyViewModel.recordHistory(results = resultsToDisplay)
                        currentPage = CountPage.THIRD
                    }
                )
            }

            CountPage.THIRD -> {
                CountThirdPage(
                    results = displayedResults,
                    onCopyClick = {
                        clipboardManager.setText(
                            AnnotatedString(displayedResults.joinToString(","))
                        )
                    },
                    onBackToFilterClick = { currentPage = CountPage.SECOND },
                    onCompleteClick = {
                        currentPage = CountPage.FIRST
                        pageOneGroupValues = List(4) { "" }
                        mainGroupIndex = 0
                        mainGroupParticipationCount = ""
                        basePermutationResults = emptyList()
                        displayedResults = emptyList()
                        positionDigitSets = List(4) { "" }
                        positionDigitKeepFlags = List(4) { false }
                        undeterminedCount = ""
                        undeterminedSumDigit = ""
                        undeterminedKeep = false
                        specifiedPositions = List(4) { false }
                        specifiedSumDigit = ""
                        specifiedKeep = false
                        largeCount = ""
                        largeCountKeep = false
                        primeCount = ""
                        primeCountKeep = false
                        oddCount = ""
                        oddCountKeep = false
                        repeatPositions = List(4) { false }
                        repeatKeep = false
                        confirmFixedCount = ""
                        expandedSection = null
                        keyboardWarning = null
                        editingTarget = null
                    }
                )
            }
        }
    }

    if (startErrorMessage != null) {
        AlertDialog(
            onDismissRequest = { startErrorMessage = null },
            title = {
                Text(
                    text = "\u65e0\u6cd5\u6392\u5217",
                    color = FortuneTextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = startErrorMessage.orEmpty(),
                    color = FortuneTextPrimary,
                    fontSize = 18.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { startErrorMessage = null }) {
                    Text(
                        text = "\u77e5\u9053\u4e86",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = FortuneCardSurface
        )
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "\u662f\u5426\u7b5b\u9009\uff1f",
                    color = FortuneTextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        when (
                            val result = generateCountPermutationResults(
                                groupValues = pageOneGroupValues,
                                mainGroupIndex = mainGroupIndex,
                                mainGroupParticipationCountText = mainGroupParticipationCount
                            )
                        ) {
                            is CountPermutationResult.Error -> {
                                showConfirmDialog = false
                                startErrorMessage = result.message
                            }

                            is CountPermutationResult.Success -> {
                                basePermutationResults = result.results
                                showConfirmDialog = false
                                currentPage = CountPage.SECOND
                            }
                        }
                    }
                ) {
                    Text(
                        text = "\u662f",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        when (
                            val result = generateCountPermutationResults(
                                groupValues = pageOneGroupValues,
                                mainGroupIndex = mainGroupIndex,
                                mainGroupParticipationCountText = mainGroupParticipationCount
                            )
                        ) {
                            is CountPermutationResult.Error -> {
                                showConfirmDialog = false
                                startErrorMessage = result.message
                            }

                            is CountPermutationResult.Success -> {
                                basePermutationResults = result.results
                                displayedResults = result.results
                                historyViewModel.recordHistory(results = result.results)
                                showConfirmDialog = false
                                currentPage = CountPage.THIRD
                            }
                        }
                    }
                ) {
                    Text(
                        text = "\u5426",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = FortuneCardSurface
        )
    }

    editingTarget?.let { target ->
        NumberKeyboardDialog(
            value = currentValueForTarget(
                target = target,
                pageOneGroupValues = pageOneGroupValues,
                mainGroupParticipationCount = mainGroupParticipationCount,
                positionDigitSets = positionDigitSets,
                undeterminedCount = undeterminedCount,
                undeterminedSumDigit = undeterminedSumDigit,
                specifiedSumDigit = specifiedSumDigit,
                largeCount = largeCount,
                primeCount = primeCount,
                oddCount = oddCount,
                confirmFixedCount = confirmFixedCount
            ),
            warningMessage = keyboardWarning,
            onDismiss = {
                keyboardWarning = null
                editingTarget = null
            },
            onDigitClick = { digit ->
                when (target) {
                    is CountEditTarget.PageOneGroup -> {
                        val currentValue = pageOneGroupValues[target.index]
                        if (currentValue.contains(digit)) {
                            keyboardWarning =
                                "\u91cd\u590d\u63d0\u9192\uff1a\u8be5\u7ec4\u5df2\u8f93\u5165\u6570\u5b57 $digit"
                        } else {
                            keyboardWarning = null
                            pageOneGroupValues =
                                pageOneGroupValues.toMutableList().also { values ->
                                    values[target.index] = values[target.index] + digit
                                }
                        }
                    }

                    CountEditTarget.MainGroupParticipationCount -> {
                        if (isMainGroupParticipationInvalid(mainGroupParticipationCount, digit)) {
                            keyboardWarning = "\u4e3b\u8981\u7ec4\u5408\u81f3\u591a\u53c2\u52a03\u4f4d"
                        } else {
                            keyboardWarning = null
                            mainGroupParticipationCount = digit
                        }
                    }

                    is CountEditTarget.PositionDigitSet -> {
                        val currentValue = positionDigitSets[target.index]
                        if (currentValue.contains(digit)) {
                            keyboardWarning =
                                "\u91cd\u590d\u63d0\u9192\uff1a\u8be5\u96c6\u5408\u5df2\u8f93\u5165\u6570\u5b57 $digit"
                        } else {
                            keyboardWarning = null
                            positionDigitSets =
                                positionDigitSets.toMutableList().also { values ->
                                    values[target.index] = values[target.index] + digit
                                }
                        }
                    }

                    CountEditTarget.UndeterminedCount -> {
                        if (isLimitedCountInputInvalid(undeterminedCount, digit)) {
                            keyboardWarning = "\u4e2a\u6570\u8d85\u8fc74"
                        } else {
                            keyboardWarning = null
                            undeterminedCount = digit
                        }
                    }

                    CountEditTarget.UndeterminedSumDigit -> {
                        if (undeterminedSumDigit.contains(digit)) {
                            keyboardWarning =
                                "\u91cd\u590d\u63d0\u9192\uff1a\u5df2\u8f93\u5165\u6570\u5b57 $digit"
                        } else {
                            keyboardWarning = null
                            undeterminedSumDigit += digit
                        }
                    }

                    CountEditTarget.SpecifiedSumDigit -> {
                        if (specifiedSumDigit.contains(digit)) {
                            keyboardWarning =
                                "\u91cd\u590d\u63d0\u9192\uff1a\u5df2\u8f93\u5165\u6570\u5b57 $digit"
                        } else {
                            keyboardWarning = null
                            specifiedSumDigit += digit
                        }
                    }

                    CountEditTarget.LargeCount -> {
                        if (isLimitedCountInputInvalid(largeCount, digit)) {
                            keyboardWarning = "\u4e2a\u6570\u8d85\u8fc74"
                        } else {
                            keyboardWarning = null
                            largeCount = digit
                        }
                    }

                    CountEditTarget.PrimeCount -> {
                        if (isLimitedCountInputInvalid(primeCount, digit)) {
                            keyboardWarning = "\u4e2a\u6570\u8d85\u8fc74"
                        } else {
                            keyboardWarning = null
                            primeCount = digit
                        }
                    }

                    CountEditTarget.OddCount -> {
                        if (isLimitedCountInputInvalid(oddCount, digit)) {
                            keyboardWarning = "\u4e2a\u6570\u8d85\u8fc74"
                        } else {
                            keyboardWarning = null
                            oddCount = digit
                        }
                    }

                    CountEditTarget.ConfirmFixedCount -> {
                        if (isLimitedCountInputInvalid(confirmFixedCount, digit)) {
                            keyboardWarning = "\u4e2a\u6570\u8d85\u8fc74"
                        } else {
                            keyboardWarning = null
                            confirmFixedCount = digit
                        }
                    }
                }
            },
            onDeleteClick = {
                keyboardWarning = null
                when (target) {
                    is CountEditTarget.PageOneGroup -> {
                        pageOneGroupValues =
                            pageOneGroupValues.toMutableList().also { values ->
                                values[target.index] = values[target.index].dropLast(1)
                            }
                    }

                    CountEditTarget.MainGroupParticipationCount -> {
                        mainGroupParticipationCount = mainGroupParticipationCount.dropLast(1)
                    }

                    is CountEditTarget.PositionDigitSet -> {
                        positionDigitSets =
                            positionDigitSets.toMutableList().also { values ->
                                values[target.index] = values[target.index].dropLast(1)
                            }
                    }

                    CountEditTarget.UndeterminedCount -> {
                        undeterminedCount = undeterminedCount.dropLast(1)
                    }

                    CountEditTarget.UndeterminedSumDigit -> {
                        undeterminedSumDigit = undeterminedSumDigit.dropLast(1)
                    }

                    CountEditTarget.SpecifiedSumDigit -> {
                        specifiedSumDigit = specifiedSumDigit.dropLast(1)
                    }

                    CountEditTarget.LargeCount -> {
                        largeCount = largeCount.dropLast(1)
                    }

                    CountEditTarget.PrimeCount -> {
                        primeCount = primeCount.dropLast(1)
                    }

                    CountEditTarget.OddCount -> {
                        oddCount = oddCount.dropLast(1)
                    }

                    CountEditTarget.ConfirmFixedCount -> {
                        confirmFixedCount = confirmFixedCount.dropLast(1)
                    }
                }
            },
            onClearClick = {
                keyboardWarning = null
                when (target) {
                    is CountEditTarget.PageOneGroup -> {
                        pageOneGroupValues =
                            pageOneGroupValues.toMutableList().also { values ->
                                values[target.index] = ""
                            }
                    }

                    CountEditTarget.MainGroupParticipationCount -> {
                        mainGroupParticipationCount = ""
                    }

                    is CountEditTarget.PositionDigitSet -> {
                        positionDigitSets =
                            positionDigitSets.toMutableList().also { values ->
                                values[target.index] = ""
                            }
                    }

                    CountEditTarget.UndeterminedCount -> {
                        undeterminedCount = ""
                    }

                    CountEditTarget.UndeterminedSumDigit -> {
                        undeterminedSumDigit = ""
                    }

                    CountEditTarget.SpecifiedSumDigit -> {
                        specifiedSumDigit = ""
                    }

                    CountEditTarget.LargeCount -> {
                        largeCount = ""
                    }

                    CountEditTarget.PrimeCount -> {
                        primeCount = ""
                    }

                    CountEditTarget.OddCount -> {
                        oddCount = ""
                    }

                    CountEditTarget.ConfirmFixedCount -> {
                        confirmFixedCount = ""
                    }
                }
            }
        )
    }
}

private fun currentValueForTarget(
    target: CountEditTarget,
    pageOneGroupValues: List<String>,
    mainGroupParticipationCount: String,
    positionDigitSets: List<String>,
    undeterminedCount: String,
    undeterminedSumDigit: String,
    specifiedSumDigit: String,
    largeCount: String,
    primeCount: String,
    oddCount: String,
    confirmFixedCount: String,
): String {
    return when (target) {
        is CountEditTarget.PageOneGroup -> pageOneGroupValues[target.index]
        CountEditTarget.MainGroupParticipationCount -> mainGroupParticipationCount
        is CountEditTarget.PositionDigitSet -> positionDigitSets[target.index]
        CountEditTarget.UndeterminedCount -> undeterminedCount
        CountEditTarget.UndeterminedSumDigit -> undeterminedSumDigit
        CountEditTarget.SpecifiedSumDigit -> specifiedSumDigit
        CountEditTarget.LargeCount -> largeCount
        CountEditTarget.PrimeCount -> primeCount
        CountEditTarget.OddCount -> oddCount
        CountEditTarget.ConfirmFixedCount -> confirmFixedCount
    }
}

private fun isLimitedCountInputInvalid(currentValue: String, digit: String): Boolean {
    return currentValue.isNotEmpty() || digit >= "5"
}

private fun isMainGroupParticipationInvalid(currentValue: String, digit: String): Boolean {
    return currentValue.isNotEmpty() || digit >= "4"
}
