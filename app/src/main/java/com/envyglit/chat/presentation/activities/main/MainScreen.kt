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
import com.envyglit.chat.domain.entities.chat.ChatItem
import com.envyglit.chat.presentation.components.ToolBar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(list: List<ChatItem>) {

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val title = "LazyColomn"
            ToolBar(
                title = title
            )
        }
    ) {
        LazyColumn() {
            items(list.size) { index ->
                ComposeChatItem(list[index])
            }
        }
    }

}
