package com.dingjianlun.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class SimpleAdapter<T>(
    builder: SimpleAdapter<T>.() -> Unit
) : RecyclerView.Adapter<SimpleAdapter.ViewHolder<T>>() {

    fun setItem(
        layoutId: Int,
        onBind: View.(item: T) -> Unit
    ): SimpleAdapter<T> = itemList.clear().run { addItem(null, layoutId, onBind) }

    fun setItem(
        layoutId: Int,
        onBind: View.(item: T, holder: RecyclerView.ViewHolder) -> Unit
    ): SimpleAdapter<T> = addItem(null, layoutId, onBind)

    inline fun <reified R : T> addItem(
        layoutId: Int,
        noinline onBind: View.(item: R) -> Unit
    ): SimpleAdapter<T> = addItem(R::class.java, layoutId, onBind)

    inline fun <reified R : T> addItem(
        layoutId: Int,
        noinline onBind: View.(item: R, holder: RecyclerView.ViewHolder) -> Unit
    ): SimpleAdapter<T> = addItem(R::class.java, layoutId, onBind)

    inline fun <reified R : T> addItem(
        noinline viewHolder: ViewHolder.Builder<R>.(viewGroup: ViewGroup) -> Unit
    ): SimpleAdapter<T> = addItem(R::class.java, viewHolder)

    fun getItem(position: Int): T? = data.getOrNull(position)

    val currentList: List<T> get() = data


    private var data = emptyList<T>()

    private val itemList = ArrayList<Item<T>>()

    fun <R : T> addItem(
        classes: Class<R>?,
        layoutId: Int,
        onBind: View.(item: R) -> Unit
    ) = addItem(classes, layoutId) { item, _ -> onBind.invoke(this, item) }

    fun <R : T> addItem(
        classes: Class<R>?,
        layoutId: Int,
        onBind: View.(item: R, holder: ViewHolder<R>) -> Unit
    ) = addItem(classes) {
        this.view = LayoutInflater.from(it.context).inflate(layoutId, it, false)
        this.onBind = onBind
    }

    fun <R : T> addItem(
        classes: Class<R>?,
        viewHolder: ViewHolder.Builder<R>.(viewGroup: ViewGroup) -> Unit
    ) = apply {
        val item = Item(classes) { viewGroup ->
            val builder = ViewHolder.Builder<R>()
            viewHolder.invoke(builder, viewGroup)
            ViewHolder(builder)
        }
        itemList.add(item as Item<T>)
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int =
        (itemList.firstOrNull { it == getItem(position) } ?: itemList.first()).type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> =
        (itemList.firstOrNull { it.type == viewType } ?: itemList.first())
            .viewHolder.invoke(parent)

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        val item = getItem(position) ?: return
        holder.builder.onBind?.invoke(holder.itemView, item, holder)
    }

    override fun onBindViewHolder(
        holder: ViewHolder<T>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    class ViewHolder<T>(
        val builder: Builder<T>
    ) : RecyclerView.ViewHolder(builder.view) {
        class Builder<T> {
            lateinit var view: View
            var onBind: (View.(item: T, holder: ViewHolder<T>) -> Unit)? = null
        }
    }

    class Item<T>(
        private val cls: Class<T>?,
        val viewHolder: (parent: ViewGroup) -> ViewHolder<T>
    ) {

        companion object {
            private var seq = 0
                get() = field++
        }

        val type = seq

        override fun equals(other: Any?): Boolean {
            val c1 = cls
            val c2 = other?.javaClass
            return /*if (c1 != null && c2 != null) c1.isAssignableFrom(c2) else*/ c1 == c2
        }

        override fun hashCode() = type

    }

    fun setData(list: List<T>) = submitList(list)

    fun addData(list: List<T>) = submitList(ArrayList(this.data).apply { addAll(list) })

    private val diffCallback: DiffUtil.ItemCallback<T>? = null

    private fun submitList(list: List<T>) {
        val oldList = ArrayList(data)
        val newList = ArrayList(list)
        val diffResult = calculateDiff(oldList, newList, diffCallback)
        this.data = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private fun calculateDiff(
        oldList: List<T>,
        newList: List<T>,
        diffCallback: DiffUtil.ItemCallback<T>?
    ): DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList.getOrNull(oldItemPosition)
            val newItem = newList.getOrNull(newItemPosition)
            return if (diffCallback != null && oldItem != null && newItem != null)
                diffCallback.areItemsTheSame(oldItem, newItem)
            else
                oldItem == newItem
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList.getOrNull(oldItemPosition)
            val newItem = newList.getOrNull(newItemPosition)
            return if (diffCallback != null && oldItem != null && newItem != null)
                diffCallback.areContentsTheSame(oldItem, newItem)
            else
                oldItem.toString() == newItem.toString()
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldList.getOrNull(oldItemPosition)
            val newItem = newList.getOrNull(newItemPosition)
            return if (diffCallback != null && oldItem != null && newItem != null)
                diffCallback.getChangePayload(oldItem, newItem)
            else
                super.getChangePayload(oldItemPosition, newItemPosition)
        }
    })

    init {
        builder.invoke(this)
    }

}
