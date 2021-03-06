package hk.edu.hkbu.comp.lab01

import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import hk.edu.hkbu.comp.lab01.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import retrofit2.Call
import retrofit2.Callback
import android.content.Intent
import android.databinding.ViewDataBinding
import android.support.v4.view.MotionEventCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.text.Html
import android.view.*
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import java.math.BigInteger
import java.security.MessageDigest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import hk.edu.hkbu.comp.lab01.json.*
import kotlinx.android.synthetic.main.abc_screen_toolbar.*
import kotlinx.android.synthetic.main.design_navigation_menu_item.view.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding

    var current_page: Int = 1


    var current_category: String = "latest/page/"
    //    var current_category: Int = 1;
    var url_header: String = "/api_v2/thread/"

    var user_name: String = LIHKGService.user_name.toString()

    var user_id: String = LIHKGService.getUID()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)


        Log.d("Current Category: ", current_category.toString())

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.appBarMain?.contentMain?.centerText = "Mayanne: I love U"

        binding.appBarMain?.contentMain?.listViewModel = ListViewModel<Thread>(BR.threadItem, R.layout.content_main_item)

        val navViewBinding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, R.layout.nav_header_main, binding.navView, false)
        binding.navView.addHeaderView(navViewBinding.root)

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            //            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
            if (LIHKGService.get_login_check()) {
                LIHKGService.set_login_check(false)
                Toast.makeText(this@MainActivity, "恭喜你成功登出。", Toast.LENGTH_LONG).show()
                LIHKGService.user_name.set("CD-ROM")
                with(Intent(this, MainActivity::class.java)) {
                    startActivity(this)
                }
            } else {
                with(Intent(this, LoginActivity::class.java)) {
                    startActivity(this)
                }
            }

        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)



        val gestureDetector = GestureDetector(binding.root.context, object:GestureDetector.OnGestureListener{
            override fun onShowPress(e: MotionEvent?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return true
            }

            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                // ...
                Log.d("onFling","onFling")
                if (e1 == null || e2 == null)
                    return false

                if (e1.x > e2.x) {
                    Log.d("onFling","swipe from right")
                    getNextPage()
                } else {
                    Log.d("onFling","swipe from left")
                }
//                val SWIPE_THRESHOLD = 3
//
//                if (Math.abs(e2!!.getX() - e2!!.getX()) > SWIPE_THRESHOLD) {
//                    if (e1!!.getX() > e2!!.getX()) {
//                        Log.d("onFling","swipe from right")
//                        getNextPage()
//                    } else {
//                        Log.d("onFling","swipe from left")
//                    }
//                }

                return true
//                var result : Boolean = false;
//                try {
//                    var diffY:Float = e2!!.getY() - e1!!.getY();
//                    var diffX:Float = e2!!.getX() - e1!!.getX();
//                    val SWIPE_VELOCITY_THRESHOLD = 30
//                    val SWIPE_THRESHOLD = 30
//                    if (Math.abs(diffX) > Math.abs(diffY)) {
//                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                            if (diffX > 0) {
////                        onSwipeRight();
//                                Log.d("onFling","swipe Right")
//                                getNextPage()
//                            } else {
////                        onSwipeLeft();
//                                Log.d("onFling","swipe Left")
//
//                            }
//                            result = true;
//                        }
//                    }
//                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffY > 0) {
////                    onSwipeBottom();
//                            Log.d("onFling","swipe Down")
//
//                        } else {
////                    onSwipeTop();
//                            Log.d("onFling","swipe Up")
//
//                        }
//                        result = true;
//                    }
//                } catch (exception:Exception) {
//                    exception.printStackTrace();
//                }
//                return result;
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
//        binding.root.setOnTouchListener { v, event ->
//            gestureDetector.onTouchEvent(event)
//            binding.refreshThreadListLayout.onTouchEvent(event)
//            binding.navView.onTouchEvent(event)
//
//        }
        binding.appBarMain.contentMain.threadList.setOnTouchListener { v, event ->
            Log.d("onFling","refreshThreadListLayout.setOnTouchListener")
            gestureDetector.onTouchEvent(event)
        }
//        binding.root.setOnTouchListener { v, event ->
//            gestureDetector.onTouchEvent(event)
//            true
//        }

    }

    override fun onStart() {
        super.onStart()

        user_id = LIHKGService.getUID()

        if (LIHKGService.get_login_check()) {
            Log.d("testFab", "can come here----------------")
            fab.setImageResource(R.drawable.ic_logout)
        }

        binding.appBarMain.contentMain.refreshThreadListLayout.setOnRefreshListener {
            refreshThread()
        }

        refreshThread()

    }

    fun refreshThread() {
        Log.d("current_page", current_page.toString())
        Log.d("R.id.action_next_list-", current_category.toString())

        /*       if (current_category == 1) {
                   LIHKGService.instance.getLatestThread(current_page).enqueue(object : Callback<Response<ThreadList>> {
                       override fun onFailure(call: Call<Response<ThreadList>>, t: Throwable) {
                           Log.e("MainActivity", t.message)
                       }

                       override fun onResponse(call: Call<Response<ThreadList>>, response: retrofit2.Response<Response<ThreadList>>) {
                           if (response.isSuccessful) {
                               Log.d("Thread:", response.body().toString())
                               val threads = response.body()?.response?.items as List<Thread>
                               with(binding.appBarMain.contentMain.listViewModel?.items as ObservableArrayList<Thread>) {
                                   clear()
                                   addAll(threads)
                               }
                           }
                       }
                   })
               } else if (current_category == 2) {
                   LIHKGService.instance.getHotPost().enqueue(object : Callback<Response<ThreadList>> {
                       override fun onFailure(call: Call<Response<ThreadList>>, t: Throwable) {
                           Log.e("MainActivity", t.message)
                       }

                       override fun onResponse(call: Call<Response<ThreadList>>, response: retrofit2.Response<Response<ThreadList>>) {
                           if (response.isSuccessful) {
                               val threads = response.body()?.response?.items as List<Thread>
                               with(binding.appBarMain.contentMain.listViewModel?.items as ObservableArrayList<Thread>) {
                                   clear()
                                   addAll(threads)
                               }
                           }
                       }
                   })
               }else if(current_category == 5){
                   LIHKGService.instance.getNewsPost().enqueue(object : Callback<Response<ThreadList>> {
                       override fun onFailure(call: Call<Response<ThreadList>>, t: Throwable) {
                           Log.e("MainActivity", t.message)
                       }

                       override fun onResponse(call: Call<Response<ThreadList>>, response: retrofit2.Response<Response<ThreadList>>) {
                           if (response.isSuccessful) {
                               val threads = response.body()?.response?.items as List<Thread>
                               with(binding.appBarMain.contentMain.listViewModel?.items as ObservableArrayList<Thread>) {
                                   clear()
                                   addAll(threads)
                               }
                           }
                       }
                   })
               }else if(current_category == 31){
                   LIHKGService.instance.getCreativePost().enqueue(object : Callback<Response<ThreadList>> {
                       override fun onFailure(call: Call<Response<ThreadList>>, t: Throwable) {
                           Log.e("MainActivity", t.message)
                       }

                       override fun onResponse(call: Call<Response<ThreadList>>, response: retrofit2.Response<Response<ThreadList>>) {
                           if (response.isSuccessful) {
                               val threads = response.body()?.response?.items as List<Thread>
                               with(binding.appBarMain.contentMain.listViewModel?.items as ObservableArrayList<Thread>) {
                                   clear()
                                   addAll(threads)
                               }
                           }
                       }
                   })

               }else if(current_category == 22){
                   LIHKGService.instance.getHardwarePost().enqueue(object : Callback<Response<ThreadList>> {
                       override fun onFailure(call: Call<Response<ThreadList>>, t: Throwable) {
                           Log.e("MainActivity", t.message)
                       }

                       override fun onResponse(call: Call<Response<ThreadList>>, response: retrofit2.Response<Response<ThreadList>>) {
                           if (response.isSuccessful) {
                               val threads = response.body()?.response?.items as List<Thread>
                               with(binding.appBarMain.contentMain.listViewModel?.items as ObservableArrayList<Thread>) {
                                   clear()
                                   addAll(threads)
                               }
                           }
                       }
                   })
               } else if (current_category == 18) {
                   LIHKGService.instance.getAcademicPost().enqueue(object : Callback<Response<ThreadList>> {
                       override fun onFailure(call: Call<Response<ThreadList>>, t: Throwable) {
                           Log.e("MainActivity", t.message)
                       }

                       override fun onResponse(call: Call<Response<ThreadList>>, response: retrofit2.Response<Response<ThreadList>>) {
                           if (response.isSuccessful) {
                               val threads = response.body()?.response?.items as List<Thread>
                               with(binding.appBarMain.contentMain.listViewModel?.items as ObservableArrayList<Thread>) {
                                   clear()
                                   addAll(threads)
                               }
                           }
                       }
                   })
               }else if(current_category == 29){
                   LIHKGService.instance.getChildrenPost().enqueue(object : Callback<Response<ThreadList>> {
                       override fun onFailure(call: Call<Response<ThreadList>>, t: Throwable) {
                           Log.e("MainActivity", t.message)
                       }

                       override fun onResponse(call: Call<Response<ThreadList>>, response: retrofit2.Response<Response<ThreadList>>) {
                           if (response.isSuccessful) {
                               val threads = response.body()?.response?.items as List<Thread>
                               with(binding.appBarMain.contentMain.listViewModel?.items as ObservableArrayList<Thread>) {
                                   clear()
                                   addAll(threads)
                               }
                           }
                       }
                   })
               } else if (current_category == 32) {
                   LIHKGService.instance.getBlackHolePost().enqueue(object : Callback<Response<ThreadList>> {
                       override fun onFailure(call: Call<Response<ThreadList>>, t: Throwable) {
                           Log.e("MainActivity", t.message)
                       }

                       override fun onResponse(call: Call<Response<ThreadList>>, response: retrofit2.Response<Response<ThreadList>>) {
                           if (response.isSuccessful) {
                               val threads = response.body()?.response?.items as List<Thread>
                               with(binding.appBarMain.contentMain.listViewModel?.items as ObservableArrayList<Thread>) {
                                   clear()
                                   addAll(threads)
                               }
                           }
                       }
                   })
               }*/
        Log.d("current_category_test", "test1 " + user_id)
        if (current_category.equals("0")) {
            Log.d("current_category_test", "test2")

            setTitle("偉大嘅紅登討論區-留底")
            fab.hide()

//            binding.appBarMain?.contentMain?.listViewModel?.items
            Log.d("current_category", user_id)
            FirebaseFirestore.getInstance().collection(sha512(user_id))
                    .get()
                    .addOnSuccessListener {
                        //                        Log.d("current_category",it.toObjects(Thread::class.java).toString())
                        with(binding.appBarMain.contentMain.listViewModel?.items as ObservableArrayList<Thread>) {
                            clear()
                            for (doc in it.documents) {
                                val remark = doc["remark"] as HashMap<String, Int>
                                val user = doc["user"] as java.util.HashMap<String, Any>
                                val postsData = doc["item_data"] as ArrayList<HashMap<String, Any>>

                                val posts = mutableListOf<Post>()
                                for (postData in postsData) {
                                    val post_user = postData["user"] as java.util.HashMap<String, Any>
                                    posts.add(Post(
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

                                add(hk.edu.hkbu.comp.lab01.json.Thread(

                                        doc["thread_id"] as String,
                                        doc["cat_id"] as String,
                                        doc["sub_cat_id"] as String,
                                        doc["title"] as String,
                                        doc["user_id"] as String,
                                        doc["user_nickname"] as String,
                                        doc["user_gender"] as String,
                                        doc["no_of_reply"] as String,
                                        doc["no_of_uni_user_reply"] as String,
                                        doc["like_count"] as String,
                                        doc["dislike_count"] as String,
                                        doc["reply_like_count"] as String,
                                        doc["reply_dislike_count"] as String,
                                        doc["max_reply_like_count"] as String,
                                        doc["max_reply_dislike_count"] as String,
                                        doc["create_time"] as Long,
                                        doc["last_reply_time"] as Long,
                                        doc["status"] as String,
                                        Remark(remark["last_reply_count"] as Long),
                                        doc["last_reply_user_id"] as String,
                                        doc["max_reply"] as String,
                                        doc["total_page"] as Long,
                                        doc["_adu"] as Boolean,
                                        doc["_hot"] as Boolean,
                                        null,
                                        doc["_bookmarked"] as Boolean,
                                        doc["_replied"] as Boolean,
                                        User(user["user_id"] as String,
                                                user["nickname"] as String,
                                                user["level"] as String,
                                                user["gender"] as String,
                                                user["level"] as String,
                                                user["create_time"] as Long,
                                                user["level_name"] as String,
                                                user["_following"] as Boolean,
                                                user["_blocked"] as Boolean,
                                                user["_disappear"] as Boolean),
                                        null,
                                        doc["page"] as String?,
                                        posts


                                ))
//                            doc["category"].
//                            Log.d("current_category", doc.toObject(Thread::class.java).toString())

                            }
                            binding.appBarMain.contentMain.refreshThreadListLayout.isRefreshing = false

                        }

                        Log.d("current_category", binding.appBarMain.contentMain.listViewModel?.items.toString())
//                        with(binding.appBarMain.contentMain.listViewModel?.items as ObservableArrayList<Thread>) {
//                            clear()
//                            val threads = it.toObjects(Thread::class.java)
//                            this.addAll(threads)
////                            for (doc in it.toObjects()) {
////                                this.add(doc.toObject())
////                            }
//                        }

                    }

//            (object:OnCompleteListener<QuerySnapshot>() {
//                        if (it.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                                }
//                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//              })
        } else {
            fab.show()
            LIHKGService.instance.getUrlThread(url_header + current_category + current_page).enqueue(object : Callback<Response<ThreadList>> {
                override fun onFailure(call: Call<Response<ThreadList>>, t: Throwable) {
                    Log.e("MainActivity", t.message)
                }

                override fun onResponse(call: Call<Response<ThreadList>>, response: retrofit2.Response<Response<ThreadList>>) {
                    if (response.isSuccessful) {
                        if (response.body()?.response?.category?.cat_id.equals("29")) {
                            setTitle("紅登或成為最大贏家")
                        } else {
                            setTitle("偉大嘅紅登討論區-${"${response.body()?.response?.category?.name}"}")
                        }
                        val threads = response.body()?.response?.items as List<Thread>
                        with(binding.appBarMain.contentMain.listViewModel?.items as ObservableArrayList<Thread>) {
                            clear()
                            addAll(threads)
                        }
                        binding.appBarMain.contentMain.refreshThreadListLayout.isRefreshing = false
                    }
                }
            })
        }
    }

    fun createPost() {
        if (LIHKGService.get_login_check()) {
            with(Intent(this, CreateActivity    ::class.java)) {
                startActivity(this)
            }
        } else {
            Toast.makeText(this@MainActivity, "未登入學咩人出Post。", Toast.LENGTH_SHORT).show()
        }

    }

    fun getNextPage() {
        if (current_category != "hot/page/" && current_category != "latest/page/") {
            current_page += 1
            refreshThread()
        } else {
            Toast.makeText(this@MainActivity, "最後一頁啦", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
//            R.id.action_refresh -> {
//                current_page = 1
//                refreshThread()
//                return true
//            }
            R.id.action_next_list -> {
                getNextPage()
                return true
            }
            R.id.action_createPost -> {
                createPost()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        current_page = 1;

        when (item.itemId) {

            R.id.nav_catergory_saved -> {
                // Handle the lm action
                current_category = "0"
                refreshThread()
            }
            R.id.nav_catergory_hot -> {
//                current_category = 2;
                current_category = "hot/page/"
                refreshThread()

            }
            R.id.nav_catergory_chat -> {
                current_category = "latest/page/"
//                current_category = 1;
                refreshThread()

            }
            R.id.nav_catergory_news -> {
                current_category = "category?cat_id=5&page="
//                current_category = 5;
                refreshThread()

            }
            R.id.nav_catergory_creative -> {
                current_category = "category?cat_id=31&page="
//                current_category = 31;
                refreshThread()
            }
            R.id.nav_catergory_hardware -> {
                current_category = "category?cat_id=22&page="
//                current_category = 22;
                refreshThread()

            }
            R.id.nav_catergory_academic -> {
                current_category = "category?cat_id=18&page="
//                current_category = 18;
                refreshThread()
            }
            R.id.nav_catergory_children -> {
//                current_category = "category?cat_id=29&page="
////                current_category = 29;
//                refreshThread()
                showChildrenThread()

            }
            R.id.nav_catergory_blackhole -> {
                current_category = "category?cat_id=32&page="
//                current_category = 32;
                refreshThread()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showChildrenThread() {
        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog


        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)

        // Set a title for alert dialog
        builder.setTitle(Html.fromHtml("<b>" + "WARNING" + "</b>"));

        // Set a message for alert dialog
        builder.setMessage(Html.fromHtml("<b>" + "有冇人喺你隔離" + "</b>"));


        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    nav_view.setCheckedItem(R.id.nav_catergory_chat)
                    current_category = "latest/page/1"
                    refreshThread()
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    current_category = "category?cat_id=29&page="
//                current_category = 29;
                    refreshThread()
                }
//                DialogInterface.BUTTON_NEUTRAL -> toast("Neutral/Cancel button clicked.")
            }
        }


        // Set the alert dialog positive/yes button
        builder.setPositiveButton(Html.fromHtml("<b>" + "有" + "</b>"), dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNegativeButton(Html.fromHtml("<b>" + "冇" + "</b>"), dialogClickListener)

        // Set the alert dialog neutral/cancel button
//        builder.setNeutralButton("CANCEL",dialogClickListener)


        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

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


    fun showThreadActivity(thread: Thread) {
        with(Intent(this, ThreadActivity::class.java)) {
            putExtra("thread", thread)
            if (current_category.equals("0")) {
                putExtra("show_saved", true)
            }
            startActivity(this)
            overridePendingTransition(R.anim.child_enter, R.anim.parent_exit)
        }
    }
}
