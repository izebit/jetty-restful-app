package ru.izebit.presentation

import com.beust.klaxon.Klaxon
import ru.izebit.presentation.HttpUtils.of
import ru.izebit.service.AccountService
import ru.izebit.service.AccountServiceException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.*

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 11.10.2019
 */
class TransferMoneyServlet constructor(private val accountService: AccountService<Int>) : HttpServlet() {

    /**
     * servlet for handling requests to create new accounts
     * the endpoint has an url: {@code /accounts}
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
        val transaction = Klaxon().parse<Transaction>(request.inputStream)
        if (transaction == null)
            response.of(
                """{
                    "status": "fail",
                    "info": "can't parse body request"
                }""", SC_BAD_REQUEST)
        else
            try {
                accountService.transfer(transaction.from, transaction.to, transaction.amount)
                response.of(
                    """{
                    "status":"success",
                    "data":{
                        "from": ${transaction.from},
                        "to": ${transaction.to},
                        "money": ${transaction.amount}
                    }
                }""", SC_OK)
            } catch (ex: AccountServiceException) {
                response.of(
                    """{
                    "status": "fail",
                    "info": "${ex.message}"
                }""", SC_INTERNAL_SERVER_ERROR)
            }
    }

    data class Transaction(
        val from: Int,
        val to: Int,
        val amount: Double
    )
}


