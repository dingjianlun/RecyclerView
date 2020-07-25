package com.dingjianlun.recyclerview

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class ItemDecoration(
    private val verticalSpace: Int,
    private val horizontalSpace: Int,
    private val includeEdge: Boolean = false
) : RecyclerView.ItemDecoration() {

    constructor(space: Int, includeEdge: Boolean = false) : this(space, space, includeEdge)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        val count: Int
        val orientation: Int
        val column: Int
        val isFirstGroup: Boolean

        parent.layoutManager.let {
            when (it) {
                is GridLayoutManager -> {
//                    count = it.spanCount
//                    column = position % count
                    orientation = it.orientation
                    val itemInfo = getItemInfo(it.spanSizeLookup, position, it.spanCount)
                    column = itemInfo.column - 1
                    count = itemInfo.count
                    isFirstGroup = itemInfo.isFirstGroup
                }
                is LinearLayoutManager -> {
                    count = 1
                    column = 0
                    orientation = it.orientation
                    isFirstGroup = position == 0
                }
                is StaggeredGridLayoutManager -> {
                    count = it.spanCount
                    column = position % count
                    orientation = it.orientation
                    isFirstGroup = position < count
                }
//                is FlexboxLayoutManager -> {
//                }
                else -> throw Exception()
            }
        }

        var vSpace = verticalSpace
        var hSpace = horizontalSpace

        if (orientation != RecyclerView.VERTICAL) {
            vSpace = hSpace.also { hSpace = vSpace }
        }

        if (includeEdge) {
            outRect.left = hSpace - (column * hSpace / count)
            outRect.right = (column + 1) * hSpace / count
            outRect.top = if (isFirstGroup) vSpace else 0
            outRect.bottom = vSpace
        } else {
            outRect.left = column * hSpace / count
            outRect.right = hSpace - (column + 1) * hSpace / count
            outRect.top = if (!isFirstGroup) vSpace else 0
            outRect.bottom = 0
        }

        if (orientation != RecyclerView.VERTICAL) {
            outRect.left = outRect.top.also { outRect.top = outRect.left }
            outRect.right = outRect.bottom.also { outRect.bottom = outRect.right }
        }

    }

    private fun getItemInfo(
        spanSizeLookup: GridLayoutManager.SpanSizeLookup,
        position: Int,
        spanCount: Int
    ): ItemInfo {
        val groupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount)

        var column = 0
        val count = (position - spanCount..position + spanCount).count {
            (it >= 0 && spanSizeLookup.getSpanGroupIndex(it, spanCount) == groupIndex).apply {
                if (this && it <= position) {
                    column++
                }
            }
        }
        return ItemInfo(column, count, groupIndex == 0)
    }

    private data class ItemInfo(val column: Int, val count: Int, val isFirstGroup: Boolean)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }
}