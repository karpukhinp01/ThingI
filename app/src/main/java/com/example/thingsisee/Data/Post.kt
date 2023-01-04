package com.example.thingsi.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Post(
    var postId: String? = null,
    var postName: String? = null,
    var postText: String? = null
): Parcelable
/*либо главный ключ это юзерайди либо уникальный номер
*переделать*/

