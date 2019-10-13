package ru.izebit.service

import org.junit.jupiter.api.Test
import ru.izebit.data.Account
import java.util.stream.IntStream

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */


class TransactionServiceTest {

    @Test
    fun testStartTransaction() {
        val transactionService = TransactionService<Int>()

        val firstAccount = Account(0, 101.0)
        val secondAccount = Account(1, 102.0)
        val accounts = listOf(firstAccount, secondAccount)
        val amount = 100L;

        val count = 10_000
        val result = IntStream.range(0, count)
            .parallel()
            .map { i ->
                val first = accounts[i % 2]
                val second = accounts[(i + 1) % 2]
                transactionService.transaction(first.id, second.id) {
                    first.money -= amount
                    second.money += amount
                    1
                }
            }.sum()

        assert(result == count)
        assert(firstAccount.money == 101.0) { firstAccount.toString() }
        assert(secondAccount.money == 102.0) { secondAccount.toString() }
    }
}