package hk.edu.hkbu.comp.lab01

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import hk.edu.hkbu.comp.lab01.databinding.ActivityThreadBinding
import hk.edu.hkbu.comp.lab01.json.Thread

import kotlinx.android.synthetic.main.activity_thread.*
import hk.edu.hkbu.comp.lab01.json.Post
import hk.edu.hkbu.comp.lab01.json.Remark
import hk.edu.hkbu.comp.lab01.json.User
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.security.MessageDigest

class ThreadActivity : AppCompatActivity() {

    lateinit var thread: Thread
    val channelPosts = Channel<List<Post>>()
    var current_page: Int = 1;

    var user_name: String = "Guest";

    private val refreshThread = SwipeRefreshLayout.OnRefreshListener{
        // 模擬加載時間
//        Thread.sleep(200)
        refreshThreadLayout.setProgressViewOffset(true,0,100)

        fetchThreadPosts()
        java.lang.Thread.sleep(200)
        refreshThreadLayout.isRefreshing = false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityThreadBinding = DataBindingUtil.setContentView(this, R.layout.activity_thread)
        setSupportActionBar(toolbar)

        // original fab function
        binding.fabMenu.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {
                when (menuItem?.itemId) {
                    R.id.action_comment -> {
                        val intent = Intent(this@ThreadActivity, replyActivity::class.java).apply {
                            putExtra("current_thread", thread.thread_id)
                        }
                        startActivity(intent)
                    }
                    R.id.action_save -> {
                        Log.d("user_name", user_name)
                        Log.d("sha512(user_name)", sha512(user_name))

                        getAllThreadPosts()
                        GlobalScope.launch(Dispatchers.Main) {
                            repeat(thread.total_page.toInt()) {
                                binding.contentThread.posts?.addAll(channelPosts.receive())
                                binding.contentThread.executePendingBindings()
                            }
                        }

                        FirebaseFirestore.getInstance().collection("${sha512(user_name)}").document("${thread.thread_id}").set(thread).addOnSuccessListener {
                            Snackbar.make(binding.root, "Thread saved", Snackbar.LENGTH_LONG)
                                    .setAction("Done", null).show()
                        }

//                        FirebaseStorage.getInstance().reference.child("users_saved_thread/${user_name.md5()}/${thread.thread_id}.json")
//                                .putBytes(thread.toString().toByteArray()).addOnSuccessListener{
//
//                                }

                        current_page = 1

                        fetchThreadPosts()

                    }
                    R.id.action_next_page -> {
                        if (current_page < thread.total_page.toInt()) {
                            current_page += 1
                            fetchThreadPosts()
                        } else {
                            Toast.makeText(this@ThreadActivity, "It's the last page!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    R.id.action_prev_page -> {
                        if (current_page > 1) {
                            current_page -= 1
                            fetchThreadPosts()
                        } else {
                            Toast.makeText(this@ThreadActivity, "It's the first page!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> {

                    }
                }
                return super.onMenuItemSelected(menuItem)
            }
        })

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        thread = intent.extras["thread"] as Thread
        binding.contentThread.numPosts = thread.no_of_reply.toInt()

//        Log.d("onCreate",thread.thread_id)

        if (binding.contentThread.posts.isNullOrEmpty()) {
            binding.contentThread.posts = ObservableArrayList<Post>()
        }
        binding.contentThread.itemBinding = ItemBinding.of<Post>(BR.post, R.layout.content_thread_item)
        getSupportActionBar()?.setTitle(thread.title)

        if (intent.hasExtra("show_saved")) {
            FirebaseFirestore.getInstance().collection(sha512(user_name)).document("${thread.thread_id}")
                    .get()
                    .addOnSuccessListener {
                        //                        for (postData in it) {
//                        for (doc in it.documents) {
                        val remark = it["remark"] as HashMap<String, Int>
                        val user = it["user"] as java.util.HashMap<String, Any>
                        val postsData = it["item_data"] as ArrayList<HashMap<String, Any>>

                        binding.contentThread.posts.let {
                            for (postData in postsData) {
                                val post_user = postData["user"] as java.util.HashMap<String, Any>
                                it?.add(Post(
                                postData["post_id"] as String,
                                postData["quote_post_id"] as String,
                                postData["thread_id"] as String,
                                postData["user_nickname"] as String,
                                postData["user_gender"] as String,
                                postData["like_count"] as String,
                                postData["dislike_count"] as String,
                                postData["vote_score"] as String,
                                postData["no_of_quote"] as String,
                                postData["status"] as String,
                                postData["reply_time"] as Long,
                                postData["msg"] as String,

                                User(post_user["user_id"] as String,
                                        post_user["nickname"] as String,
                                        post_user["level"] as String,
                                        post_user["gender"] as String,
                                        post_user["level"] as String,
                                        post_user["create_time"] as Long,
                                        post_user["level_name"] as String,
                                        post_user["_following"] as Boolean,
                                        post_user["_blocked"] as Boolean,
                                        post_user["_disappear"] as Boolean),
                                postData["page"] as Long,
                                postData["msg_num"] as Long
                                ))
                            }

//                        binding.contentThread.posts = posts as? ObservableArrayList<Post>


//                            doc["category"].
//                            Log.d("current_category", doc.toObject(Thread::class.java).toString())

//                        }
                        }

                    }

        } else {

            getSupportActionBar()?.setTitle(thread.title)

            if (thread.item_data.isNullOrEmpty())
                thread.item_data = ObservableArrayList<Post>()

            binding.contentThread.itemBinding = ItemBinding.of<Post>(BR.post, R.layout.content_thread_item)
            binding.contentThread.posts = thread.item_data as? ObservableArrayList<Post>
            binding.contentThread.numPosts = thread.no_of_reply.toInt()

            fetchThreadPosts()
            refreshThreadLayout.setOnRefreshListener(refreshThread)


            GlobalScope.launch(Dispatchers.Main) {
                while (true) {
                    // here to seperate the pages
                    val arr = channelPosts.receive()
                    binding.contentThread.posts?.clear()
                    binding.contentThread.posts?.addAll(arr)
                    binding.contentThread.executePendingBindings()
                }
            }
//    Log.d("widthXXX","${getWindowManager().getDefaultDisplay().getWidth()}")
        }
    }

    fun getAllThreadPosts() = GlobalScope.launch(Dispatchers.Default) {
        for (i in 1..thread.total_page.toInt()) {
            launch(Dispatchers.IO) {
                val call = LIHKGService.instance.getThreadPosts(thread.thread_id, "$i", "msg_num").execute()
                if (call.isSuccessful) {
                    val posts = call.body()?.response?.item_data as List<Post>
                    channelPosts.send(posts)
                }
            }.join()
        }
    }

    fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }


//
//    fun String.md5(): String {
//        val md = MessageDigest.getInstance("MD5")
//        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
//    }

    fun sha512(input: String): String {
        val digest = MessageDigest.getInstance("SHA-512")
        val result = digest.digest(input.toByteArray())
        return toHex(result)
    }

    fun toHex(byteArray: ByteArray): String {

        val result = with(StringBuilder()) {
            byteArray.forEach {
                val value = it
                val hex = value.toInt() and (0xFF)
                val hexStr = Integer.toHexString(hex)
                //println(hexStr)
                if (hexStr.length == 1) {
                    //this.append("0").append(hexStr)
                    append("0").append(hexStr)
                } else {
                    //this.append(hexStr)
                    append(hexStr)
                }
            }
            this.toString()
        }
        return result

    }

    fun fetchThreadPosts() = GlobalScope.launch(Dispatchers.Default) {
//        for (i in 1..thread.total_page.toInt()) {

        Log.d("fetchThreadPosts current_page", current_page.toString())
            launch(Dispatchers.IO) {
                val call = LIHKGService.instance.getThreadPosts(thread.thread_id, "$current_page", "msg_num").execute()
                if (call.isSuccessful) {
                    val posts = call.body()?.response?.item_data as List<Post>
                    channelPosts.send(posts)
//                }
            }
//                        .join()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.parent_enter, R.anim.child_exit)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.parent_enter, R.anim.child_exit)
    }



}
