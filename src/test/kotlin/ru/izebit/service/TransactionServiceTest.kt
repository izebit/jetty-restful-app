package ru.izebit.service

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import ru.izebit.data.Account
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.IntStream

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */


class TransactionServiceTest {

    @Test
    fun testStartTransaction() {
        val transactionService = TransactionService<Int>(ForkJoinPool.getCommonPoolParallelism())

        val firstAccount = Account(0, 101)
        val secondAccount = Account(1, 102)
        val accounts = listOf(firstAccount, secondAccount)
        val amount = 100L;

        val result = IntStream.range(0, 100)
            .parallel()
            .map { i ->
                val first = accounts[i % 2]
                val second = accounts[(i + 1) % 2]
                transactionService.startTransaction(first.id, second.id) {
                    first.money -= amount
                    second.money += amount
                    1
                }
            }.sum()

        assert(result == 100)
        assert(firstAccount.money == 101L) { firstAccount.toString() }
        assert(secondAccount.money == 102L) { secondAccount.toString() }
    }
}