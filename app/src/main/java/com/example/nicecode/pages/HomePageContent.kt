package com.example.nicecode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun HomePageContent(fortune: DailyFortune?) {
    val levelMeta = fortune?.levelMeta
    val levelText = levelMeta?.label ?: "\u52a0\u8f7d\u4e2d"
    val favorableText = fortune?.favorable?.joinToString("\u3001") ?: "\u6b63\u5728\u8ba1\u7b97"
    val unfavorableText = fortune?.unfavorable?.joinToString("\u3001") ?: "\u6b63\u5728\u8ba1\u7b97"
    val luckyNumberText = fortune?.luckyNumber?.toString() ?: "-"
    val quoteText = levelMeta?.quote ?: "\u6b63\u5728\u751f\u6210\u4eca\u65e5\u8fd0\u52bf"
    val levelColor = fortune?.let { fortuneLevelColor(it.levelIndex) } ?: FortuneTextSecondary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "\u2728\u60a8\u4eca\u65e5\u7684\u8fd0\u52bf\u4e3a\uff1a",
                color = FortuneTextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = levelText,
                color = levelColor,
                fontSize = 46.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(36.dp))
            FortuneLine(
                label = "\u5b9c",
                value = favorableText,
                labelColor = FortuneGoodGreen,
                valueColor = FortuneTextSecondary
            )
            Spacer(modifier = Modifier.height(14.dp))
            FortuneLine(
                label = "\u5fcc",
                value = unfavorableText,
                labelColor = FortuneBadRed,
                valueColor = FortuneTextSecondary
            )
            Spacer(modifier = Modifier.height(14.dp))
            FortuneLine(
                label = "\u5e78\u8fd0\u6570\u5b57",
                value = luckyNumberText,
                labelColor = FortuneTextPrimary,
                valueColor = FortuneTextSecondary
            )
        }

        Text(
            text = quoteText,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            color = FortuneTextSecondary,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

private fun fortuneLevelColor(levelIndex: Int): Color {
    return when (levelIndex) {
        0 -> FortuneBadRed
        1 -> Color(0xFF5C6F7A)
        2, 3 -> Color(0xFF3C8C7A)
        4, 5, 6 -> FortuneLuckyGold
        else -> FortuneTextSecondary
    }
}

@Composable
private fun FortuneLine(
    label: String,
    value: String,
    labelColor: Color,
    valueColor: Color,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label\uFF1A",
            color = labelColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = value,
            color = valueColor,
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )
    }
}
