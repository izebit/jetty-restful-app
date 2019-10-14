package ru.izebit.cli

import com.beust.jcommander.JCommander
import ru.izebit.conf.DaggerHttpServerComponent
import ru.izebit.conf.HttpServerComponent
import ru.izebit.conf.WebModule

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 10.10.2019
 */

fun main(args: Array<String>) {
    ApplicationLauncher().main(args)
}

class ApplicationLauncher {
    fun main(args: Array<String>) {
        val arguments = CommandArguments()
        JCommander.newBuilder()
            .addObject(arguments)
            .build()
            .parse(*args)

        val httpServerComponent: HttpServerComponent = DaggerHttpServerComponent.builder()
            .webModule(WebModule(arguments.threads, arguments.port))
            .build()
        val httpServer = httpServerComponent.createHttpServer()

        Runtime.getRuntime().addShutdownHook(Thread {
            httpServer.stop()
        })

        httpServer.start()
        httpServer.join()
    }
}