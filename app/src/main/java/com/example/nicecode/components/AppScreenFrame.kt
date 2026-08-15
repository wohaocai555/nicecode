package com.example.nicecode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val FortuneHeaderRed = Color(0xFFB23A34)
val FortuneCream = Color(0xFFF5EBD8)
val FortuneTextPrimary = Color(0xFF3B2F2F)
val FortuneTextSecondary = Color(0xFF6B5B53)
val FortuneLuckyGold = Color(0xFFD48A2F)
val FortuneGoodGreen = Color(0xFF4E8B57)
val FortuneBadRed = Color(0xFFB04A4A)
val FortuneCardBorder = Color(0xFFD8C4AC)
val FortuneCardSurface = Color(0xFFF9F2E5)

@Composable
fun AppSectionScreen(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FortuneCream)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val compactWidth = maxWidth < 360.dp
            val compactHeight = maxHeight < 640.dp
            val horizontalPadding = if (compactWidth) 14.dp else 24.dp
            val verticalPadding = if (compactHeight) 14.dp else 28.dp
            val headerPadding = if (compactHeight) 12.dp else 18.dp
            val titleSize = if (compactWidth) 21.sp else 24.sp

            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(FortuneHeaderRed)
                        .padding(vertical = headerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = titleSize,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = horizontalPadding, vertical = verticalPadding),
                    content = content
                )
            }
        }
    }
}
