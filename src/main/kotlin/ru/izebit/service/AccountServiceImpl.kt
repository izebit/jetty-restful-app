package ru.izebit.service

import ru.izebit.data.Account
import ru.izebit.data.AccountStore

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */
class AccountServiceImpl<K : Comparable<K>> constructor(
    private val accountStore: AccountStore<K>,
    private val transactionService: TransactionService<K>
) : AccountService<K> {

    override fun transfer(from: K, to: K, amount: Double): Boolean {
        if (amount == 0.0 || from == to)
            return true;

        return transactionService.transaction(from, to, {
            val sender = get(from) ?: throw AccountServiceException("A sender account with id:${from} doesn't exist")
            val recipient = get(to) ?: throw AccountServiceException("A recipient account with id:${to} doesn't exist")
            sender.money -= amount
            if (sender.money < 0)
                throw AccountServiceException("An account with id: ${sender.id} doesn't have enough money")

            recipient.money += amount

            save(sender)
            save(recipient)

            true
        })
    }


    override fun save(account: Account<K>) = accountStore.save(account)
    override fun get(id: K): Account<K>? = accountStore.get(id)
}