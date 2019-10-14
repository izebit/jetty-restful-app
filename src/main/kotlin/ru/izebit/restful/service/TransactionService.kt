package ru.izebit.restful.service

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */
class TransactionService<K : Comparable<K>> {
    private val locks: Cache<K, AtomicReference<String>> = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.HOURS)
        .weakValues()
        .build()

    private fun <T : Comparable<T>> max(a: T, b: T): T = if (a > b) a else b
    private fun <T : Comparable<T>> min(a: T, b: T): T = if (a < b) a else b

    fun <T> transaction(first: K, second: K, operation: () -> T): T {
        if (first == second)
            throw AccountServiceException("Can't start transaction with the same ids")

        var firstLock: AtomicReference<String>? = null
        var secondLock: AtomicReference<String>? = null
        try {
            val threadName = Thread.currentThread().name!!;
            firstLock = lock(min(first, second), threadName)
            secondLock = lock(max(first, second), threadName)

            return operation.invoke()
        } finally {
            firstLock?.set(null)
            secondLock?.set(null)
        }
    }


    private fun lock(key: K, value: String): AtomicReference<String> {
        val time = System.currentTimeMillis()
        val lock = locks.get(key, { _ -> AtomicReference() })!!
        while (!Thread.currentThread().isInterrupted && !lock.compareAndSet(null, value)) {
            val diff = System.currentTimeMillis() - time
            if (diff > 100)
                throw IllegalAccessException("can't start a transaction. locking took more than $diff ms")
        }

        return lock
    }
}