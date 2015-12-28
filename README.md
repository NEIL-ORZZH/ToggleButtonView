# ToggleButtonView
实现根据手势滑动的自定义开关控件
<h1>效果预览<h1>

![image](https://github.com/songnigo/ToggleButtonView/blob/master/ToggleButtonView/screenShots/main.png)

<h1>使用方法</h1>
<h3>build.gradle文件</h3>
    dependencies {
        compile 'com.android.support:appcompat-v7:23.1.1'
        compile project(':libs')
    }
<h1>引用实例</h1>
    <com.song.libs.ToggleButtonView
        android:layout_width="40dp"
        android:layout_height="25dp"
        android:layout_centerVertical="true"
        android:layout_alignParentRight="true"
        android:layout_marginRight="10dp"
        app:onColor="#0D4300"
        app:offColor="@android:color/white"
        app:bgLineHeight="7dp"
        app:bgLineColorRadius="3dp"
        android:id="@+id/toggleButton"/>
<h1>添加监听</h1>
        ((ToggleButtonView)findViewById(R.id.toggleButton)).setOnToggleButtonChangedListener(new       ToggleButtonView.OnToggleButtonChanged() {
            @Override
            public void onToggle(boolean toggle) {
                Log.i(TAG, toggle + "");
            }
        });
<h3>QQ：474608897</h3>
