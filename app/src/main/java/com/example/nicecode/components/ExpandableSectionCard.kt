package com.example.nicecode

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.delay

@Composable
internal fun ExpandableSectionCard(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 18.dp, end = 18.dp, bottom = 16.dp),
    contentSpacing: androidx.compose.ui.unit.Dp = 12.dp,
    titleFontSize: TextUnit = 22.sp,
    indicatorFontSize: TextUnit = 28.sp,
    headerHorizontalPadding: androidx.compose.ui.unit.Dp = 18.dp,
    headerVerticalPadding: androidx.compose.ui.unit.Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val bottomRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(expanded) {
        if (expanded) {
            delay(120)
            bottomRequester.bringIntoView()
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = FortuneCardSurface
        ),
        border = BorderStroke(1.dp, FortuneCardBorder),
        shape = RoundedCornerShape(18.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(horizontal = headerHorizontalPadding, vertical = headerVerticalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = FortuneTextPrimary,
                fontSize = titleFontSize,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = if (expanded) "\u2304" else "\u203A",
                modifier = Modifier.padding(start = 12.dp),
                color = FortuneTextSecondary,
                fontSize = indicatorFontSize,
                fontWeight = FontWeight.Bold
            )
        }

        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(contentSpacing)
            ) {
                content()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .bringIntoViewRequester(bottomRequester)
                )
            }
        }
    }
}

internal fun Int?.toggleExclusiveExpanded(value: Int): Int? {
    return if (this == value) null else value
}
