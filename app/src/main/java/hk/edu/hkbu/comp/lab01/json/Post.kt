package hk.edu.hkbu.comp.lab01.json
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
        val post_id: String,
        val quote_post_id: String,
        val thread_id: String,
        val user_nickname: String,
        val user_gender: String,
        val like_count: String,
        val dislike_count: String,
        val vote_score: String,
        val no_of_quote: String,
        val status: String,
        val reply_time: Long,
        val msg: String,
        val user: User,
        val page: Long,
        val msg_num: Long
) : Parcelable