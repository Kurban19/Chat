package com.envyglit.main.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.envyglit.core.domain.entities.chat.ChatType
import com.envyglit.core.ui.entities.chat.ChatItem
import com.envyglit.compose.theme.ChatTheme
import com.envyglit.core.R
import com.envyglit.core.utils.StorageUtils

@Composable
fun ChatListItem(item: ChatItem, navigateToChat: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(79.dp)
            .padding(8.dp)
            .clickable(onClick = { navigateToChat(item.id) })
    ) {
        Image(
            painter = rememberImagePainter(
                data = if (!item.avatar.isNullOrBlank()) {
                    Log.d("Logger", item.avatar.toString())
                    StorageUtils.pathToReference(item.avatar!!)
                } else null,
                builder = {
                    crossfade(false)
//                    placeholder(R.drawable.placeholder) //TODO Add placeholder
                }
            ),
//            painter = rememberImagePainter("https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg"),
            contentDescription = null,
            contentScale = ContentScale.Crop,
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
                item.title,
                style = TextStyle(
                    colorResource(R.color.color_primary),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            val description = item.shortDescription ?: "Сообщений еще нет"

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
        ChatListItem(
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
            ),
            navigateToChat = { }
        )
    }
}