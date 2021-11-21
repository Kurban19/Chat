package com.envyglit.chat.presentation.activities.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.envyglit.chat.presentation.components.ToolBar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ToolBar(
                title = "Chat"
            )
        }
    ) {
//        LazyColumn() {
//            items(list.size) { index ->
//                ComposeChatItem(list[index])
//            }
//        }
    }

}
