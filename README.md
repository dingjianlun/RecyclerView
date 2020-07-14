# RecyclerView

支持多布局的Adapter

### 集成
添加依赖
```gradle
implementation 'com.dingjianlun:recyclerview:0.0.1'
```

### 创建Adapter
```kotlin
val adapter = SimpleAdapter<BaseItem> {
  //addItem...
}
```

### kotlinx
```kotlin
import kotlinx.android.synthetic.main.list_item_1.view.*

val adapter = SimpleAdapter<BaseItem> {
  addItem<Item1>(R.layout.list_item_1) { item, holder ->
    tv_text.text = ("${item.id}: ${item.name}")
    setOnClickListener {
      Toast.makeText(context, "${holder.adapterPosition}", Toast.LENGTH_SHORT).show()
    }
  }
  //addItem...
}
```

### dataBinding
```kotlin
val adapter = SimpleAdapter<BaseItem> {
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
