package ru.izebit.restful.web

import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.izebit.restful.data.InMemoryAccountStore
import ru.izebit.restful.service.AccountServiceImpl
import ru.izebit.restful.service.TransactionService
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.ThreadLocalRandom

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 13.10.2019
 */
class AccountServletTest {
    private val port = ThreadLocalRandom.current().nextLong(9000, 9100).toInt()
    private lateinit var httpServer: HttpServer
    @BeforeEach
    fun setUp() {
        httpServer = HttpServer(
            port = port,
            accountService = AccountServiceImpl(InMemoryAccountStore(), TransactionService())
        )
        httpServer.start()
    }

    @Test
    fun testTransferMoney_not_enough_money() {
        createAccount(1, 100.0)
        createAccount(2, 100.0)

        val (_, transferMoneyResponse, _) = "http://127.0.0.1:$port/accounts"
            .httpPut()
            .jsonBody(
                """
                {
                    "from": 1,
                    "to": 2,
                    "money": 101.0
                }
            """, UTF_8
            )
            .responseString()
        assert(transferMoneyResponse.statusCode == 400)

        val createAccountResponseBody = String(transferMoneyResponse.body().toByteArray(), UTF_8)
        assertString(
            """{
                    "status": "fail",
                    "info": "An account with id: 1 doesn't have enough money"
                }"""
            , createAccountResponseBody
        )
    }

    @Test
    fun testTransferMoney_success() {
        createAccount(1, 100.0)
        createAccount(2, 100.0)


        val (_, transferMoneyResponse, _) = "http://127.0.0.1:$port/accounts"
            .httpPut()
            .jsonBody(
                """
                {
                    "from": 1,
                    "to": 2,
                    "money": 20.0
                }
            """, UTF_8
            )
            .responseString()
        assert(transferMoneyResponse.statusCode == 200)

        val transferMoneyResponseBody = String(transferMoneyResponse.body().toByteArray(), UTF_8)
        assertString(
            """{
                    "status":"success",
                    "data":{
                        "from": 1,
                        "to": 2,
                        "money": 20.0
                    }
                }"""
            , transferMoneyResponseBody
        )

        val (_, getFirstAccountInfoResponse, _) = "http://127.0.0.1:$port/accounts/1"
            .httpGet()
            .responseString()
        assert(getFirstAccountInfoResponse.statusCode == 200)

        val getFirstAccountInfoResponseBody = String(getFirstAccountInfoResponse.body().toByteArray(), UTF_8)
        assertString(
            """{
                    "status": "ok",
                    "data":{
                        "id": 1,
                        "money": 80.0
                    }
                }""", getFirstAccountInfoResponseBody
        )

        val (_, getSecondAccountInfoResponse, _) = "http://127.0.0.1:$port/accounts/2"
            .httpGet()
            .responseString()
        assert(getSecondAccountInfoResponse.statusCode == 200)

        val getSecondAccountResponseBody = String(getSecondAccountInfoResponse.body().toByteArray(), UTF_8)
        assertString(
            """{
                    "status": "ok",
                    "data":{
                        "id": 2,
                        "money": 120.0
                    }
                }""", getSecondAccountResponseBody
        )
    }

    @Test
    fun testTransferMoney_bad_format() {
        val (_, transferMoneyResponse, _) = "http://127.0.0.1:$port/accounts"
            .httpPut()
            .jsonBody(
                """
                {
                    "from": 10,
                    "to": my_account_id,
                    "money: 10.0
                }
            """, UTF_8
            )
            .responseString()
        assert(transferMoneyResponse.statusCode == 400)

        val createAccountResponseBody = String(transferMoneyResponse.body().toByteArray(), UTF_8)
        assertString(
            """{
                    "status": "fail",
                    "info": "can't parse a body request"
                }"""
            , createAccountResponseBody
        )
    }


    @Test
    fun testCreateNewAccount_bad_format() {
        val createAccountResponse = createAccount(10, "a lot of money")
        assert(createAccountResponse.statusCode == 400)

        val createAccountResponseBody = String(createAccountResponse.body().toByteArray(), UTF_8)
        assertString(
            """{
                    "status": "fail",
                    "info": "can't parse a body request"
                }"""
            , createAccountResponseBody
        )
    }


    @Test
    fun testCreateNewAccount_success() {
        val accountId = 10
        val money = 0.1

        val createAccountResponse = createAccount(accountId, money)
        assert(createAccountResponse.statusCode == 200)

        val createAccountResponseBody = String(createAccountResponse.body().toByteArray(), UTF_8)
        assertString(
            """{
                    "status":"success",
                    "data":{
                        "id": $accountId,
                        "money": $money
                    }
                }""", createAccountResponseBody
        )

        val (_, getAccountInfoResponse, _) = "http://127.0.0.1:$port/accounts/$accountId"
            .httpGet()
            .responseString()
        assert(getAccountInfoResponse.statusCode == 200)

        val getAccountInfoResponseBody = String(getAccountInfoResponse.body().toByteArray(), UTF_8)
        assertString(
            """{
                    "status": "ok",
                    "data":{
                        "id": $accountId,
                        "money": $money
                    }
                }""", getAccountInfoResponseBody
        )
    }


    @Test
    fun testGetAccountInfo_bad_param() {
        val (_, response, _) = "http://127.0.0.1:$port/accounts/a"
            .httpGet()
            .responseString()
        assert(response.statusCode == 400)

        val responseBody = String(response.body().toByteArray(), UTF_8)
        assertString(
            """
                {
                    "status": "fail",
                    "info": "can't parse account id: /a"
                }""", responseBody
        )
    }


    @Test
    fun testGetAccountInfo_not_found() {
        val (_, response, _) = "http://127.0.0.1:$port/accounts/1"
            .httpGet()
            .responseString()
        assert(response.statusCode == 404)

        val responseBody = String(response.body().toByteArray(), UTF_8)
        assertString(
            """
            {
                    "status": "fail",
                    "info": "can't found an account with id: 1"
            }
        """, responseBody
        )
    }


    @AfterEach
    fun tearDown() {
        httpServer.stop()
    }

    private fun assertString(v1: String, v2: String) {
        val v3 = v1.replace(Regex("\\s{2,}|\\n"), " ").trim()
        val v4 = v2.replace(Regex("\\s{2,}|\\n"), " ").trim()
        assert(v3 == v4) { "$v3 doesn't equal $v4" }
    }

    private fun createAccount(id: Int, money: Any): Response {
        val (_, createAccountResponse, _) = "http://127.0.0.1:$port/accounts"
            .httpPost()
            .jsonBody(
                """
                {
                    "id": $id,
                    "money": $money
                }
            """, UTF_8
            )
            .responseString()

        return createAccountResponse
    }
}