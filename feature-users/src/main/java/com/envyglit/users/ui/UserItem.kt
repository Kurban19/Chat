package com.envyglit.chat.presentation.activities.users

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.envyglit.chat.R
import com.envyglit.core.ui.entities.user.UserItem
import com.envyglit.compose.theme.ChatTheme

@Composable
fun UserListItem(item: UserItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(79.dp)
            .padding(16.dp),
    ) {
        if (item.isSelected) {
            Image(
                painter = painterResource(id = R.drawable.ic_done_black_24dp),
                contentDescription = null,
                modifier = Modifier
                    .width(dimensionResource(R.dimen.icon_size))
                    .height(dimensionResource(R.dimen.icon_size))
            )
        }
        Image(
            painter = rememberImagePainter(item.avatar),
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
                item.fullName,
                style = TextStyle(
                    colorResource(R.color.color_primary),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            val lastActivity = item.lastActivity

            Text(
                lastActivity,
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
fun PreviewUserItem() {
    ChatTheme() {
        UserListItem(UserItem("1", "John Doe", "JD", null, "online", false))
    }
}