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


        val money = 90.0
        val result = accountService.transfer(Transaction(firstAccount.id, secondAccount.id, money))
        assert(result)
        assert(accountService.get(firstAccount.id)?.money == firstAccount.money - money)
        assert(accountService.get(secondAccount.id)?.money == secondAccount.money + money)
    }

    @Test
    fun testTransfer_not_enough_money() {
        val accountService = AccountServiceImpl<Int>(InMemoryAccountStore(), TransactionService());
        val firstAccount = Account(1, 100.0)
        val secondAccount = Account(2, 200.0)
        accountService.save(firstAccount.copy())
        accountService.save(secondAccount.copy())


        val money = 101.0
        assertThrows<AccountServiceException> {
            accountService.transfer(Transaction(firstAccount.id, secondAccount.id, money))
        }
    }

    @Test
    fun testTransfer_the_same_account() {
        val accountService = AccountServiceImpl<Int>(InMemoryAccountStore(), TransactionService());
        val account = Account(1, 100.0)
        accountService.save(account.copy())


        val money = 101.0
        val result = accountService.transfer(Transaction(account.id, account.id, money))
        assert(result)
        assert(accountService.get(account.id)?.money == account.money)
    }

    @Test
    fun testTransfer_money_is_zero() {
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