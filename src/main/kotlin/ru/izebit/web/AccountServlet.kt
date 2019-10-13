package ru.izebit.web

import ru.izebit.service.AccountService
import ru.izebit.web.HttpUtils.of
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.*

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */
class AccountServlet constructor(private val accountService: AccountService<Int>) : HttpServlet() {

    /**
     * servlet for handling request to get information about accounts
     * the endpoint has an url: {@code /accounts/{id}}
     * where {id} is an account id
     */
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val id = getAccountId(request)
        if (id == null) {
            response.of(
                """
                {
                    "status": "fail",
                    "info": "can't parse account id: ${request.pathInfo}"
                }""", SC_BAD_REQUEST
            )
        } else {
            val account = accountService.get(id)
            if (account == null)
                response.of(
                    """{
                    "status": "fail",
                    "info": "can't found an account with id: $id"
                }""", SC_NOT_FOUND
                )
            else
                response.of(
                    """{
                    "status": "ok",
                    "data":{
                        "id": ${account.id},
                        "amount": ${account.money}
                    }
                }""", SC_OK
                )
        }
    }

    private fun getAccountId(request: HttpServletRequest): Int? {
        val paths = request.pathInfo.split("/")
        return if(paths.size == 2)
            paths[1].toIntOrNull(10)
        else
            null
    }

}


