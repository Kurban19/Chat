package com.envyglit.chat.presentation.main

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    navigateToChat: (String) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    // UiState of the HomeScreen
    val uiState by mainViewModel.uiState.collectAsState()

    MainScreen(
        uiState = uiState,
        scaffoldState = scaffoldState
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    scaffoldState: ScaffoldState
) {

}