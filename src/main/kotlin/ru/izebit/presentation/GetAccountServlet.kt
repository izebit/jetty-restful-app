package ru.izebit.presentation

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.eclipse.jetty.servlet.ServletHandler
import ru.izebit.data.Account
import ru.izebit.service.AccountService
import javax.servlet.http.HttpServletResponse.*

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */
class GetAccountServlet constructor(private val accountService: AccountService<Int>) : HttpServlet() {

    /**
     * servlet for handling request to get information about accounts
     * the endpoint has an url: {@code /account/{id}}
     * where {id} is an account id
     */
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val idParameterValue = request.requestURL.split("//").last()
        val id = idParameterValue.toIntOrNull(10)
        if (id == null) {
            response.of(
                """{
                    "status": "fail",
                    "info": "can't parse account id: $idParameterValue"
                }""", SC_BAD_REQUEST)
        } else {
            val account = accountService.get(id)
            if (account == null)
                response.of(
                    """{
                    "status": "fail",
                    "info": "can't found an account with id: $id"
                }""", SC_NOT_FOUND)
            else
                response.of(
                    """{
                    "status": "ok",
                    "data":{
                        "id": ${account.id},
                        "amount": ${account.money}
                    }
                }""", SC_OK)
        }
    }

    private fun HttpServletResponse.of(body: String, statusCode: Int) {
        this.status = statusCode
        this.writer.println(body.trimIndent())
        this.contentType = "application/json; charset=UTF-8";
    }
}


