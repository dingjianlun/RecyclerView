package com.dingjianlun.recyclerview

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Paging<T, K>(
    private val coroutineScope: CoroutineScope,
    private val listAdapter: SimpleAdapter<T>,
    private val ui: UI,
    private val initKey: K,
    private val loadData: suspend (key: K) -> Result<T, K>
) {

    internal val adapter by lazy { MultiAdapter(listAdapter, moreAdapter) }

    private var nextKey: K = initKey

    abstract class UI {

        abstract val layoutId: Int
        abstract val update: View.(state: State) -> Unit
        open var updateState: ((state: State) -> Unit)? = null

    }

    private var state: State = Nothing
        set(value) {
            field = value
            ui.updateState?.invoke(value)
            moreAdapter.setData(arrayListOf(value))
        }
    private val moreAdapter = SimpleAdapter<State>()
        .addItem<Nothing>(ui.layoutId) { item, _ -> updateUI(this, item) }
        .addItem<Loading>(ui.layoutId) { item, _ -> updateUI(this, item) }
        .addItem<NoMore>(ui.layoutId) { item, _ -> updateUI(this, item) }
        .addItem<Error>(ui.layoutId) { item, _ -> updateUI(this, item) }
        .addItem<Empty>(ui.layoutId) { item, _ -> updateUI(this, item) }

    private fun updateUI(view: View, item: State) {
        ui.update(view, item)
        if (state == Nothing) nextPage()
    }

    private fun nothing() = run { state = Nothing }
    private fun loading(isFirstPage: Boolean) = run { state = Loading(isFirstPage) }
    private fun noMore() = run { state = NoMore }
    private fun error(e: Throwable) = run { state = Error(e) { getPage(nextKey) } }
    private fun empty() = run { state = Empty }

    fun initPage() = getPage(initKey)
    fun nextPage() = getPage(nextKey)
    fun refresh() = initPage()

    private fun getPage(key: K) {
        coroutineScope.launch {
            try {
                val isFirstPage = (key == initKey)

                loading(isFirstPage)

                val result = loadData.invoke(key)

                val nextKey = result.nextKey
                val data = result.data

                if (data.isEmpty()) {
                    if (isFirstPage) {
                        listAdapter.setData(data)
                        empty()
                    } else {
                        noMore()
                    }
                } else {
                    if (isFirstPage) {
                        listAdapter.setData(data, false)
                    } else {
                        listAdapter.addData(data)
                    }
                    this@Paging.nextKey = nextKey
                    nothing()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                error(e)
            }
        }
    }

    open class State
    object Nothing : State()
    class Loading(val isFirstPage: Boolean) : State()
    object NoMore : State()
    class Error(val e: Throwable, val retry: () -> Unit) : State()
    object Empty : State()

    class Result<T, K>(val data: List<T>, val nextKey: K)

}
