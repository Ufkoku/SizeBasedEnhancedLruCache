package com.ufkoku.cache

import android.support.v4.util.LruCache
import com.vladium.utils.ObjectProfiler

open class SizeBasedEnhancedLruCache<K, V>(sizeInBytes: Int) : LruCache<K, V>(sizeInBytes) {

    protected val map: Map<K, V> by lazy {
        synchronized(this, {
            val fields = LruCache::class.java.declaredFields
            for (field in fields) {
                if (Map::class.java.isAssignableFrom(field.type)) {
                    field.isAccessible = true
                    return@lazy field.get(this) as Map<K, V>
                }
            }
            throw NoSuchFieldException("Map field not found") //never thrown
        })
    }

    fun getEntryCount(): Int {
        synchronized(this, {
            return map.size
        })
    }

    fun containsKey(key: K): Boolean {
        synchronized(this, {
            return map.containsKey(key)
        })
    }

    override fun sizeOf(key: K?, value: V?): Int {
        val keySize = if (key != null) ObjectProfiler.sizeof(key) else 0
        val valueSize = if (value != null) ObjectProfiler.sizeof(value) else 0
        return keySize + valueSize
    }

}
