package ru.izebit.restful.web

import javax.servlet.http.HttpServletResponse

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 12.10.2019
 */
object HttpUtils {

    fun HttpServletResponse.of(body: String, statusCode: Int) {
        this.status = statusCode
        this.contentType = "application/json; charset=UTF-8";
        this.writer.println(body.trimIndent())
    }
}