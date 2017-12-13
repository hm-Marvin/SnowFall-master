# SnowFall-master
<br>基于[Android-Snowfall](https://github.com/JetradarMobile/android-snowfall)进行kotlin->Java和使用SurfaceView绘制
<br>
<br>![](https://raw.githubusercontent.com/hm-Marvin/SnowFall-master/master/art/demo.gif)

接入
----
<br>1.下载后在根目录下的build.gradle添加依赖：
```
dependencies {
    implementation project(':snowfall')
}
```
使用
----
<br>1.在xml文件
```
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
```
License
----
```
Copyright 2017 hm-Marvin

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
 
