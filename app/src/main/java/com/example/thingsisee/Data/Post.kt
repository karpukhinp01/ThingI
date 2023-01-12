package com.example.thingsi.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.sql.Timestamp


@Parcelize
data class Post(
    var postId: String? = null,
    var timestamp: String? = null,
    var uid: String? = null,
    var postName: String? = null,
    var postText: String? = null
): Parcelable
/*либо главный ключ это юзерайди либо уникальный номер
*переделать*/

