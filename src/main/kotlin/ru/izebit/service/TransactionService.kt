package ru.izebit.service

import java.lang.ref.Reference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */
class TransactionService<K : Comparable<K>> constructor(threadCount: Int) {
    private val locks = ConcurrentHashMap<K, String>(threadCount)

    private fun <T : Comparable<T>> max(a: T, b: T): T = if (a > b) a else b
    private fun <T : Comparable<T>> min(a: T, b: T): T = if (a < b) a else b

    fun <T> startTransaction(first: K, second: K, operation: () -> T): T {
        try {
            val threadName = Thread.currentThread().name!!;
            lock(min(first, second), threadName)
            lock(max(first, second), threadName)

            return operation.invoke()
        } finally {
            locks.remove(first)
            locks.remove(second)
        }
    }


    private fun lock(key: K, value: String) {
        val time = System.currentTimeMillis()
        while (!Thread.currentThread().isInterrupted && locks.putIfAbsent(key, value) != null) {
            if (System.currentTimeMillis() - time > 100)
                throw IllegalAccessException("can't start a transaction")
        }
    }
}