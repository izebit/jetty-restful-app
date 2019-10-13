package ru.izebit.web

import org.eclipse.jetty.server.Connector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.servlet.ServletHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.util.thread.QueuedThreadPool
import ru.izebit.service.AccountService
import java.util.concurrent.TimeUnit


/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 12.10.2019
 */

class HttpServer constructor(
    maxThreads: Int = 10,
    port: Int = 8080,
    accountService: AccountService<Int>
) {
    private val server: Server

    init {
        val threadPool = QueuedThreadPool(maxThreads, (maxThreads / 100) * 10, TimeUnit.MINUTES.toMillis(1).toInt())

        val server = Server(threadPool)
        val connector = ServerConnector(server)
        connector.port = port
        server.connectors = arrayOf<Connector>(connector)

        val servletHandler = ServletHandler()
        server.handler = servletHandler

        servletHandler.addServletWithMapping(
            ServletHolder("account_servlet", AccountServlet(accountService)),
            "/accounts/*"
        )
        servletHandler.addServletWithMapping(
            ServletHolder("accounts_servlet", AccountsServlet(accountService)),
            "/accounts"
        )

        this.server = server
    }

    @Throws(Exception::class)
    fun start() {
        server.start()
    }

    fun stop() {
        server.stop()
    }
}