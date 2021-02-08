package ru.skillbranch.chat.models

class UserView(
    val id: String,
    val fullName: String,
    val nickName: String,
    var avatar: String? = null,
    var status: String? = "offline",
    var initialse: String?
) {


}