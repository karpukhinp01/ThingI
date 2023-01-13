package com.example.thingsisee.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User (
    var uid: String? = null,
    var pic: String? = null
    ): Parcelable