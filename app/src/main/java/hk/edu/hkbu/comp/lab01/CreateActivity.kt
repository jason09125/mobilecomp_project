package hk.edu.hkbu.comp.lab01

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.View
import android.widget.*
import hk.edu.hkbu.comp.lab01.R.id.content
import hk.edu.hkbu.comp.lab01.R.id.spinner1
import hk.edu.hkbu.comp.lab01.databinding.ActivityCreateBinding
import hk.edu.hkbu.comp.lab01.json.Response

import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.content_create.*
import retrofit2.Call


class CreateActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateBinding
    var selectedToy = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create)

        setContentView(R.layout.activity_create)
        //setSupportActionBar(toolbar)

//        var dropdown = findViewById<View>(R.id.spinner1)
        //val items = mutableListOf<String>("吹水台", "時事台", "創意台", "硬件台", "學術台", "兒童台", "黑洞")
        //val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items)
        //binding.contentCreate.spinner1.adapter = adapter

        val spinner: Spinner = findViewById(R.id.spinner1)
        ArrayAdapter.createFromResource(
                this, R.array.dropDownList, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    selectedToy = parent?.getItemAtPosition(position).toString()
                    Log.d("selectedToy!!!!!!!!!!!!!!!!!!!!!!!!!!",selectedToy)
                }

            }
        }

        create_fab.setOnClickListener { view ->
            val title = create_title.text.toString()
            val content = create_content.text.toString()



            if (!title.equals("") && !content.equals("")) {
                Log.d("CreateTitle", title)
                Log.d("CreateContent", content)

                val userId = LIHKGService.getUID()
                val timestamp = LIHKGService.timestamp(true)
                val digest = LIHKGService.createDigest(5, title, content, 0)

                LIHKGService.instance.create(5, title, content, 0, digest, userId, timestamp).enqueue(object : retrofit2.Callback<Response<Any>> {
                    override fun onFailure(call: Call<hk.edu.hkbu.comp.lab01.json.Response<Any>>, t: Throwable) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                    }

                    override fun onResponse(call: Call<Response<Any>>, response: retrofit2.Response<Response<Any>>) {

                        Log.d("response", response.toString())
                        Log.d("response", response.headers().toString())

//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        if (response.body()?.success?.equals(1)!!) {
                            Toast.makeText(this@CreateActivity, "Post Created.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@CreateActivity, "Failed to create.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }


}
