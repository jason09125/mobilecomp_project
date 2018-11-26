package hk.edu.hkbu.comp.lab01.json

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Remark(
        val last_reply_count: Long
) : Parcelable