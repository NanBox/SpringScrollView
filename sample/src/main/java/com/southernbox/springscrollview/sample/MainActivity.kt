package com.southernbox.springscrollview.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * Created by SouthernBox on 2017/6/19 0019.
 * 主页面
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        recycler_view.layoutManager = layoutManager
        layoutManager.isAutoMeasureEnabled = true
        val names = resources.getStringArray(R.array.nba)
        val mList = ArrayList<String>()
        Collections.addAll(mList, *names)
        val mAdapter = MainAdapter(this, mList)
        recycler_view.adapter = mAdapter
        recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}