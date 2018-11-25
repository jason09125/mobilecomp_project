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
import android.view.Menu
import android.view.MenuItem
import hk.edu.hkbu.comp.lab01.databinding.ActivityMainBinding
import hk.edu.hkbu.comp.lab01.json.Response
import hk.edu.hkbu.comp.lab01.json.ThreadList
import hk.edu.hkbu.comp.lab01.json.Thread
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import retrofit2.Call
import retrofit2.Callback
import android.content.Intent
import android.support.v4.view.MotionEventCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.text.Html
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import java.math.BigInteger
import java.security.MessageDigest
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding

    var current_page: Int = 1


    var current_category: String = "latest/page/"
    //    var current_category: Int = 1;
    var url_header: String = "/api_v2/thread/"

    var user_name: String = "Guest";

    private val refreshThreadListListener = SwipeRefreshLayout.OnRefreshListener{
        // 模擬加載時間
//        Thread.sleep(200)

       refreshThread()
        java.lang.Thread.sleep(200)
        refreshThreadListLayout.isRefreshing = false

    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)


        Log.d("Current Category: ", current_category.toString())

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.appBarMain?.contentMain?.centerText = "Mayanne: I love U"

        binding.appBarMain?.contentMain?.listViewModel = ListViewModel<Thread>(BR.threadItem, R.layout.content_main_item)

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            //            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
            with(Intent(this, LoginActivity::class.java)) {
                startActivity(this)
            }
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        refreshThread()
        refreshThreadListLayout.setOnRefreshListener(refreshThreadListListener)

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
        if (current_category.equals("0")) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("users_saved_thread/${user_name.md5()}")
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val value = dataSnapshot.getValue(String::class.java)
                    Log.d("Value is: ", value!!)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("Failed to read value.", error.toException())
                }
            })
        } else {
            LIHKGService.instance.getUrlThread(url_header + current_category + current_page).enqueue(object : Callback<Response<ThreadList>> {
                override fun onFailure(call: Call<Response<ThreadList>>, t: Throwable) {
                    Log.e("MainActivity", t.message)
                }

                override fun onResponse(call: Call<Response<ThreadList>>, response: retrofit2.Response<Response<ThreadList>>) {
                    if (response.isSuccessful) {
                        if(response.body()?.response?.category?.cat_id.equals("29")){
                            setTitle("紅登或成為最大贏家")
                        }else {
                            setTitle("偉大嘅紅登討論區-${"${response.body()?.response?.category?.name}"}")
                        }
                        val threads = response.body()?.response?.items as List<Thread>
                        with(binding.appBarMain.contentMain.listViewModel?.items as ObservableArrayList<Thread>) {
                            clear()
                            addAll(threads)
                        }
                    }
                }
            })
        }
    }


    fun getNextPage() {
        if (current_category != "hot/page/" && current_category != "latest/page/") {
            current_page += 1
        } else {
            Toast.makeText(this@MainActivity, "This is the last page", Toast.LENGTH_SHORT).show()

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
            R.id.action_refresh -> {
                current_page = 1
                refreshThread()
                return true
            }
            R.id.action_next_list -> {
                getNextPage()
                refreshThread()
                return true
            }
            R.id.action_next_list -> {
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

    private fun showChildrenThread(){
        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog


        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this,R.style.AlertDialogStyle)

        // Set a title for alert dialog
        builder.setTitle(Html.fromHtml("<b>"+"WARNING"+"</b>"));

        // Set a message for alert dialog
        builder.setMessage(Html.fromHtml("<b>"+"有冇人喺你隔離"+"</b>"));


        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    nav_view.setCheckedItem(R.id.nav_catergory_chat)
                    current_category="latest/page/1"
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
        builder.setPositiveButton(Html.fromHtml("<b>"+"有"+"</b>"),dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNegativeButton(Html.fromHtml("<b>"+"冇"+"</b>"),dialogClickListener)

        // Set the alert dialog neutral/cancel button
//        builder.setNeutralButton("CANCEL",dialogClickListener)


        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }


    fun showThreadActivity(thread: Thread) {
        with(Intent(this, ThreadActivity::class.java)) {
            putExtra("thread", thread)
            startActivity(this)
            overridePendingTransition(R.anim.child_enter, R.anim.parent_exit)
        }
    }

    fun showPostActivity(thread: Thread) {
        with(Intent(this, ThreadActivity::class.java)) {
            putExtra("thread", thread)
            startActivity(this)
            overridePendingTransition(R.anim.child_enter, R.anim.parent_exit)
        }
    }

    fun showProfileActivity(view: View) {

    }
}
