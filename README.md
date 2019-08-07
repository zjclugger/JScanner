# JScanner 基于zxing的扫一扫
声明：
1. 主体代码从网上获取，自己填补或修改了部分代码。
2. 本项目用于个人学习使用。
3. 因已记不清出处，若有侵权请及时与我联系。


使用方法

1.添加依赖
项目根目录build.gradle

allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://dl.bintray.com/zjclugger/maven' }
    }
}

然后在app目录build.gradle
 dependencies {
    ...
    //添加依赖
	compile 'com.github.zjclugger.JScanner:JScanner:1.0'
 ...
}

2.具体使用请参考Demo
