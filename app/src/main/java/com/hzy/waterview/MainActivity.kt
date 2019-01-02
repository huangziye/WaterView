package com.hzy.waterview

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hzy.water.Water
import com.hzy.water.WaterContainer


class MainActivity : AppCompatActivity() {
    private lateinit var mWaterContainer: WaterContainer
    private lateinit var mBtn: Button
    private lateinit var mList: MutableList<Water>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mWaterContainer = findViewById(R.id.custom_view)
        mBtn = findViewById(R.id.btn)
        mWaterContainer.setOnWaterItemListener(object : WaterContainer.OnWaterItemListener {
            override fun onItemClick(water: Water) {
                if (water.clickable) {
                    Toast.makeText(this@MainActivity, "收取能量" + water.content, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "暂时不能收取能量", Toast.LENGTH_SHORT).show()
                }
            }
        })
        initData()
    }

    fun onClick(v: View) {
        initData()
    }

    private fun initData() {
        mList = mutableListOf()
        for (i in 0..5) {
            mList.add(Water(i, "${i}g", i % 2 == 0))
        }
        mBtn.post {
            //此处目前写死坐标，后期可以获取小树的坐标添加进去
            mWaterContainer.setWaterList(mList, mBtn)
        }

    }
}
