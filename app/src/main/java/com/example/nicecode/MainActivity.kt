package com.example.nicecode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.nicecode.ui.theme.NicecodeTheme

class MainActivity : ComponentActivity() {
    private val dailyFortuneViewModel: DailyFortuneViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NicecodeTheme {
                NicecodeApp(
                    dailyFortuneViewModel = dailyFortuneViewModel,
                    historyViewModel = historyViewModel
                )
            }
        }
    }
}

@Composable
internal fun NicecodeApp(
    dailyFortuneViewModel: DailyFortuneViewModel,
    historyViewModel: HistoryViewModel,
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (currentDestination) {
                AppDestinations.HOME -> HomeScreen(
                    dailyFortuneViewModel = dailyFortuneViewModel,
                    modifier = Modifier.padding(innerPadding)
                )

                AppDestinations.COUNT -> CountScreen(
                    historyViewModel = historyViewModel,
                    modifier = Modifier.padding(innerPadding)
                )

                AppDestinations.HISTORY -> HistoryScreen(
                    historyViewModel = historyViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    HOME("\u4e3b\u9875", R.drawable.ic_home),
    COUNT("\u8ba1\u7b97", R.drawable.ic_count),
    HISTORY("\u5386\u53f2\u8bb0\u5f55", R.drawable.ic_history),
}
