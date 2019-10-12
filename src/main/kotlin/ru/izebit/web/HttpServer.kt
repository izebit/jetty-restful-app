package ru.izebit.web

import org.eclipse.jetty.server.Connector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.servlet.ServletHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.util.thread.QueuedThreadPool
import ru.izebit.service.AccountService


/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 12.10.2019
 */

class HttpServer constructor(
    private val maxThreads: Int = 100,
    private val minThreads: Int = 10,
    private val idleTimeout: Int = 120,
    private val port:Int = 8090,
    private val accountService: AccountService<Int>
) {

    private var server: Server? = null

    @Throws(Exception::class)
    fun start() {

        val threadPool = QueuedThreadPool(maxThreads, minThreads, idleTimeout)

        val server = Server(threadPool)
        val connector = ServerConnector(server)
        connector.port = port
        server.connectors = arrayOf<Connector>(connector)

        val servletHandler = ServletHandler()
        server.handler = servletHandler

        servletHandler.addServletWithMapping(
            ServletHolder("get_account", GetAccountServlet(accountService)),
            "/accounts"
        )
        servletHandler.addServletWithMapping(
            ServletHolder("create_account", CreateAccountServlet(accountService)),
            "/accounts"
        )
        servletHandler.addServletWithMapping(
            ServletHolder("transfer", TransferMoneyServlet(accountService)),
            "/accounts"
        )

        server.start()
        this.server = server
    }

    fun stop() {
        server?.stop()
    }
}