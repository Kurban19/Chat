package com.example.compose.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ToolBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackPressed: () -> Unit = { },
    elevation: Dp = 0.dp,
    navigationIcon: @Composable (() -> Unit)? = null,
    ) {
    TopAppBar(
        modifier = modifier,
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
                    color = Color.White
                )
            )
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = elevation
    )
}

@Preview
@Composable
fun ToolBarPreview() {
    MaterialTheme() {
        ToolBar(title = "Preview!")
    }
}
