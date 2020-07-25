package com.dingjianlun.recyclerview.demo.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dingjianlun.recyclerview.Paging
import com.dingjianlun.recyclerview.SimpleAdapter
import com.dingjianlun.recyclerview.demo.R
import com.dingjianlun.recyclerview.demo.data.RemoteData
import com.dingjianlun.recyclerview.linearLayout
import com.dingjianlun.recyclerview.setAdapter
import kotlinx.android.synthetic.main.list_item_1.view.*
import kotlinx.android.synthetic.main.list_item_more.view.*
import kotlinx.android.synthetic.main.paging_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class PagingActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private val adapter = SimpleAdapter<String>()
        .setItem(R.layout.list_item_1) { item ->
            tv_text.text = item
            setOnClickListener {
                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
            }
        }

    private val paging = Paging(
        this,
        adapter,
        object : Paging.UI() {

            override val layoutId: Int = R.layout.list_item_more

            override val update: View.(state: Paging.State) -> Unit = { state ->
                v_loading.isVisible = state is Paging.Loading && !state.isFirstPage
                v_error.isVisible = state is Paging.Error
                v_noMore.isVisible = state is Paging.NoMore
                v_error.setOnClickListener { if (state is Paging.Error) state.retry.invoke() }
            }

            override var updateState: ((state: Paging.State) -> Unit)? = { state ->
                swipeRefreshLayout.isRefreshing = state is Paging.Loading && state.isFirstPage
            }

            private var View.isVisible
                get() = visibility == View.VISIBLE
                set(value) {
                    visibility = if (value) View.VISIBLE else View.GONE
                }
        },
        1
    ) { key ->
        val data = RemoteData.getList(key)
        Paging.Result(data, key + 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paging_activity)

        recyclerView.linearLayout().setAdapter(paging)

        paging.initPage()

        swipeRefreshLayout.setOnRefreshListener { paging.refresh() }

    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}