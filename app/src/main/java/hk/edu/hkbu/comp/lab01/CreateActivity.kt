package hk.edu.hkbu.comp.lab01

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Spinner
import hk.edu.hkbu.comp.lab01.R.id.spinner1
import hk.edu.hkbu.comp.lab01.databinding.ActivityCreateBinding

import kotlinx.android.synthetic.main.activity_create.*

class CreateActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create)

        setContentView(R.layout.activity_create)
        setSupportActionBar(toolbar)

//        var dropdown = findViewById<View>(R.id.spinner1)
        //val items = mutableListOf<String>("吹水台", "時事台", "創意台", "硬件台", "學術台", "兒童台", "黑洞")
        //val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items)
        //binding.contentCreate.spinner1.adapter = adapter

        val spinner: Spinner = findViewById(R.id.spinner1)
        ArrayAdapter.createFromResource(
                this, R.array.dropDownList, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

        }

    }

}
