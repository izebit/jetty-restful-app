package ru.izebit.service

import ru.izebit.data.Account

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */
interface AccountService<K : Comparable<K>> {

    /**
     * saves an account into data store
     * @param account which will be saved
     * @return true if a data store doesn't contain an account with the same id, otherwise false
     */
    fun save(account: Account<K>): Boolean

    /**
     *  gets an account data with a certain id
     *  @param id - account id
     *  @return account if it exists, otherwise null
     */
    fun get(id: K): Account<K>?

    /**
     * transfers money from an account to another one
     * @param from - account id, which money will be withdrawn from
     * @param to - account id, which money will come to
     * @param amount - amount of money, it can be only positive
     * @return true if an operation is successful, otherwise false
     * @throws AccountServiceException occurs if a transaction couldn't start or accounts don't exist
     */
    @Throws(AccountServiceException::class)
    fun transfer(from: K, to: K, amount: Double): Boolean
}