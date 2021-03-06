package hk.edu.hkbu.comp.lab01.json

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Thread(
        val thread_id: String,
        val cat_id: String,
        val sub_cat_id: String,
        val title: String,
        val user_id: String,
        val user_nickname: String,
        val user_gender: String,
        val no_of_reply: String,
        val no_of_uni_user_reply: String,
        val like_count: String,
        val dislike_count: String,
        val reply_like_count: String,
        val reply_dislike_count: String,
        val max_reply_like_count: String,
        val max_reply_dislike_count: String,
        val create_time: Long,
        val last_reply_time: Long,
        val status: String,
        val remark: Remark?,
        val last_reply_user_id: String,
        val max_reply: String,
        val total_page: Long,
        val is_adu: Boolean,
        val is_hot: Boolean,
        val category: Category?,
        val is_bookmarked: Boolean,
        val is_replied: Boolean,
        val user: User,
//        val sub_category: SubCategory
        val sub_category: SubCategory?,
        val page: String? = "1",
        var item_data: List<Post>? = mutableListOf<Post>()
) : Parcelable {

}