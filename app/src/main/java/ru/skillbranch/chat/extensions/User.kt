package ru.skillbranch.chat.extensions




import ru.skillbranch.chat.models.Profile
import ru.skillbranch.chat.models.data.User
import ru.skillbranch.chat.models.UserView
import ru.skillbranch.chat.utils.Utils



fun User.toUserView(): UserView {

    val nickName = Utils.transliteration("$firstName $lastName")
    val initials = Utils.toInitials(firstName, lastName)
    val status = if(lastVisit == null) "Еше ни разу не был" else if(isOnline) "online" else "Последний раз был ${lastVisit!!.humanizeDiff()}"
    val avatar: String = "test"
    return UserView(
        id,
        fullName = "$firstName $lastName",
        nickName = nickName,
        initialse = initials,
        avatar = avatar,
        status = status
    )

}

fun User.toProfile(): Profile {
    return Profile(this.firstName, this.lastName, "", "", this.rating, this.respect)
}

