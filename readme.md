
[![](https://jitpack.io/v/huangziye/WaterView.svg)](https://jitpack.io/#huangziye/WaterView)

# Add ` WaterView ` to project

- Step 1：Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```android
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- Step 2：Add the dependency

The latest version shall prevail.

```android
dependencies {
        implementation 'com.github.huangziye:WaterView:${latest_version}'
}
```



# Effect picture


![微光效果图](https://github.com/huangziye/WaterView/blob/master/screenshot/WaterView.gif)


# Usage

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

    <FrameLayout
            android:layout_width="match_parent"
            android:background="@drawable/bg"
            android:layout_height="400dp">

        <com.hzy.water.WaterContainer
                android:id="@+id/custom_view"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>

    </FrameLayout>


    <Button
            android:id="@+id/btn"
            android:onClick="onClick"
            android:text="重置"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>

</LinearLayout>
```


```Kotlin
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
```







# About me


- [简书](https://user-gold-cdn.xitu.io/2018/7/26/164d5709442f7342)

- [掘金](https://juejin.im/user/5ad93382518825671547306b)

- [Github](https://github.com/huangziye)


# License

```
Copyright 2018, huangziye

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```



