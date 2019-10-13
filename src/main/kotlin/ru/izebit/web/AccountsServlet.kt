package ru.izebit.web

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import ru.izebit.data.Account
import ru.izebit.service.AccountService
import ru.izebit.service.AccountServiceException
import ru.izebit.service.Transaction
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
class AccountsServlet constructor(private val accountService: AccountService<Int>) : HttpServlet() {

    /**
     * servlet for handling requests to create new accounts
     * the endpoint has an url format: {@code /accounts}
     * a request body should be a json and has the next format:
     * {@code
     *      {
     *          "id": 0,
     *          "money": 0
     *      }
     * }
     */
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        val account = try {
            Klaxon().parse<Account<Int>>(request.inputStream)
        } catch (ex: KlaxonException) {
            null
        }

        if (account == null)
            response.of(
                """{
                    "status": "fail",
                    "info": "can't parse a body request"
                }""", SC_BAD_REQUEST
            )
        else {
            accountService.save(account)
            response.of(
                """{
                    "status":"success",
                    "data":{
                        "id": ${account.id},
                        "money": ${account.money}
                    }
                }""", SC_OK
            )
        }
    }

    /**
     * servlet for handling requests to create new accounts
     * the endpoint has an url format: {@code /accounts}
     * a request body should be a json and has the next format:
     * {@code
     *      {
     *          "from": 0,
     *          "to": 1,
     *          "amount": 10
     *      }
     * }
     */
    override fun doPut(request: HttpServletRequest, response: HttpServletResponse) {
        val transaction = try {
            Klaxon().parse<Transaction<Int>>(request.inputStream)
        } catch (ex: KlaxonException) {
            null
        }
        if (transaction == null)
            response.of(
                """{
                    "status": "fail",
                    "info": "can't parse a body request"
                }""", SC_BAD_REQUEST
            )
        else
            try {
                accountService.transfer(transaction)
                response.of(
                    """{
                    "status":"success",
                    "data":{
                        "from": ${transaction.from},
                        "to": ${transaction.to},
                        "money": ${transaction.amount}
                    }
                }""", SC_OK
                )
            } catch (ex: AccountServiceException) {
                response.of(
                    """{
                    "status": "fail",
                    "info": "${ex.message}"
                }""", SC_BAD_REQUEST
                )
            }
    }
}


