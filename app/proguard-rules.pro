# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#指定压缩级别
-optimizationpasses 5

#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers

#混淆时采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#把混淆类中的方法名也混淆了
-useuniqueclassmembernames

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile
#保留行号
-keepattributes SourceFile,LineNumberTable

-keepattributes *Annotation*,InnerClasses #保留注解不混淆
-keepattributes Signature # 避免混淆泛型
-keepattributes SourceFile,LineNumberTable# 抛出异常时保留代码行号


#混淆时不使用大小写混合类名
-dontusemixedcaseclassnames

#不跳过library中的非public的类
-dontskipnonpubliclibraryclasses

#打印混淆的详细信息
-verbose


#保留native方法的类名和方法名

-keepclasseswithmembernames class * {
    native <methods>;
}


#--------------------------------------------- 公共配置 End ----------------------------------------#

#--------------------------2.第三方包-------------------------------
#Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.* { *;}
-dontwarn com.google.gson.**
# Rxtool
-keep class com.github.tamsiree.* { *;}
-dontwarn com.vondear.**
# okio
-dontwarn okio.**
