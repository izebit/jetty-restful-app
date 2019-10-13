package ru.izebit.web

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 13.10.2019
 */
data class Transaction(
    val from: Int,
    val to: Int,
    val amount: Double
)