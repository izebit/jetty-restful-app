package ru.izebit.service

import java.lang.RuntimeException

/**
 * @author <a href="mailto:izebit@gmail.com">Artem Konovalov</a> <br/>
 * Date: 12.10.2019
 */
class AccountServiceException(msg: String) : RuntimeException(msg, null, true, false)