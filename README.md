Lib overrides android.support.v4.util.LruCache and uses code from [article](http://www.javaworld.com/article/2077408/core-java/sizeof-for-java.html) to measure size of objects in heap.

```gradle

repositories {
    maven { url 'https://dl.bintray.com/ufkoku/maven/' }
}

dependencies {
    compile 'com.ufkoku.cache:lru_cache:1.0.0@aar'
    //or
    compile 'com.ufkoku.cache:lru_cache:1.0.0'
}

```

Usage:

SizeBasedEnhancedLruCache<String, Object> cache = new SizeBasedEnhancedLruCache<>(sizeInBytes);
