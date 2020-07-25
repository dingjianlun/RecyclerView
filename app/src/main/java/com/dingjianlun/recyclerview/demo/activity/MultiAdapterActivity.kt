package com.dingjianlun.recyclerview.demo.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dingjianlun.recyclerview.SimpleAdapter
import com.dingjianlun.recyclerview.demo.R
import com.dingjianlun.recyclerview.linearLayout
import com.dingjianlun.recyclerview.setAdapter
import kotlinx.android.synthetic.main.list_item_1.view.*
import kotlinx.android.synthetic.main.multi_adapter_activity.*

class MultiAdapterActivity : AppCompatActivity() {

    private val adapter1 = SimpleAdapter<String>()
        .setItem(R.layout.list_item_1) { item ->
            tv_text.text = ("adapter1：$item")
        }

    private val adapter2 = SimpleAdapter<Int>()
        .setItem(R.layout.list_item_2) { item ->
            tv_text.text = ("adapter2：$item")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.multi_adapter_activity)

        recyclerView.linearLayout().setAdapter(adapter1, adapter2)


        adapter1.setData((0..10).map { it.toString() })
        adapter2.setData((11..20).map { it })

    }

}