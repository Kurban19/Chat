package com.envyglit.chat.presentation.activities.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.components.ToolBar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navigateToChat: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state = viewModel.uiState.collectAsState()

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = { ToolBar() }
    ) {
        LazyColumn() {
            items(state.value.chatItems.size) { index ->
                ComposeChatItem(state.value.chatItems[index])
            }
        }
    }

}
