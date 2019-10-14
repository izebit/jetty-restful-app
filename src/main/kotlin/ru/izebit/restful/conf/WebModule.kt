package ru.izebit.restful.conf

import dagger.Module
import dagger.Provides
import ru.izebit.restful.service.AccountService
import ru.izebit.restful.web.HttpServer
import javax.inject.Singleton

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 14.10.2019
 */

@Module
class WebModule constructor(
    private val threads: Int,
    private val port: Int) {

    @Provides
    @Singleton
    fun httpService(accountService: AccountService<Int>): HttpServer {
        return HttpServer(
            accountService = accountService,
            port = port,
            maxThreads = threads
        )
    }
}