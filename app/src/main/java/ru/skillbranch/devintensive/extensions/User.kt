package ru.skillbranch.devintensive.extensions




import ru.skillbranch.devintensive.models.User
import ru.skillbranch.devintensive.models.UserView
import ru.skillbranch.devintensive.utils.Utils



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

