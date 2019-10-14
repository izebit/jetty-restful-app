package ru.izebit.restful.service

import ru.izebit.restful.data.Account
import ru.izebit.restful.data.AccountStore

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */
class AccountServiceImpl<K : Comparable<K>> constructor(
    private val accountStore: AccountStore<K>,
    private val transactionService: TransactionService<K>
) : AccountService<K> {

    override fun transfer(transaction: Transaction<K>): Boolean {
        if (transaction.amount == 0.0 || transaction.from == transaction.to)
            return true;

        return transactionService.transaction(transaction.from, transaction.to, {
            val sender = get(transaction.from)
                ?: throw AccountServiceException("A sender account with id:${transaction.from} doesn't exist")
            val recipient = get(transaction.to)
                ?: throw AccountServiceException("A recipient account with id:${transaction.to} doesn't exist")
            sender.money -= transaction.amount
            if (sender.money < 0)
                throw AccountServiceException("An account with id: ${sender.id} doesn't have enough money")

            recipient.money += transaction.amount

            save(sender)
            save(recipient)

            true
        })
    }


    override fun save(account: Account<K>) = accountStore.save(account)
    override fun get(id: K): Account<K>? = accountStore.get(id)
}