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
    data class SpecifiedSumDigit(val conditionIndex: Int) : CountEditTarget
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
    var specifiedSumConditions by remember {
        mutableStateOf(listOf(SpecifiedSumFilterCondition()))
    }
    var largePositions by remember { mutableStateOf(List(4) { false }) }
    var largeKeep by remember { mutableStateOf(false) }
    var primePositions by remember { mutableStateOf(List(4) { false }) }
    var primeKeep by remember { mutableStateOf(false) }
    var oddPositions by remember { mutableStateOf(List(4) { false }) }
    var oddKeep by remember { mutableStateOf(false) }
    var repeatConditions by remember {
        mutableStateOf(listOf(RepeatValueFilterCondition()))
    }
    var confirmFixedPositions by remember { mutableStateOf(List(4) { false }) }
    var confirmFixedSeparateDisplay by remember { mutableStateOf(false) }
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
                    specifiedSumConditions = specifiedSumConditions,
                    largePositions = largePositions,
                    largeKeep = largeKeep,
                    primePositions = primePositions,
                    primeKeep = primeKeep,
                    oddPositions = oddPositions,
                    oddKeep = oddKeep,
                    repeatConditions = repeatConditions,
                    confirmFixedPositions = confirmFixedPositions,
                    confirmFixedSeparateDisplay = confirmFixedSeparateDisplay,
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
                    onSpecifiedPositionChange = { conditionIndex, positionIndex, checked ->
                        specifiedSumConditions = specifiedSumConditions.toMutableList().also { conditions ->
                            val condition = conditions[conditionIndex]
                            conditions[conditionIndex] = condition.copy(
                                positions = condition.positions.toMutableList().also { positions ->
                                    positions[positionIndex] = checked
                                }
                            )
                        }
                    },
                    onSpecifiedSumClick = { conditionIndex ->
                        keyboardWarning = null
                        editingTarget = CountEditTarget.SpecifiedSumDigit(conditionIndex)
                    },
                    onSpecifiedKeepChange = { conditionIndex, checked ->
                        specifiedSumConditions = specifiedSumConditions.toMutableList().also { conditions ->
                            conditions[conditionIndex] = conditions[conditionIndex].copy(keep = checked)
                        }
                    },
                    onAddSpecifiedCondition = {
                        if (specifiedSumConditions.size < 5) {
                            specifiedSumConditions = specifiedSumConditions + SpecifiedSumFilterCondition()
                        }
                    },
                    onRemoveSpecifiedCondition = {
                        if (specifiedSumConditions.size > 1) {
                            val removedIndex = specifiedSumConditions.lastIndex
                            specifiedSumConditions = specifiedSumConditions.dropLast(1)
                            if (editingTarget == CountEditTarget.SpecifiedSumDigit(removedIndex)) {
                                editingTarget = null
                                keyboardWarning = null
                            }
                        }
                    },
                    onLargePositionChange = { index, checked ->
                        largePositions = largePositions.toMutableList().also { values ->
                            values[index] = checked
                        }
                    },
                    onLargeKeepChange = { largeKeep = it },
                    onPrimePositionChange = { index, checked ->
                        primePositions = primePositions.toMutableList().also { values ->
                            values[index] = checked
                        }
                    },
                    onPrimeKeepChange = { primeKeep = it },
                    onOddPositionChange = { index, checked ->
                        oddPositions = oddPositions.toMutableList().also { values ->
                            values[index] = checked
                        }
                    },
                    onOddKeepChange = { oddKeep = it },
                    onRepeatPositionChange = { conditionIndex, positionIndex, checked ->
                        repeatConditions = repeatConditions.toMutableList().also { conditions ->
                            val condition = conditions[conditionIndex]
                            conditions[conditionIndex] = condition.copy(
                                positions = condition.positions.toMutableList().also { positions ->
                                    positions[positionIndex] = checked
                                }
                            )
                        }
                    },
                    onRepeatKeepChange = { conditionIndex, checked ->
                        repeatConditions = repeatConditions.toMutableList().also { conditions ->
                            conditions[conditionIndex] = conditions[conditionIndex].copy(keep = checked)
                        }
                    },
                    onAddRepeatCondition = {
                        if (repeatConditions.size < 5) {
                            repeatConditions = repeatConditions + RepeatValueFilterCondition()
                        }
                    },
                    onRemoveRepeatCondition = {
                        if (repeatConditions.size > 1) {
                            repeatConditions = repeatConditions.dropLast(1)
                        }
                    },
                    onConfirmFixedPositionChange = { index, checked ->
                        confirmFixedPositions = confirmFixedPositions.toMutableList().also { values ->
                            values[index] = checked
                        }
                    },
                    onConfirmFixedSeparateDisplayChange = { confirmFixedSeparateDisplay = it },
                    onStartFilterClick = {
                        val resultsToDisplay = applyCountSecondPageFilters(
                            baseResults = basePermutationResults,
                            positionDigitSets = positionDigitSets,
                            positionDigitKeepFlags = positionDigitKeepFlags,
                            undeterminedCount = undeterminedCount,
                            undeterminedSumDigit = undeterminedSumDigit,
                            undeterminedKeep = undeterminedKeep,
                            specifiedSumConditions = specifiedSumConditions,
                            largePositions = largePositions,
                            largeKeep = largeKeep,
                            primePositions = primePositions,
                            primeKeep = primeKeep,
                            oddPositions = oddPositions,
                            oddKeep = oddKeep,
                            repeatConditions = repeatConditions,
                            confirmFixedPositions = confirmFixedPositions,
                            confirmFixedSeparateDisplay = confirmFixedSeparateDisplay
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
                            AnnotatedString(formatResultsForCopy(displayedResults))
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
                        specifiedSumConditions = listOf(SpecifiedSumFilterCondition())
                        largePositions = List(4) { false }
                        largeKeep = false
                        primePositions = List(4) { false }
                        primeKeep = false
                        oddPositions = List(4) { false }
                        oddKeep = false
                        repeatConditions = listOf(RepeatValueFilterCondition())
                        confirmFixedPositions = List(4) { false }
                        confirmFixedSeparateDisplay = false
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
                specifiedSumConditions = specifiedSumConditions,
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

                    is CountEditTarget.SpecifiedSumDigit -> {
                        val condition = specifiedSumConditions[target.conditionIndex]
                        if (condition.sumDigits.contains(digit)) {
                            keyboardWarning =
                                "\u91cd\u590d\u63d0\u9192\uff1a\u5df2\u8f93\u5165\u6570\u5b57 $digit"
                        } else {
                            keyboardWarning = null
                            specifiedSumConditions = specifiedSumConditions.toMutableList().also { conditions ->
                                val currentCondition = conditions[target.conditionIndex]
                                conditions[target.conditionIndex] = currentCondition.copy(
                                    sumDigits = currentCondition.sumDigits + digit
                                )
                            }
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

                    is CountEditTarget.SpecifiedSumDigit -> {
                        specifiedSumConditions = specifiedSumConditions.toMutableList().also { conditions ->
                            val condition = conditions[target.conditionIndex]
                            conditions[target.conditionIndex] = condition.copy(
                                sumDigits = condition.sumDigits.dropLast(1)
                            )
                        }
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

                    is CountEditTarget.SpecifiedSumDigit -> {
                        specifiedSumConditions = specifiedSumConditions.toMutableList().also { conditions ->
                            conditions[target.conditionIndex] = conditions[target.conditionIndex].copy(sumDigits = "")
                        }
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
    specifiedSumConditions: List<SpecifiedSumFilterCondition>,
): String {
    return when (target) {
        is CountEditTarget.PageOneGroup -> pageOneGroupValues[target.index]
        CountEditTarget.MainGroupParticipationCount -> mainGroupParticipationCount
        is CountEditTarget.PositionDigitSet -> positionDigitSets[target.index]
        CountEditTarget.UndeterminedCount -> undeterminedCount
        CountEditTarget.UndeterminedSumDigit -> undeterminedSumDigit
        is CountEditTarget.SpecifiedSumDigit -> {
            specifiedSumConditions[target.conditionIndex].sumDigits
        }
    }
}

private fun isLimitedCountInputInvalid(currentValue: String, digit: String): Boolean {
    return currentValue.isNotEmpty() || digit >= "5"
}

private fun isMainGroupParticipationInvalid(currentValue: String, digit: String): Boolean {
    return currentValue.isNotEmpty() || digit >= "4"
}
