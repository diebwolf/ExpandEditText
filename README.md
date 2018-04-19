# ExpandEditText
一个支持图文混排的EditText

- 图文编辑
- 图文解析

### 资源
- [下载示例软件](https://raw.githubusercontent.com/zfman/ExpandEditText/master/extras/app-debug.apk)
- [有图有真相](https://github.com/zfman/ExpandEditText/wiki/%E6%95%88%E6%9E%9C%E5%9B%BE)
- [Document WIKI](https://github.com/zfman/ExpandEditText/wiki)

### 简单使用

**Step 1：添加项目依赖**

在build.gradle文件中添加以下代码
```
compile 'com.zhuangfei:ExpandEditText:1.0.0'
```

**Step 2：引入`ExpandEditText`控件**

该控件自身是没有滚动效果的，故应该在其外部加一层滚动布局。

引入该控件后，它就拥有了基础的图文编辑功能。
```xml
    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:fillViewport="true">

        <com.zhuangfei.expandedittext.ExpandEditText
            android:id="@+id/id_edittext"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical"
            android:padding="15dp" />
    </ScrollView>
```

### Document
[Document WIKI](https://github.com/zfman/ExpandEditText/wiki)
