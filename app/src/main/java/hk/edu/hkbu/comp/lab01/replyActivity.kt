package hk.edu.hkbu.comp.lab01

import android.Manifest
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import hk.edu.hkbu.comp.lab01.json.Login
import hk.edu.hkbu.comp.lab01.json.Response
import kotlinx.android.synthetic.main.activity_login.*

import kotlinx.android.synthetic.main.activity_reply.*
import kotlinx.android.synthetic.main.content_reply.*
import retrofit2.Call
import android.support.annotation.NonNull
import android.view.MenuItem
import android.view.View
import android.text.Selection.getSelectionEnd
import android.text.Selection.getSelectionStart
import android.R.id.edit
import android.support.design.internal.BottomNavigationItemView
import android.widget.EditText
import android.R.id.edit




class replyActivity : AppCompatActivity() {

    var thread_id = ""

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        val content = user_comment.text.toString()

        val et = findViewById<View>(R.id.user_comment) as EditText
        val startSelection = et.selectionStart
        val endSelection = et.selectionEnd
        val selectedText = et.text.toString().substring(startSelection, endSelection)

        when (item.itemId) {
            R.id.action_bold -> {
                if(selectedText.equals("")){

                }
            }
            R.id.action_italic -> {
                Toast.makeText(this@replyActivity, "selectedText: " + selectedText, Toast.LENGTH_LONG).show()

            }
            R.id.action_underline -> {


            }
            R.id.action_align_left -> {


            }
            R.id.action_align_center -> {


            }
            R.id.action_align_right -> {
                Toast.makeText(this@replyActivity, "content: " + content, Toast.LENGTH_LONG).show()

            }
            R.id.action_member -> {
                Toast.makeText(this@replyActivity, "selectedText: " + selectedText, Toast.LENGTH_LONG).show()

            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle("Comment")

        setContentView(R.layout.activity_reply)

        thread_id = getIntent().getExtras().getString("current_thread")

        Log.d("thread_ID", thread_id)

//        Toast.makeText(this@replyActivity, "${LIHKGService.getUID()} and ${LIHKGService.getToken()}", Toast.LENGTH_LONG).show()

        fab.setOnClickListener { view ->

            if (attemptComment()) {
//                Toast.makeText(this@replyActivity, "on99 ar dllm.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@replyActivity, "Cannot detect any text.", Toast.LENGTH_SHORT).show()
            }

        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.reply_appbar)
        val bottomNavigation_2: BottomNavigationView = findViewById(R.id.reply_appbar_2)

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation_2.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun attemptComment(): Boolean {

        val content = user_comment.text.toString()

        Log.d("current", thread_id)


        val userId = LIHKGService.getUID()
        val timestamp = LIHKGService.timestamp(true)
        val digest = LIHKGService.replyDigest(thread_id, content)

        Log.d("whatiwant", "toke = ${LIHKGService.getToken()} thread_id = ${thread_id} content = ${content} digest = ${digest} userID = ${userId} timestamp = ${timestamp}")

        if (!content.equals("")) {
            LIHKGService.instance.reply(thread_id, content, digest, userId, timestamp).enqueue(object : retrofit2.Callback<Response<Any>> {
                override fun onFailure(call: Call<Response<Any>>, t: Throwable) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                }

                override fun onResponse(call: Call<Response<Any>>, response: retrofit2.Response<Response<Any>>) {

                    Log.d("response", response.body().toString())

//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    if (response.body()?.success?.equals(1)!!) {
                        Toast.makeText(this@replyActivity, "Commented.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@replyActivity, "Failed to comment.", Toast.LENGTH_SHORT).show()
                    }
                }

            })
            return true
        } else {
            return false
        }

    }

}
