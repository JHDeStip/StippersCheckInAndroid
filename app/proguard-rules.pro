-keepclassmembers public class * {
     @com.fasterxml.jackson.annotation.JsonCreator *;
}

-dontobfuscate

-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn java.beans.Transient
-dontwarn java.beans.ConstructorProperties
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.Paths
-dontwarn java.beans.Transient
-dontwarn java.beans.ConstructorProperties