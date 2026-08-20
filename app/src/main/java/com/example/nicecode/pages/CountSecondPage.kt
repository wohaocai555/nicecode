package com.example.nicecode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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

private val digitPositionLabels = listOf("\u5343\u4f4d", "\u767e\u4f4d", "\u5341\u4f4d", "\u4e2a\u4f4d")
private val positionFilterRowLabels = listOf("\u5343\u4f4d", "\u767e\u4f4d", "\u5341\u4f4d", "\u4e2a\u4f4d")

@Composable
internal fun CountSecondPage(
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
    expandedSection: Int?,
    onBackClick: () -> Unit,
    onToggleSection: (Int) -> Unit,
    onPositionDigitClick: (Int) -> Unit,
    onPositionKeepChange: (Int, Boolean) -> Unit,
    onUndeterminedCountClick: () -> Unit,
    onUndeterminedSumClick: () -> Unit,
    onUndeterminedKeepChange: (Boolean) -> Unit,
    onSpecifiedPositionChange: (Int, Int, Boolean) -> Unit,
    onSpecifiedSumClick: (Int) -> Unit,
    onSpecifiedKeepChange: (Int, Boolean) -> Unit,
    onAddSpecifiedCondition: () -> Unit,
    onRemoveSpecifiedCondition: () -> Unit,
    onLargePositionChange: (Int, Boolean) -> Unit,
    onLargeKeepChange: (Boolean) -> Unit,
    onPrimePositionChange: (Int, Boolean) -> Unit,
    onPrimeKeepChange: (Boolean) -> Unit,
    onOddPositionChange: (Int, Boolean) -> Unit,
    onOddKeepChange: (Boolean) -> Unit,
    onRepeatPositionChange: (Int, Int, Boolean) -> Unit,
    onRepeatKeepChange: (Int, Boolean) -> Unit,
    onAddRepeatCondition: () -> Unit,
    onRemoveRepeatCondition: () -> Unit,
    onConfirmFixedPositionChange: (Int, Boolean) -> Unit,
    onConfirmFixedSeparateDisplayChange: (Boolean) -> Unit,
    onStartFilterClick: () -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val compactWidth = maxWidth < 360.dp
        val compactHeight = maxHeight < 700.dp
        val sectionTitleSize = if (compactWidth) 19.sp else 22.sp
        val pageTitleSize = if (compactWidth) 20.sp else 22.sp
        val filterLabelSize = if (compactWidth) 18.sp else 20.sp
        val buttonHeight = if (compactHeight) 54.dp else 64.dp

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CountBackTextButton(
                    onClick = onBackClick,
                    fontSize = if (compactWidth) 16.sp else 18.sp
                )

                Text(
                    text = "\u7b2c 2 \u9875 / \u5171 3 \u9875",
                    color = FortuneTextSecondary,
                    fontSize = if (compactWidth) 15.sp else 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "\u7b5b\u9009\u5fc3\u4e2d\u7684\u9753\u7801\uff1a",
                    color = FortuneTextPrimary,
                    fontSize = pageTitleSize,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 30.sp
                )

                ExpandableSectionCard(
                    title = "\u6570\u4f4d\u503c\u7b5b\u9009",
                    expanded = expandedSection == 0,
                    onToggle = { onToggleSection(0) },
                    titleFontSize = sectionTitleSize,
                    indicatorFontSize = if (compactWidth) 24.sp else 28.sp,
                    headerHorizontalPadding = if (compactWidth) 14.dp else 18.dp,
                    headerVerticalPadding = if (compactHeight) 12.dp else 16.dp
                ) {
                    positionFilterRowLabels.forEachIndexed { index, label ->
                        CountFilterInputRow(
                            label = label,
                            value = positionDigitSets[index],
                            onInputClick = { onPositionDigitClick(index) },
                            trailing = {
                                CountPreserveCheckbox(
                                    checked = positionDigitKeepFlags[index],
                                    onCheckedChange = { checked ->
                                        onPositionKeepChange(index, checked)
                                    }
                                )
                            }
                        )
                    }
                }

                ExpandableSectionCard(
                    title = "\u4e0d\u5b9a\u4f4d\u6570\u7b5b\u9009",
                    expanded = expandedSection == 1,
                    onToggle = { onToggleSection(1) },
                    titleFontSize = sectionTitleSize,
                    indicatorFontSize = if (compactWidth) 24.sp else 28.sp,
                    headerHorizontalPadding = if (compactWidth) 14.dp else 18.dp,
                    headerVerticalPadding = if (compactHeight) 12.dp else 16.dp
                ) {
                    CountFilterInputRow(
                        label = "\u6570\u4f4d\u4e2a\u6570",
                        value = undeterminedCount,
                        onInputClick = onUndeterminedCountClick
                    )
                    CountFilterInputRow(
                        label = "\u548c\u6570\u4e2a\u4f4d\u503c",
                        value = undeterminedSumDigit,
                        onInputClick = onUndeterminedSumClick,
                        trailing = {
                            CountPreserveCheckbox(
                                checked = undeterminedKeep,
                                onCheckedChange = onUndeterminedKeepChange
                            )
                        }
                    )
                }

                ExpandableSectionCard(
                    title = "\u6570\u4f4d\u548c\u503c\u7b5b\u9009",
                    expanded = expandedSection == 2,
                    onToggle = { onToggleSection(2) },
                    titleFontSize = sectionTitleSize,
                    indicatorFontSize = if (compactWidth) 24.sp else 28.sp,
                    headerHorizontalPadding = if (compactWidth) 14.dp else 18.dp,
                    headerVerticalPadding = if (compactHeight) 12.dp else 16.dp
                ) {
                    specifiedSumConditions.forEachIndexed { conditionIndex, condition ->
                        SpecifiedSumConditionContent(
                            condition = condition,
                            conditionIndex = conditionIndex,
                            compactWidth = compactWidth,
                            onPositionChange = onSpecifiedPositionChange,
                            onSumClick = onSpecifiedSumClick,
                            onKeepChange = onSpecifiedKeepChange
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (specifiedSumConditions.size < 5) {
                            TextButton(onClick = onAddSpecifiedCondition) {
                                Text(
                                    text = "\u65b0\u589e\u6761\u4ef6",
                                    color = Color(0xFF3F74C7),
                                    fontSize = if (compactWidth) 16.sp else 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                        if (specifiedSumConditions.size > 1) {
                            TextButton(onClick = onRemoveSpecifiedCondition) {
                                Text(
                                    text = "\u5220\u9664\u6761\u4ef6",
                                    color = Color(0xFFB04A4A),
                                    fontSize = if (compactWidth) 16.sp else 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }

                ExpandableSectionCard(
                    title = "\u6027\u8d28\u7b5b\u9009",
                    expanded = expandedSection == 3,
                    onToggle = { onToggleSection(3) },
                    titleFontSize = sectionTitleSize,
                    indicatorFontSize = if (compactWidth) 24.sp else 28.sp,
                    headerHorizontalPadding = if (compactWidth) 14.dp else 18.dp,
                    headerVerticalPadding = if (compactHeight) 12.dp else 16.dp
                ) {
                    PropertyPositionFilterContent(
                        label = "\u5927\u6570",
                        positions = largePositions,
                        keep = largeKeep,
                        compactWidth = compactWidth,
                        onPositionChange = onLargePositionChange,
                        onKeepChange = onLargeKeepChange
                    )
                    PropertyPositionFilterContent(
                        label = "\u8d28\u6570",
                        positions = primePositions,
                        keep = primeKeep,
                        compactWidth = compactWidth,
                        onPositionChange = onPrimePositionChange,
                        onKeepChange = onPrimeKeepChange
                    )
                    PropertyPositionFilterContent(
                        label = "\u5947\u6570",
                        positions = oddPositions,
                        keep = oddKeep,
                        compactWidth = compactWidth,
                        onPositionChange = onOddPositionChange,
                        onKeepChange = onOddKeepChange
                    )
                }

                ExpandableSectionCard(
                    title = "\u91cd\u590d\u503c\u7b5b\u9009",
                    expanded = expandedSection == 4,
                    onToggle = { onToggleSection(4) },
                    titleFontSize = sectionTitleSize,
                    indicatorFontSize = if (compactWidth) 24.sp else 28.sp,
                    headerHorizontalPadding = if (compactWidth) 14.dp else 18.dp,
                    headerVerticalPadding = if (compactHeight) 12.dp else 16.dp
                ) {
                    repeatConditions.forEachIndexed { conditionIndex, condition ->
                        RepeatValueConditionContent(
                            condition = condition,
                            conditionIndex = conditionIndex,
                            compactWidth = compactWidth,
                            onPositionChange = onRepeatPositionChange,
                            onKeepChange = onRepeatKeepChange
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (repeatConditions.size < 5) {
                            TextButton(onClick = onAddRepeatCondition) {
                                Text(
                                    text = "\u65b0\u589e\u6761\u4ef6",
                                    color = Color(0xFF3F74C7),
                                    fontSize = if (compactWidth) 16.sp else 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                        if (repeatConditions.size > 1) {
                            TextButton(onClick = onRemoveRepeatCondition) {
                                Text(
                                    text = "\u5220\u9664\u6761\u4ef6",
                                    color = Color(0xFFB04A4A),
                                    fontSize = if (compactWidth) 16.sp else 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }

                ExpandableSectionCard(
                    title = "\u786e\u8ba4\u5b9a\u6570",
                    expanded = expandedSection == 5,
                    onToggle = { onToggleSection(5) },
                    titleFontSize = sectionTitleSize,
                    indicatorFontSize = if (compactWidth) 24.sp else 28.sp,
                    headerHorizontalPadding = if (compactWidth) 14.dp else 18.dp,
                    headerVerticalPadding = if (compactHeight) 12.dp else 16.dp
                ) {
                    DigitPositionSelection(
                        labels = digitPositionLabels,
                        checkedValues = confirmFixedPositions,
                        compactWidth = compactWidth,
                        onCheckedChange = onConfirmFixedPositionChange
                    )
                    CountPreserveCheckbox(
                        checked = confirmFixedSeparateDisplay,
                        label = "\u5355\u72ec\u663e\u793a",
                        labelFontSize = if (compactWidth) 16.sp else 18.sp,
                        compact = compactWidth,
                        onCheckedChange = onConfirmFixedSeparateDisplayChange
                    )
                }
            }

            Button(
                onClick = onStartFilterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .height(buttonHeight),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = "\u5f00\u59cb\u7b5b\u9009",
                    fontSize = if (compactWidth) 21.sp else 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PropertyPositionFilterContent(
    label: String,
    positions: List<Boolean>,
    keep: Boolean,
    compactWidth: Boolean,
    onPositionChange: (Int, Boolean) -> Unit,
    onKeepChange: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            color = FortuneTextPrimary,
            fontSize = if (compactWidth) 18.sp else 20.sp,
            fontWeight = FontWeight.Medium
        )
        DigitPositionSelection(
            labels = digitPositionLabels,
            checkedValues = positions,
            compactWidth = compactWidth,
            onCheckedChange = onPositionChange
        )
        CountPreserveCheckbox(
            checked = keep,
            labelFontSize = if (compactWidth) 16.sp else 18.sp,
            compact = compactWidth,
            onCheckedChange = onKeepChange
        )
    }
}

@Composable
private fun SpecifiedSumConditionContent(
    condition: SpecifiedSumFilterCondition,
    conditionIndex: Int,
    compactWidth: Boolean,
    onPositionChange: (Int, Int, Boolean) -> Unit,
    onSumClick: (Int) -> Unit,
    onKeepChange: (Int, Boolean) -> Unit,
) {
    DigitPositionSelection(
        labels = digitPositionLabels,
        checkedValues = condition.positions,
        compactWidth = compactWidth,
        onCheckedChange = { positionIndex, checked ->
            onPositionChange(conditionIndex, positionIndex, checked)
        }
    )

    CountFilterInputRow(
        label = "\u548c\u6570\u4e2a\u4f4d\u503c",
        value = condition.sumDigits,
        onInputClick = { onSumClick(conditionIndex) },
        trailing = {
            CountPreserveCheckbox(
                checked = condition.keep,
                onCheckedChange = { checked -> onKeepChange(conditionIndex, checked) }
            )
        }
    )
}

@Composable
private fun RepeatValueConditionContent(
    condition: RepeatValueFilterCondition,
    conditionIndex: Int,
    compactWidth: Boolean,
    onPositionChange: (Int, Int, Boolean) -> Unit,
    onKeepChange: (Int, Boolean) -> Unit,
) {
    DigitPositionSelection(
        labels = digitPositionLabels,
        checkedValues = condition.positions,
        compactWidth = compactWidth,
        onCheckedChange = { positionIndex, checked ->
            onPositionChange(conditionIndex, positionIndex, checked)
        }
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        CountPreserveCheckbox(
            checked = condition.keep,
            onCheckedChange = { checked -> onKeepChange(conditionIndex, checked) }
        )
    }
}

@Composable
private fun CountFilterInputRow(
    label: String,
    value: String,
    onInputClick: () -> Unit,
    trailing: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = label,
            color = FortuneTextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val compactWidth = maxWidth < 360.dp
            val inputWidth = if (trailing != null) {
                maxWidth * if (compactWidth) 0.58f else 0.68f
            } else {
                maxWidth
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CountDigitInputBox(
                    value = value,
                    onClick = onInputClick,
                    modifier = Modifier.width(inputWidth),
                    textFontSize = if (compactWidth) 16.sp else 18.sp,
                    horizontalPadding = if (compactWidth) 10.dp else 16.dp,
                    verticalPadding = if (compactWidth) 5.dp else 8.dp
                )
                if (trailing != null) {
                    Box {
                        trailing()
                    }
                }
            }
        }
    }
}
