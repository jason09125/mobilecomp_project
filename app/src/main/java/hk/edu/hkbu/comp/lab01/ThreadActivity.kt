package hk.edu.hkbu.comp.lab01

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import hk.edu.hkbu.comp.lab01.databinding.ActivityThreadBinding
import hk.edu.hkbu.comp.lab01.json.Thread

import kotlinx.android.synthetic.main.activity_thread.*
import hk.edu.hkbu.comp.lab01.databinding.*
import hk.edu.hkbu.comp.lab01.json.Post
import io.github.yavski.fabspeeddial.FabSpeedDial
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ThreadActivity : AppCompatActivity() {

    lateinit var thread: Thread
    val channelPosts = Channel<List<Post>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityThreadBinding = DataBindingUtil.setContentView(this, R.layout.activity_thread)
        setSupportActionBar(toolbar)

        // original fab function
        binding.fabMenu.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {
                when (menuItem?.itemId) {
                    R.id.action_comment -> {

                    }
                    R.id.action_save -> {
                        FirebaseFirestore.getInstance().collection("thread").add(thread).addOnSuccessListener {
                            Snackbar.make(binding.root, "Thread saved", Snackbar.LENGTH_LONG)
                                    .setAction("Done", null).show()
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
        getSupportActionBar()?.setTitle(thread.title)

        if (thread.item_data.isNullOrEmpty())
            thread.item_data = ObservableArrayList<Post>()

        binding.contentThread.itemBinding = ItemBinding.of<Post>(BR.post, R.layout.content_thread_item)
        binding.contentThread.posts = thread.item_data as? ObservableArrayList<Post>
        binding.contentThread.numPosts = thread.no_of_reply.toInt()

        fetchThreadPosts()

        GlobalScope.launch(Dispatchers.Main) {
            repeat(thread.total_page) {
                binding.contentThread.posts?.addAll(channelPosts.receive())
                binding.contentThread.executePendingBindings()
            }
        }
    }

    fun fetchThreadPosts() = GlobalScope.launch(Dispatchers.Default) {
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
