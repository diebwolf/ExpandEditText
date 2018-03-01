# ExpandEditText
一个支持图文混排的EditText

- 图文编辑
- 图文解析

### 资源
- [下载示例软件](https://raw.githubusercontent.com/zfman/ExpandEditText/master/extras/app-debug.apk)
- [有图有真相](https://github.com/zfman/ExpandEditText/wiki/%E6%95%88%E6%9E%9C%E5%9B%BE)

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

### 高级功能

**1.基础设置**
```java
  expandEditText.bind(this)//绑定上下文，点击非编辑区域键盘自动弹起
                .setOnExpandImageClickListener(this)//图片点击监听
                .setHintText("说些什么吧~");//设置Hint
```

**2.插入文字**
```java
  //追加控件显示的文本
  expandEditText.appendText("追加字符串至末尾");
  
  //设置控件显示的文本
  expandEditText.setText("设置文本");
  
  //在索引处插入新文本
  expandEditText.createEditEntity(0)
  
```

**2.插入图片**
```java
  //在光标位置插入图片，使用path来替换该图片
  expandEditText.insertBitmap(bitmap,path);
  
  //在末尾追加图片，使用replace来替换该图片,replace通常为图片的路径
  expandEditText.appendBitmap(bitmap,replace);
  
  //在索引处插入图片
  expandEditText.createImageEntity(bitmap, replace,0)
  
```

**3.视图的删除**
```java
  //根据实体索引删除视图
  expandEditText.removeExpandViewAt(0)
  
  //根据实体对象删除视图
  expandEditText.removeExpandViewAt(baseEntity)
  
```

**4.获取文本**
```java
  //将控件中的所有元素转换为字符串
  expandEditText.getText()
  
```

**5.图文解析**
```java
  //解析图文
  expandEditText.load(text);
```

**6.自定义**
- 自定义图片解析格式

  提示：需要继承ImageWrapper抽象类，实现从字符串中解析出图片实体的方法
- 自定义图片的样式

  提示：需要继承DefaultImageLoader
- 文字样式可以修改

  提示：实现OnExpandBuildListener接口即可

### 题外话
由于自身项目需要，所以做了这么个控件，由于时间紧迫，demo以及文档都很粗糙。

项目虽然还有一些地方不太完善，但是已经能够投入生产了，等近期忙完后将会优化项目以及完善demo。
