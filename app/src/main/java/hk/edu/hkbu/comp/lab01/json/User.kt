package hk.edu.hkbu.comp.lab01.json

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        val user_id: String,
        val nickname: String,
        val level: String,
        val gender: String,
        val status: String,
        val create_time: Long,
        val level_name: String,
        val is_following: Boolean,
        val is_blocked: Boolean,
        val is_disappear: Boolean
)  : Parcelable