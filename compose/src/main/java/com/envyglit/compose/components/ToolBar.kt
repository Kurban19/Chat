package com.envyglit.compose.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun ToolBar(
    modifier: Modifier = Modifier,
    title: String = "Chat",
    onBackPressed: () -> Unit = { },
    elevation: Dp = 0.dp,
    navigationIcon: @Composable (() -> Unit)? = null,
    ) {
    TopAppBar(
        navigationIcon = navigationIcon,
//        navigationIcon = {
//            IconButton(
//                onClick = onBackPressed
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_arrow),
//                    contentDescription = null,
//                    tint = Color.White
//                )
//            }
//        }
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = elevation,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(bottom = false)
    )
}

@Preview
@Composable
fun ToolBarPreview() {
    MaterialTheme() {
        ToolBar(title = "Preview!")
    }
}
