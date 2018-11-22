package hk.edu.hkbu.comp.lab01

import com.google.common.hash.Hashing
import hk.edu.hkbu.comp.lab01.json.Login
import hk.edu.hkbu.comp.lab01.json.Response
import hk.edu.hkbu.comp.lab01.json.ThreadList
import hk.edu.hkbu.comp.lab01.json.Thread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.nio.charset.Charset

interface LIHKGService {
    companion object {
        val instance: LIHKGService by lazy {
            Retrofit.Builder()
                    .baseUrl("https://lihkg.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(LIHKGService::class.java)
        }

        private var userId = ""
        private var token = ""
        fun setUserIdToken(userId:String, token:String) {
            this.userId = userId
            this.token = token
        }

        private var currentTimestamp = ""
        fun timestamp(generateNew:Boolean = false):String {
            if (generateNew)
                this.currentTimestamp = "${System.currentTimeMillis()/1000}"
            return currentTimestamp
        }

        fun replyDigest(thread_id:String, content:String):String {
            return Hashing.sha1().hashString("jeams\$post\$https://lihkg.com/api_v2/thread/reply\$thread_id=$thread_id&content=$content\$${this.token}\$$currentTimestamp", Charset.defaultCharset()).toString()
        }
    }

    @GET("/api_v2/thread/latest/page/1")
    fun getLatestThread(@Query("cat_id") cat_id: Int = 1): Call<Response<ThreadList>>

    @GET("/api_v2/thread/{thread_id}/page/{page}")
    fun getThreadPosts(@Path("thread_id") threadId: String, @Path("page") page: String, @Query("order") order: String = "reply_time"): Call<Response<Thread>>

    @FormUrlEncoded
    @POST("/api_v2/auth/login")
    fun login(@Field("email") email:String, @Field("password") password:String):Call<Response<Login>>

    @FormUrlEncoded
    @POST("/api_v2/thread/reply")
    fun reply(@Field("thread_id") thread_id:String, @Field("content") content:String, @Header("X-LI-DIGEST") digest:String, @Header("X-LI-USER") userId:String = Companion.userId, @Header("X-LI-TIMESTAMP") timestamp:String = Companion.timestamp()) :Call<Response<Any>>
    // thread_id=xxxxxx&content=xxxxx

    // reply("", "", LIHKGService.replyDigest("", ""))
}