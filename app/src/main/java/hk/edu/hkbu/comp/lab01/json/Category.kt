package hk.edu.hkbu.comp.lab01.json

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
        val cat_id: String,
        val name: String,
        val postable: Boolean
) : Parcelable