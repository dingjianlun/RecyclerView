package com.dingjianlun.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.linearLayout(
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) = apply { layoutManager = LinearLayoutManager(context, orientation, reverseLayout) }

fun RecyclerView.gridLayout(
    spanCount: Int,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) = apply { layoutManager = GridLayoutManager(context, spanCount, orientation, reverseLayout) }