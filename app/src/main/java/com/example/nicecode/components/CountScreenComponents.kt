package com.example.nicecode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit

@Composable
internal fun CountDigitInputBox(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textFontSize: TextUnit = 18.sp,
    horizontalPadding: androidx.compose.ui.unit.Dp = 16.dp,
    verticalPadding: androidx.compose.ui.unit.Dp = 8.dp,
) {
    val displayValue = formatDigitDisplayValue(value)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White.copy(alpha = 0.72f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        Text(
            text = if (value.isEmpty()) "\u70b9\u51fb\u9009\u62e9\u6570\u5b57" else displayValue,
            color = if (value.isEmpty()) {
                FortuneTextSecondary.copy(alpha = 0.8f)
            } else {
                FortuneTextPrimary
            },
            fontSize = textFontSize,
            fontWeight = if (value.isEmpty()) FontWeight.Normal else FontWeight.SemiBold
        )
    }
}

@Composable
internal fun CountBackTextButton(
    onClick: () -> Unit,
    fontSize: TextUnit = 18.sp,
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(bottom = 2.dp)
    ) {
        Text(
            text = "< \u8fd4\u56de\u4e0a\u4e00\u9875",
            color = Color(0xFF3F74C7),
            fontSize = fontSize,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
internal fun NumberKeyboardDialog(
    value: String,
    warningMessage: String?,
    onDismiss: () -> Unit,
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
) {
    val displayValue = formatDigitDisplayValue(value)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "\u9009\u62e9\u6570\u5b57",
                color = FortuneTextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White.copy(alpha = 0.78f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = if (value.isEmpty()) "\u5c1a\u672a\u9009\u62e9" else displayValue,
                        color = FortuneTextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (warningMessage != null) {
                    Text(
                        text = warningMessage,
                        color = FortuneBadRed,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val itemSpacing = 10.dp
                    val itemWidth = (maxWidth - itemSpacing * 2) / 3

                    Column(verticalArrangement = Arrangement.spacedBy(itemSpacing)) {
                        listOf(
                            listOf("1", "2", "3"),
                            listOf("4", "5", "6"),
                            listOf("7", "8", "9"),
                            listOf("\u6e05\u7a7a", "0", "\u5220\u9664")
                        ).forEach { rowItems ->
                            Row(horizontalArrangement = Arrangement.spacedBy(itemSpacing)) {
                                rowItems.forEach { item ->
                                    val action: () -> Unit = when (item) {
                                        "\u6e05\u7a7a" -> onClearClick
                                        "\u5220\u9664" -> onDeleteClick
                                        else -> { { onDigitClick(item) } }
                                    }

                                    Button(
                                        onClick = action,
                                        modifier = Modifier
                                            .width(itemWidth)
                                            .height(52.dp),
                                        shape = RoundedCornerShape(14.dp)
                                    ) {
                                        Text(
                                            text = item,
                                            fontSize = if (
                                                item == "\u6e05\u7a7a" || item == "\u5220\u9664"
                                            ) {
                                                16.sp
                                            } else {
                                                18.sp
                                            },
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "\u5b8c\u6210",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = FortuneCardSurface
    )
}

@Composable
internal fun CountPreserveCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String = "\u4fdd\u7559",
    labelFontSize: TextUnit = 18.sp,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    Row(
        modifier = if (compact) {
            modifier.clickable { onCheckedChange(!checked) }
        } else {
            modifier
        },
        horizontalArrangement = if (compact) Arrangement.spacedBy(2.dp) else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (compact) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .background(
                        color = if (checked) FortuneGoodGreen else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (checked) FortuneGoodGreen else FortuneTextSecondary,
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (checked) {
                    Text(
                        text = "\u2713",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
        Text(
            text = label,
            color = FortuneTextSecondary,
            fontSize = labelFontSize,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
internal fun CountMainGroupCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    labelFontSize: TextUnit = 14.sp,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "\u8bbe\u4e3a\u4e3b\u8981\u7ec4\u5408",
            color = FortuneTextSecondary,
            fontSize = labelFontSize,
            fontWeight = FontWeight.Medium
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

private fun formatDigitDisplayValue(value: String): String {
    return value.toCharArray().joinToString("\uff0c")
}
