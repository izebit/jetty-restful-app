package ru.izebit.service

import java.lang.ref.Reference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */
class TransactionService<K : Comparable<K>> {
    private val locks = ConcurrentHashMap<K, AtomicReference<String>>()
    private val minReference = ThreadLocal.withInitial { AtomicReference<String>() }
    private val maxReference = ThreadLocal.withInitial { AtomicReference<String>() }

    private fun <T : Comparable<T>> max(a: T, b: T): T = if (a > b) a else b
    private fun <T : Comparable<T>> min(a: T, b: T): T = if (a < b) a else b

    fun startTransaction(first: K, second: K, operation: () -> Boolean): Boolean {
        val firstLock = locks.getOrDefault(min(first, second), minReference.get())
        val secondLock = locks.getOrDefault(max(first, second), maxReference.get())
        try {
            lock(firstLock)
            lock(secondLock)
            return operation.invoke()
        } finally {
            firstLock.set(null)
            secondLock.set(null)
        }
    }


    private fun lock(lock: AtomicReference<String>): AtomicReference<String> {
        val time = System.currentTimeMillis();
        while (!(Thread.currentThread().isInterrupted || lock.compareAndSet(null, Thread.currentThread().name))) {
            if (System.currentTimeMillis() - time > 100)
                throw IllegalAccessException("can't start a transaction")
        }
        return lock;
    }
}