package com.envyglit.chat.presentation.activities.users

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.envyglit.compose.components.ToolBar

@Composable
fun UsersScreen(
    navigateHome: () -> Unit,
    viewModel: UsersViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState()

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = { ToolBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  }
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    ) {
        LazyColumn(contentPadding = PaddingValues(end = 20.dp)) {
            items(state.value.userItems.size) { index ->
                UserListItem(state.value.userItems[index])
            }
        }
    }

}