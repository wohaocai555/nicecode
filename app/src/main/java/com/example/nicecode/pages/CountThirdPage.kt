package com.example.nicecode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun CountThirdPage(
    results: List<String>,
    onCopyClick: () -> Unit,
    onBackToFilterClick: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    val resultRows = results.chunked(4).map { row ->
        row.joinToString(",")
    }
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val compactWidth = maxWidth < 360.dp
        val compactHeight = maxHeight < 700.dp
        val titleFontSize = if (compactWidth) 22.sp else 26.sp
        val titleNumberSize = if (compactWidth) 26.sp else 30.sp
        val rowFontSize = if (compactWidth) 18.sp else 22.sp
        val rowLineHeight = if (compactWidth) 26.sp else 34.sp
        val buttonHeight = if (compactHeight) 50.dp else 60.dp
        val buttonFontSize = if (compactWidth) 15.sp else 18.sp
        val titleText = buildAnnotatedString {
            append("\u6311\u51fa\u9753\u7801")
            pushStyle(
                SpanStyle(
                    fontSize = titleNumberSize,
                    fontWeight = FontWeight.Bold
                )
            )
            append(results.size.toString())
            pop()
            append("\u6761\uff1a")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "\u7b2c 3 \u9875 / \u5171 3 \u9875",
                    color = FortuneTextSecondary,
                    fontSize = if (compactWidth) 15.sp else 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = titleText,
                    color = FortuneTextPrimary,
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 34.sp
                )

                CountResultPreviewBox(
                    resultRows = resultRows,
                    modifier = Modifier.weight(1f),
                    rowFontSize = rowFontSize,
                    rowLineHeight = rowLineHeight,
                    outerPadding = if (compactWidth) 8.dp else 12.dp,
                    innerHorizontalPadding = if (compactWidth) 12.dp else 18.dp,
                    innerVerticalPadding = if (compactHeight) 10.dp else 16.dp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(if (compactWidth) 6.dp else 12.dp)
            ) {
                Button(
                    onClick = onCopyClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(buttonHeight),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = "\u590d\u5236",
                        fontSize = buttonFontSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Button(
                    onClick = onBackToFilterClick,
                    modifier = Modifier
                        .weight(1.8f)
                        .height(buttonHeight),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = "\u8fd4\u56de\u7b5b\u9009",
                        fontSize = buttonFontSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Button(
                    onClick = onCompleteClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(buttonHeight),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = "\u5b8c\u6210",
                        fontSize = buttonFontSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@Composable
private fun CountResultPreviewBox(
    resultRows: List<String>,
    modifier: Modifier = Modifier,
    rowFontSize: androidx.compose.ui.unit.TextUnit = 22.sp,
    rowLineHeight: androidx.compose.ui.unit.TextUnit = 34.sp,
    outerPadding: androidx.compose.ui.unit.Dp = 12.dp,
    innerHorizontalPadding: androidx.compose.ui.unit.Dp = 18.dp,
    innerVerticalPadding: androidx.compose.ui.unit.Dp = 16.dp,
) {
    val listState = rememberLazyListState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(FortuneCardSurface)
            .padding(outerPadding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(androidx.compose.ui.graphics.Color.White.copy(alpha = 0.72f))
                .padding(
                    horizontal = innerHorizontalPadding,
                    vertical = innerVerticalPadding
                )
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(resultRows) { row ->
                    Text(
                        text = row,
                        color = FortuneTextPrimary,
                        fontSize = rowFontSize,
                        lineHeight = rowLineHeight,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            CountResultScrollbar(
                listState = listState,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(vertical = 8.dp, horizontal = 2.dp)
            )
        }
    }
}

@Composable
private fun CountResultScrollbar(
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val layoutInfo = listState.layoutInfo
    val visibleItems = layoutInfo.visibleItemsInfo
    val totalItems = layoutInfo.totalItemsCount

    if (visibleItems.isEmpty() || totalItems <= visibleItems.size) {
        return
    }

    val averageItemHeightPx = remember(visibleItems) {
        visibleItems.map { it.size }.average().toFloat().coerceAtLeast(1f)
    }
    val viewportHeightPx =
        (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset).toFloat().coerceAtLeast(1f)
    val totalContentHeightPx = averageItemHeightPx * totalItems
    val currentScrollPx =
        listState.firstVisibleItemIndex * averageItemHeightPx + listState.firstVisibleItemScrollOffset
    val maxScrollPx = (totalContentHeightPx - viewportHeightPx).coerceAtLeast(1f)
    val thumbHeightFraction = (viewportHeightPx / totalContentHeightPx).coerceIn(0.18f, 1f)
    val thumbOffsetFraction = (currentScrollPx / maxScrollPx).coerceIn(0f, 1f)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxHeight()
            .width(8.dp)
    ) {
        val thumbHeight = maxHeight * thumbHeightFraction
        val maxOffset = maxHeight - thumbHeight

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = maxOffset * thumbOffsetFraction)
                .width(4.dp)
                .height(thumbHeight)
                .clip(RoundedCornerShape(999.dp))
                .background(Color.White.copy(alpha = 0.42f))
        )
    }
}
