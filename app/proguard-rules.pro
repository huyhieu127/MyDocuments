# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

#Data
-keepnames class * extends android.os.Parcelable
-keep class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keepnames class * extends java.io.Serializable

#Keep Android
-keep class android**
-keepclassmembers class android** {*;}

-keep class androidx**
-keepclassmembers class androidx** {*;}

-keep class ** { @kotlin.internal.InlineOnly *; }

-keep class com.huyhieu.mydocuments.** { *; }
-keepclassmembers class com.huyhieu.mydocuments** { *; }

-keep class androidx.viewbinding.** { *; }
-keepclassmembers class * extends androidx.viewbinding.ViewBinding {
    public static ** inflate(...);
}

#Missing_rules R8
-dontwarn javax.lang.model.SourceVersion
-dontwarn javax.lang.model.element.Element
-dontwarn javax.lang.model.element.ElementKind
-dontwarn javax.lang.model.type.TypeMirror
-dontwarn javax.lang.model.type.TypeVisitor
-dontwarn javax.lang.model.util.SimpleTypeVisitor8
-dontwarn com.oracle.svm.core.annotate.Delete
-dontwarn com.oracle.svm.core.annotate.TargetClass
-dontwarn javax.lang.model.element.AnnotationMirror
-dontwarn javax.lang.model.element.AnnotationValue
-dontwarn javax.lang.model.element.AnnotationValueVisitor
-dontwarn javax.lang.model.element.ElementVisitor
-dontwarn javax.lang.model.element.ExecutableElement
-dontwarn javax.lang.model.element.Name
-dontwarn javax.lang.model.element.TypeElement
-dontwarn javax.lang.model.element.VariableElement
-dontwarn javax.lang.model.type.DeclaredType
-dontwarn javax.lang.model.type.ExecutableType
-dontwarn javax.lang.model.type.TypeKind
-dontwarn javax.lang.model.util.AbstractElementVisitor8
-dontwarn javax.lang.model.util.ElementFilter
-dontwarn javax.lang.model.util.SimpleAnnotationValueVisitor8
-dontwarn javax.lang.model.util.SimpleElementVisitor8
-dontwarn javax.tools.SimpleJavaFileObject