# RecyclerView

这个项目扩展了recyclerview和adapter。你可以在一个adapter使用多种布局，也可以给一个recyclerview使用多个adapter。支持分页加载。

### 集成
添加依赖
```gradle
implementation 'com.dingjianlun:recyclerview:0.0.2'
```

### 多布局的Adapter
```kotlin
val adapter = SimpleAdapter<BaseItem> {

  //使用kotlinx
  addItem<Item1>(R.layout.list_item_1) { item, holder ->
    tv_text.text = ("${item.id}: ${item.name}")
    setOnClickListener {
      Toast.makeText(context, "${holder.adapterPosition}", Toast.LENGTH_SHORT).show()
    }
  }

  //使用dataBinding
  addItem<Item2> {
      val dataBinding = ListItem2Binding.inflate(LayoutInflater.from(it.context), it, false)
      view = dataBinding.root
      onBind = { item, holder ->
        dataBinding.item = item
        dataBinding.executePendingBindings()
      }
   }

  //addItem...
}
```

### recyclerview使用多个adapter
```kotlin
    recyclerView.setAdapter(adapter1, adapter2)
```

### 分页加载
```kotlin
    val paging = Paging(
        coroutineScope, //加载数据使用的协程
        adapter, //页面的适配器
        object : Paging.UI() {//自定义ui

            //设置layout
            override val layoutId: Int = R.layout.list_item_more

            override val update: View.(state: Paging.State) -> Unit = { state ->
                //根据当前状态更新ui
                v_loading.isVisible = state is Paging.Loading
                v_error.isVisible = state is Paging.Error
                v_noMore.isVisible = state is Paging.NoMore
                v_error.setOnClickListener { if (state is Paging.Error) state.retry.invoke() }
            }

        },
        1 //第一个页面的key
    ) { key ->
        val data = RemoteData.getList(key) //模拟网络请求，根据key加载数据
        Paging.Result(data, key + 1)//返回加载的数据，同时设置下一页的key
    }

    recyclerView.setAdapter(paging)

    paging.initPage()//加载第一页
```