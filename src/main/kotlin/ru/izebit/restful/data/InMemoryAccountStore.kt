package ru.izebit.restful.data

import java.util.concurrent.ConcurrentHashMap

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */

class InMemoryAccountStore<K : Comparable<K>> : AccountStore<K> {
    private val store = ConcurrentHashMap<K, Account<K>>();

    override fun get(id: K): Account<K>? = store[id]
    override fun save(account: Account<K>): Boolean = store.put(account.id, account) == null
}