package hk.edu.hkbu.comp.lab01

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
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding

    var current_page: Int = 1


    var current_category: String = "latest/page/"
//    var current_category: Int = 1;
    var url_header: String = "/api_v2/thread/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        setTitle("偉大嘅紅登討論區")
        Log.d("mainact", current_category.toString())

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

        LIHKGService.instance.getUrlThread(url_header+current_category+current_page).enqueue(object : Callback<Response<ThreadList>> {
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
    }


    fun getNextPage(){
        if(current_category!="hot/page/" && current_category!="latest/page/")
        {
            current_page+=1
        }
        else
        {
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
                current_page=1
                refreshThread()
                return true
            }
            R.id.action_next_list->{
                getNextPage()
                refreshThread()
                return true
            }
            R.id.action_next_list ->{
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        current_page = 1;

        when (item.itemId) {

            R.id.nav_catergory_lm -> {
                // Handle the lm action
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
                current_category = "category?cat_id=11&page="
//                current_category = 18;
                refreshThread()
            }
            R.id.nav_catergory_children -> {
                current_category = "category?cat_id=29&page="
//                current_category = 29;
                refreshThread()

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
