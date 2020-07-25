package com.dingjianlun.recyclerview.demo.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dingjianlun.recyclerview.SimpleAdapter
import com.dingjianlun.recyclerview.demo.R
import com.dingjianlun.recyclerview.linearLayout
import kotlinx.android.synthetic.main.list_item_1.view.*
import kotlinx.android.synthetic.main.single_type_activity.*

class SingleTypeActivity : AppCompatActivity() {

    private val adapter = SimpleAdapter<String>()
        .setItem(R.layout.list_item_1) { item ->
            tv_text.text = item
            setOnClickListener {
                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_type_activity)

        recyclerView.linearLayout().adapter = adapter


        val list: List<String> = (0..100).map { "item：$it" }
        adapter.setData(list)

    }

}