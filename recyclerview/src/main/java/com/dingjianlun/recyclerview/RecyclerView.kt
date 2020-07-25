package com.dingjianlun.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

//import com.google.android.flexbox.FlexDirection
//import com.google.android.flexbox.FlexWrap
//import com.google.android.flexbox.FlexboxLayoutManager


fun <T : RecyclerView> T.linearLayout(
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) = apply { layoutManager = LinearLayoutManager(context, orientation, reverseLayout) }

fun RecyclerView.gridLayout(
    spanCount: Int,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    getSpanSize: (position: Int) -> Int = { 1 },
    reverseLayout: Boolean = false
) = apply {
    layoutManager = GridLayoutManager(context, spanCount, orientation, reverseLayout)
        .apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int = getSpanSize.invoke(position)
            }
        }
}

fun RecyclerView.staggeredGridLayout(
    spanCount: Int,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL
) = apply { layoutManager = StaggeredGridLayoutManager(spanCount, orientation) }

//fun RecyclerView.flexboxLayout(
//    @FlexDirection flexDirection: Int = FlexDirection.ROW,
//    @FlexWrap flexWrap: Int = FlexWrap.WRAP
//) = apply { layoutManager = FlexboxLayoutManager(context, flexDirection, flexWrap) }

fun RecyclerView.decoration(
    itemDecoration: ItemDecoration
) = apply { addItemDecoration(itemDecoration) }

fun RecyclerView.setAdapter(vararg adapterList: SimpleAdapter<*>) {
    this.adapter = MultiAdapter(*adapterList)
}

fun RecyclerView.setAdapter(paging: Paging<*, *>) {
    this.adapter = paging.adapter
}