# SnowFall-master
##接入
1.在根目录下的build.gradle添加依赖：
dependencies {
    implementation project(':snowfall')
}
##使用
1.在xml文件
<com.marvin.snowfall.SnowfallView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/sf_snow"
        app:snowflakesNum="200"
        app:snowflakeAlphaMin="150"
        app:snowflakeAlphaMax="255"
        app:snowflakeAngleMax="5"
        app:snowflakeSizeMin="2dp"
        app:snowflakeSizeMax="100dp"
        app:snowflakeSpeedMin="2"
        app:snowflakeSpeedMax="7"
        app:snowflakesFadingEnabled="true"
        app:snowflakesAlreadyFalling="false"
        app:snowflakeImage="@mipmap/snowflake"
        />
 
