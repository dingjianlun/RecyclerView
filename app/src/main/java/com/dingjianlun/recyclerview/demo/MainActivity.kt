package com.dingjianlun.recyclerview.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dingjianlun.recyclerview.SimpleAdapter
import com.dingjianlun.recyclerview.demo.databinding.ListItem2Binding
import com.dingjianlun.recyclerview.linearLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item_1.view.*

class MainActivity : AppCompatActivity() {

    private val adapter = SimpleAdapter<BaseItem> {

        addItem<Item1>(R.layout.list_item_1) { item, holder ->
            tv_text.text = ("${item.id}: ${item.name}")
            setOnClickListener {
                Toast.makeText(context, "${holder.adapterPosition}", Toast.LENGTH_SHORT).show()
            }
        }

        addItem<Item2> {
            val dataBinding = ListItem2Binding.inflate(LayoutInflater.from(it.context), it, false)

            view = dataBinding.root
            onBind = { item, holder ->
                dataBinding.item = item
                dataBinding.executePendingBindings()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView
            .linearLayout()
            .adapter = adapter

        val data = (0..100)
            .map { if (it % 2 == 0) Item1(it, "name$it") else Item2(it, "name$it") }

        adapter.setData(data)

    }

    open class BaseItem
    data class Item1(val id: Int, var name: String) : BaseItem()
    data class Item2(val id: Int, val name: String) : BaseItem()
}