package ru.izebit.cli

import com.beust.jcommander.IParameterValidator
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException


/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 14.10.2019
 */

class CommandArguments {
    @Parameter(
        names = ["--port", "-p"],
        description = "web server http port, default value is 8080",
        required = true,
        validateWith = [PortValidator::class]
    )
    var port: Int = 8080

    @Parameter(
        names = ["--threads", "-t"],
        description = "thread counts, default value is 10",
        required = true,
        validateWith = [ThreadLimitValidator::class]
    )
    var threads: Int = 10
}

class PortValidator : IParameterValidator {
    override fun validate(name: String, value: String) {
        val port = value.toIntOrNull(10)
            ?: throw ParameterException("A parameter $name should be positive number (found $value)")

        if (port < 1024 || port > 65535)
            throw ParameterException("A port value is out of the legal range. A value can't be less than 1024 and more than 65535")
    }
}

class ThreadLimitValidator : IParameterValidator {
    override fun validate(name: String, value: String) {
        val port = value.toIntOrNull(10)
            ?: throw ParameterException("A parameter $name should be positive number (found $value)")

        if (port < 1 || port > 1000)
            throw ParameterException("A count of threads could be from range 1 to 1000, not more or less")
    }
}