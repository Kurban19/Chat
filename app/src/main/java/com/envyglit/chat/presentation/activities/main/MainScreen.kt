package com.envyglit.chat.presentation.activities.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import com.envyglit.chat.R
import com.envyglit.chat.presentation.components.ToolBar

//@Composable
//fun MainScreen(
//    mainViewModel: MainViewModel,
//    navigateToChat: (String) -> Unit,
//    scaffoldState: ScaffoldState = rememberScaffoldState()
//) {
//    // UiState of the HomeScreen
//    val uiState by mainViewModel.uiState.collectAsState()
//
//    MainScreen(
//        uiState = uiState,
//        scaffoldState = scaffoldState
//    )
//}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen() {

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val title = stringResource(id = R.string.app_name)
            ToolBar(
                modifier = Modifier
                    .fillMaxSize(),
                title = { Text(title) }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

        }
    }

}