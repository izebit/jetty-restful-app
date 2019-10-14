package ru.izebit.restful.data

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 10.10.2019
 */
interface AccountStore<K : Comparable<K>> {
    /**
     * returns an account with a certain id
     * @param id - account id
     * @return an account if a data store contains an account with id, otherwise null
     */
    fun get(id: K): Account<K>?

    /**
     * saves account into data store
     * @param account which will be saved
     * @return true if a data store doesn't contain an account with the same an account id,
     * otherwise false
     */
    fun save(account: Account<K>): Boolean
}