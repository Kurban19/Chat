package com.envyglit.users.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.envyglit.core.ui.entities.user.UserItem
import com.envyglit.compose.theme.ChatTheme
import com.envyglit.compose.theme.spacing

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
                painter = painterResource(id = com.envyglit.users.R.drawable.ic_done_black_24dp),
                contentDescription = null,
                modifier = Modifier
                    .width(dimensionResource(com.envyglit.core.R.dimen.icon_size))
                    .height(dimensionResource(com.envyglit.core.R.dimen.icon_size))
            )
        }
        Image(
            painter = rememberImagePainter(item.avatar),
            contentDescription = null,
            modifier = Modifier
                .width(dimensionResource(com.envyglit.core.R.dimen.avatar_item_size))
                .height(dimensionResource(com.envyglit.core.R.dimen.avatar_item_size))
                .clip(RoundedCornerShape(20.dp))
        )

        Column(
            Modifier.padding(
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium
            )
        ) {
            Text(
                item.fullName,
                style = TextStyle(
                    MaterialTheme.colors.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            val lastActivity = item.lastActivity

            Text(
                lastActivity,
                style = TextStyle(
                    MaterialTheme.colors.onPrimary,
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