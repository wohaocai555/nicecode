package com.example.nicecode

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val countGroupLabels = listOf("1\u7ec4", "2\u7ec4", "3\u7ec4", "4\u7ec4")

@Composable
internal fun CountFirstPage(
    values: List<String>,
    mainGroupIndex: Int,
    mainGroupParticipationCount: String,
    onInputClick: (Int) -> Unit,
    onMainGroupParticipationClick: () -> Unit,
    onMainGroupSelect: (Int) -> Unit,
    onStartClick: () -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val compactWidth = maxWidth < 360.dp
        val compactHeight = maxHeight < 700.dp
        val sectionTitleSize = if (compactWidth) 18.sp else 20.sp
        val groupLabelSize = if (compactWidth) 19.sp else 22.sp
        val descriptionSize = if (compactWidth) 18.sp else 20.sp
        val buttonHeight = if (compactHeight) 52.dp else 60.dp
        val inputFontSize = if (compactWidth) 18.sp else 20.sp
        val inputVerticalPadding = if (compactHeight) 5.dp else 8.dp

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "\u7b2c 1 \u9875 / \u5171 3 \u9875",
                    color = FortuneTextSecondary,
                    fontSize = if (compactWidth) 15.sp else 17.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "\u5c06\u60f3\u8981\u6392\u5217\u7684\u6570\u5b57\u7ec4\u5408\u586b\u5165\u65b9\u6846\u5185(\u81f3\u5c11\u586b\u5165\u4e00\u7ec4)\uff1a",
                    color = FortuneTextPrimary,
                    fontSize = descriptionSize,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 28.sp
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = FortuneCardSurface
                    ),
                    border = BorderStroke(1.dp, FortuneCardBorder),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "\u4e3b\u8981\u7ec4\u5408\u53c2\u4e0e\u6570",
                                color = FortuneTextPrimary,
                                fontSize = sectionTitleSize,
                                fontWeight = FontWeight.Bold
                            )
                            CountDigitInputBox(
                                value = mainGroupParticipationCount,
                                onClick = onMainGroupParticipationClick,
                                modifier = Modifier.fillMaxWidth(),
                                textFontSize = if (compactWidth) 16.sp else 18.sp,
                                verticalPadding = inputVerticalPadding
                            )
                        }

                        countGroupLabels.forEachIndexed { index, label ->
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = label,
                                        color = FortuneTextPrimary,
                                        fontSize = groupLabelSize,
                                        fontWeight = FontWeight.Bold
                                    )
                                    CountMainGroupCheckbox(
                                    checked = mainGroupIndex == index,
                                        onCheckedChange = { onMainGroupSelect(index) },
                                        labelFontSize = if (compactWidth) 12.sp else 14.sp
                                    )
                                }
                                CountDigitInputBox(
                                    value = values[index],
                                    onClick = { onInputClick(index) },
                                    modifier = Modifier.fillMaxWidth(),
                                    textFontSize = inputFontSize,
                                    verticalPadding = inputVerticalPadding
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .height(buttonHeight),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = "\u5f00\u59cb\u6392\u5217",
                    fontSize = if (compactWidth) 20.sp else 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
