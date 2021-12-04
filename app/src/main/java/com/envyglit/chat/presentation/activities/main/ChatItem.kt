package com.envyglit.chat.presentation.activities.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.envyglit.chat.R
import com.envyglit.chat.domain.entities.chat.ChatItem
import com.envyglit.chat.domain.entities.data.ChatType
import com.example.compose.theme.ChatTheme

@Composable
fun ComposeChatItem(chatItem: ChatItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(79.dp)
            .padding(16.dp)
    ) {
        Image(
            painter = rememberImagePainter(chatItem.avatar),
            contentDescription = null,
            modifier = Modifier
                .width(dimensionResource(R.dimen.avatar_item_size))
                .height(dimensionResource(R.dimen.avatar_item_size))
                .clip(RoundedCornerShape(20.dp))
        )

        Column(
            Modifier.padding(
                start = dimensionResource(R.dimen.spacing_normal_16),
                end = dimensionResource(R.dimen.spacing_normal_16)
            )
        ) {
            Text(
                chatItem.title,
                style = TextStyle(
                    colorResource(R.color.color_primary),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            val description = chatItem.shortDescription ?: "Сообщений еще нет"

            Text(
                description,
                style = TextStyle(
                    colorResource(R.color.color_gray_dark),
                    fontSize = 14.sp,
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewChatItem() {
    ChatTheme {
        ComposeChatItem(
            ChatItem(
                id = "1",
                null,
                "KK",
                "John Doe",
                null,
                0,
                null,
                false,
                ChatType.SINGLE
            )
        )
    }
}