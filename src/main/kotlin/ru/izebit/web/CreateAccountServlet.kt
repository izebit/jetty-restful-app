package ru.izebit.web

import com.beust.klaxon.Klaxon
import ru.izebit.data.Account
import ru.izebit.service.AccountService
import ru.izebit.web.HttpUtils.of
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST
import javax.servlet.http.HttpServletResponse.SC_OK

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */
class CreateAccountServlet constructor(private val accountService: AccountService<Int>) : HttpServlet() {

    /**
     * servlet for handling requests to create new accounts
     * the endpoint has an url: {@code /accounts}
     * a request body should be a json and has the next format:
     * {@code
     *      {
     *          "id": 0,
     *          "money": 0
     *      }
     * }
     */
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
            val account = Klaxon().parse<Account<Int>>(request.inputStream)
            if (account == null)
                response.of(
                    """{
                    "status": "fail",
                    "info": "can't parse body request"
                }""", SC_BAD_REQUEST)
            else{
                accountService.save(account)
                response.of("""{
                    "status":"success",
                    "data":{
                        "id": ${account.id},
                        "money": ${account.money}
                    }
                }""", SC_OK)
            }
    }
}


