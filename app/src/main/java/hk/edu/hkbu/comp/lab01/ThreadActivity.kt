package hk.edu.hkbu.comp.lab01

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import hk.edu.hkbu.comp.lab01.databinding.ActivityThreadBinding
import hk.edu.hkbu.comp.lab01.json.Thread

import kotlinx.android.synthetic.main.activity_thread.*
import hk.edu.hkbu.comp.lab01.databinding.*

class ThreadActivity : AppCompatActivity() {

    lateinit var thread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            val binding: ActivityThreadBinding = DataBindingUtil.setContentView(this, R.layout.activity_thread)
        }

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        thread = intent.extras["thread"] as Thread
        getSupportActionBar()?.setTitle(thread.title)
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
