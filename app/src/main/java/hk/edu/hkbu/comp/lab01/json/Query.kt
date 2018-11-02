package hk.edu.hkbu.comp.lab01.json

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Query(
        val cat_id: String,
        val sub_cat_id: String
) : Parcelable