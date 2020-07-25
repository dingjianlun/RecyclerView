package com.dingjianlun.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal open class MultiAdapter(
    vararg adapterList: SimpleAdapter<*>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val adapterList: List<RecyclerView.Adapter<RecyclerView.ViewHolder>> =
        adapterList.map { it as RecyclerView.Adapter<RecyclerView.ViewHolder> }

    init {
        adapterList.forEachIndexed { index, adapter ->
            val adapterDataObserver = AdapterDataObserver {
                (0 until index)
                    .map { adapterList[it] }
                    .sumBy { it.itemCount }
            }
            adapter.registerAdapterDataObserver(adapterDataObserver)
        }
    }

    override fun getItemCount(): Int = adapterList.sumBy { it.itemCount }

    private val typeToAdapterMap = hashMapOf<Int, RecyclerView.Adapter<RecyclerView.ViewHolder>>()

    override fun getItemViewType(position: Int): Int {
        val map = adapterList.associateWith { it.itemCount }
        var p = position + 1
        val index = adapterList.indexOfFirst { p -= map.getValue(it); p <= 0 }
        val adapter = adapterList[index]
        val type = adapter.getItemViewType(p + map.getValue(adapter) - 1)
        typeToAdapterMap[type] = adapter
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return typeToAdapterMap[viewType]!!.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, arrayListOf())
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val map = adapterList.associateWith { it.itemCount }
        var p = position + 1
        val index = adapterList.indexOfFirst { p -= map.getValue(it); p <= 0 }
        val adapter = adapterList[index]
        adapter.onBindViewHolder(holder, p + map.getValue(adapter) - 1, payloads)
    }

    private inner class AdapterDataObserver(
        private val getStartIndex: () -> Int
    ) : RecyclerView.AdapterDataObserver() {

        private val startIndex: Int get() = getStartIndex.invoke()

        override fun onChanged() {
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            notifyItemRangeChanged(startIndex + positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            notifyItemRangeChanged(startIndex + positionStart, itemCount, payload)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            notifyItemRangeInserted(startIndex + positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            notifyItemRangeRemoved(startIndex + positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            notifyItemMoved(startIndex + fromPosition, startIndex + toPosition)
        }

    }

}