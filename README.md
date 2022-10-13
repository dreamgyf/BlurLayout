# BlurLayout

**一种方便易用的毛玻璃效果控件**

**注：编译此项目请直接 clone 父工程 [AndroidLibraries](https://github.com/dreamgyf/AndroidLibraries)**

![Demo](./BlurLayout.gif)

## Get Started 快速使用

```groovy
implementation 'com.dreamgyf.android.ui.widget:BlurLayout:1.1'
```

```xml
<com.dreamgyf.android.ui.widget.blur.BlurLayout
    android:layout_width="300dp"
    android:layout_height="54dp">

    <!-- Coding widgets you want there, and just think of outer layer as FrameLayout. -->
    <!-- You need notice that you'll not see the blur effect if inner background is opaque. -->
    <!-- 在此处编写你想要的控件，把外层当成FrameLayout即可，注意如果内部背景不透明的话是看不到毛玻璃效果的 -->

</com.dreamgyf.android.ui.widget.blur.BlurLayout>
```

## Layout properties 布局属性

- `app:blurQuality`

Description: Set quality of blur effect\
Type: float\
Default value: 1\
range: 0.01 - 1

描述：设置毛玻璃效果的质量\
类型：float\
默认值：1\
范围：0.01 - 1

- `app:blurRadius`

Description: Set radius of blur effect\
Type: dimension\
Default value: 0px\
range: 0 - 25px

描述：设置毛玻璃效果的半径\
类型：dimension\
默认值：0px\
范围：0 - 25px

- `app:cornerRadius`

Description: Set radius of four rounded corners\
Type: dimension\
Default value: 0px

描述：设置毛玻璃效果的半径\
类型：dimension\
默认值：0px

**ps: You can improve performance by reducing blurQuality or blurRadius.**\
**注：可以通过降低blurQuality或blurRadius以提高性能**