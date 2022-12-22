package com.example.thingsi.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Post(
    var postId: String? = null,
    var postName: String? = null,
    var postText: String? = null
): Parcelable
