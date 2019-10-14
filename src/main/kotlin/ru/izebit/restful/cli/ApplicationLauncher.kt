package ru.izebit.restful.cli

import com.beust.jcommander.JCommander
import ru.izebit.restful.conf.DaggerHttpServerComponent
import ru.izebit.restful.conf.WebModule

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 10.10.2019
 */
class ApplicationLauncher {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val arguments = CommandArguments()
            JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(*args)

            val httpServer = DaggerHttpServerComponent.builder()
                .webModule(WebModule(arguments.threads, arguments.port))
                .build().createHttpServer()

            Runtime.getRuntime().addShutdownHook(Thread {
                httpServer.stop()
            })

            httpServer.start()
            println("\u001B[32m" + "Application has been started" + "\u001B[0m")
            httpServer.join()
        }
    }
}