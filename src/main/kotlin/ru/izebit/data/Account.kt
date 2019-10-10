package ru.izebit.data

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */
data class Account<K>(
    val id: K,
    var money: Long
)