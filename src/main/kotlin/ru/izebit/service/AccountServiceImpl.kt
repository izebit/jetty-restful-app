package ru.izebit.service

import ru.izebit.data.Account
import ru.izebit.data.AccountStore
import java.util.*

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */
class AccountServiceImpl<K : Comparable<K>> constructor(private val accountStore: AccountStore<K>) : AccountService<K> {

    override fun transfer(from: K, to: K, amount: Long): Boolean {
        if (amount == 0L)
            return true;


    }

    private fun lock(id: K) {

    }

    private fun transfer(from: Account<K>?, to: Account<K>?, amount: Long): Boolean {
        if (from == null || to == null)
            return false;

        if (amount < 0)

    }

    override fun save(account: Account<K>) = accountStore.save(account)
    override fun get(id: K): Account<K>? = accountStore.get(id)
}