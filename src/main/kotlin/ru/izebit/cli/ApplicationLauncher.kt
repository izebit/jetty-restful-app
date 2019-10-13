package ru.izebit.cli

import com.beust.jcommander.JCommander

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 10.10.2019
 */

fun main(args: Array<String>) {
    val arguments = CommandArguments()
    val argv = arrayOf("--threads", "2", "--port", "8080")
    JCommander.newBuilder()
        .addObject(arguments)
        .build()
        .parse(*argv)

    println()
}