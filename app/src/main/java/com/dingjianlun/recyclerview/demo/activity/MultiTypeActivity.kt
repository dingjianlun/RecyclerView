package com.dingjianlun.recyclerview.demo.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dingjianlun.recyclerview.SimpleAdapter
import com.dingjianlun.recyclerview.demo.R
import com.dingjianlun.recyclerview.demo.databinding.ListItem2Binding
import com.dingjianlun.recyclerview.linearLayout
import kotlinx.android.synthetic.main.list_item_1.view.*
import kotlinx.android.synthetic.main.multi_type_activity.*

class MultiTypeActivity : AppCompatActivity() {

    open class BaseItem
    data class Item1(val id: Int, var name: String) : BaseItem()
    data class Item2(val id: Int, val name: String) : BaseItem()

    private val adapter = SimpleAdapter<BaseItem> {

        addItem<Item1>(
            R.layout.list_item_1
        ) { item, holder ->
            tv_text.text = ("${item.id}: ${item.name}")
            setOnClickListener {
                Toast.makeText(context, "${holder.adapterPosition}", Toast.LENGTH_SHORT).show()
            }
        }

//        addItem<Item2>(R.layout.list_item_2) { item, holder ->
//            tv_text.text = ("${item.id}: ${item.name}")
//            setOnClickListener {
//                Toast.makeText(context, "${holder.adapterPosition}", Toast.LENGTH_SHORT).show()
//            }
//        }

        addItem<Item2> {
            //使用DataBinding
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
        setContentView(R.layout.multi_type_activity)

        recyclerView.linearLayout().adapter = adapter

        val list = (0..100)
            .map {
                if (it % 2 == 0) Item1(
                    it,
                    "name$it"
                ) else Item2(
                    it,
                    "name$it"
                )
            }
        adapter.setData(list)

    }


}