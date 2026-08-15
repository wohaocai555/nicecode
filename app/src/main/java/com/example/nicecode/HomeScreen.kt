package com.example.nicecode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@Composable
internal fun HomeScreen(
    dailyFortuneViewModel: DailyFortuneViewModel,
    modifier: Modifier = Modifier,
) {
    val dailyFortune = dailyFortuneViewModel.dailyFortune.collectAsState().value

    AppSectionScreen(
        title = "\u4eca\u65e5\u8fd0\u52bf",
        modifier = modifier
    ) {
        HomePageContent(fortune = dailyFortune)
    }
}
