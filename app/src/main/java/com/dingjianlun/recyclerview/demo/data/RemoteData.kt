package com.dingjianlun.recyclerview.demo.data

import kotlinx.coroutines.delay
import kotlin.random.Random

object RemoteData {

    suspend fun getList(pageNum: Int = 1, pageSize: Int = 20): List<String> {

        delay(1000 + Random.nextLong(2000))

        if (Random.nextInt(3) == 1) throw Exception("exception")

        val start = (pageNum - 1) * pageSize
        val end = start + 20

        return if (start >= 80)
            arrayListOf()
        else
            (start until end).map { it.toString() }
    }

}