package ru.izebit.conf

import dagger.Component
import ru.izebit.cli.ApplicationLauncher
import ru.izebit.web.HttpServer
import javax.inject.Singleton

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 14.10.2019
 */

@Singleton
@Component(modules = [ServiceModule::class, WebModule::class])
interface HttpServerComponent {

    fun createHttpServer(): HttpServer

    fun inject(main: ApplicationLauncher)
}