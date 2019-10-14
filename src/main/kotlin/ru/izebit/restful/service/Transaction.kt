package ru.izebit.restful.service

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 13.10.2019
 */
data class Transaction<K : Comparable<K>>(
    /**
     * account id, which money will be withdrawn from
     */
    val from: K,
    /**
     * account id, which money will come to
     */
    val to: K,
    /**
     *  amount of money, it can be only positive
     */
    val amount: Double
)