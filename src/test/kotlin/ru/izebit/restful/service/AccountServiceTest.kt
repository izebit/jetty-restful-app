package ru.izebit.restful.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.izebit.restful.data.Account
import ru.izebit.restful.data.InMemoryAccountStore

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 13.10.2019
 */
class AccountServiceTest {

    @Test
    fun testTransfer_success() {
        val accountService = AccountServiceImpl<Int>(InMemoryAccountStore(), TransactionService());
        val firstAccount = Account(1, 100.0)
        val secondAccount = Account(2, 200.0)
        accountService.save(firstAccount.copy())
        accountService.save(secondAccount.copy())


        val amount = 90.0
        val result = accountService.transfer(Transaction(firstAccount.id, secondAccount.id, amount))
        assert(result)
        assert(accountService.get(firstAccount.id)?.money == firstAccount.money - amount)
        assert(accountService.get(secondAccount.id)?.money == secondAccount.money + amount)
    }

    @Test
    fun testTransfer_not_enough_money() {
        val accountService = AccountServiceImpl<Int>(InMemoryAccountStore(), TransactionService());
        val firstAccount = Account(1, 100.0)
        val secondAccount = Account(2, 200.0)
        accountService.save(firstAccount.copy())
        accountService.save(secondAccount.copy())


        val amount = 101.0
        assertThrows<AccountServiceException> {
            accountService.transfer(Transaction(firstAccount.id, secondAccount.id, amount))
        }
    }

    @Test
    fun testTransfer_the_same_account() {
        val accountService = AccountServiceImpl<Int>(InMemoryAccountStore(), TransactionService());
        val account = Account(1, 100.0)
        accountService.save(account.copy())


        val amount = 101.0
        val result = accountService.transfer(Transaction(account.id, account.id, amount))
        assert(result)
        assert(accountService.get(account.id)?.money == account.money)
    }

    @Test
    fun testTransfer_amount_is_zero() {
        val accountService = AccountServiceImpl<Int>(InMemoryAccountStore(), TransactionService());
        val firstAccount = Account(1, 100.0)
        val secondAccount = Account(2, 200.0)
        accountService.save(firstAccount.copy())
        accountService.save(secondAccount.copy())



        val result = accountService.transfer(Transaction(firstAccount.id, secondAccount.id, 0.0))
        assert(result)
        assert(accountService.get(firstAccount.id)?.money == firstAccount.money)
        assert(accountService.get(secondAccount.id)?.money == secondAccount.money)
    }
}