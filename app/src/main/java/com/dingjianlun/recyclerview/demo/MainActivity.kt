package com.dingjianlun.recyclerview.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dingjianlun.recyclerview.demo.activity.MultiAdapterActivity
import com.dingjianlun.recyclerview.demo.activity.MultiTypeActivity
import com.dingjianlun.recyclerview.demo.activity.PagingActivity
import com.dingjianlun.recyclerview.demo.activity.SingleTypeActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_singleType.setOnClickListener {
            startActivity(Intent(this, SingleTypeActivity::class.java))
        }

        btn_multiType.setOnClickListener {
            startActivity(Intent(this, MultiTypeActivity::class.java))
        }

        btn_multiAdapter.setOnClickListener {
            startActivity(Intent(this, MultiAdapterActivity::class.java))
        }

        btn_paging.setOnClickListener {
            startActivity(Intent(this, PagingActivity::class.java))
        }

    }

}