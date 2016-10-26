# MultidexDemo

##利用分包成外部APK解决65535问题
demo中以google广告的分包为例，google的包有2w+个方法，很容易破65535。将调用google广告的方法和google的包打在一个新的APK中，放入项目的asserts文件里
在项目中以反射的方法调用资源apk中开启广告的方法。
###Android4.4及以后通过DexClassLoader加载APK，该方法不需要安装资源文件中的apk，可以直接调用apk内部方法
###Android4.3及以前只能通过PathClassLoader加载APK，该方法必须安装资源文件中的apk，会影响第一次安装项目的效率
###本demo只是作者经过学习之后实现的最简单的Multidex分包的应用，而且只能自己手动分包，如有一点借鉴意义的话请star哦。
###想深入学习请前往https://github.com/nuptboyzhb/AndroidPluginFramework
