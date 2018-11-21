package hk.edu.hkbu.comp.lab01

import hk.edu.hkbu.comp.lab01.json.Login
import hk.edu.hkbu.comp.lab01.json.Response
import hk.edu.hkbu.comp.lab01.json.ThreadList
import hk.edu.hkbu.comp.lab01.json.Thread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LIHKGService {
    companion object {
        val instance: LIHKGService by lazy {
            Retrofit.Builder()
                    .baseUrl("https://lihkg.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(LIHKGService::class.java)
        }
    }

    @GET("/api_v2/thread/latest?cat_id=1")
    fun getLatestThread(@Query("cat_id") cat_id: Int = 1): Call<Response<ThreadList>>

    @GET("/api_v2/thread/{thread_id}/page/{page}")
    fun getThreadPosts(@Path("thread_id") threadId: String, @Path("page") page: String, @Query("order") order: String = "reply_time"): Call<Response<Thread>>

    @GET("/api_v2/auth/login")
    fun login(@Body email:String, @Body password:String):Call<Response<Login>>
}