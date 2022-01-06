package com.envyglit.main.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.envyglit.compose.components.ToolBar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navigateToChat: (String) -> Unit,
    navigateToUsers: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState()

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = { ToolBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToUsers
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    ) {
        LazyColumn(contentPadding = PaddingValues(end = 120.dp)) {
            items(state.value.chatItems.size) { index ->
                ChatListItem(
                    state.value.chatItems[index],
                    navigateToChat
                )
            }
        }
    }

}

