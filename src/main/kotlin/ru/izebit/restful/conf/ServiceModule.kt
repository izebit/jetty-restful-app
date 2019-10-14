package ru.izebit.restful.conf

import dagger.Module
import dagger.Provides
import ru.izebit.restful.data.AccountStore
import ru.izebit.restful.data.InMemoryAccountStore
import ru.izebit.restful.service.AccountService
import ru.izebit.restful.service.AccountServiceImpl
import ru.izebit.restful.service.TransactionService
import javax.inject.Singleton


/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 14.10.2019
 */
@Module
class ServiceModule {

    @Provides
    @Singleton
    fun accountService(
        transactionService: TransactionService<Int>,
        accountStore: AccountStore<Int>
    ): AccountService<Int> {
        return AccountServiceImpl(accountStore, transactionService)
    }

    @Provides
    @Singleton
    fun transactionService(): TransactionService<Int> {
        return TransactionService<Int>()
    }

    @Provides
    @Singleton
    fun accountStore(): AccountStore<Int> = InMemoryAccountStore<Int>()
}