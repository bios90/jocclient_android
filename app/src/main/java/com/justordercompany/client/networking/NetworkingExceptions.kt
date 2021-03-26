package com.justordercompany.client.networking

import com.justordercompany.client.R
import com.justordercompany.client.extensions.getStringMy
import java.lang.RuntimeException

class NoInternetException : RuntimeException(getStringMy(R.string.err_no_internet))
class UnknownServerError : RuntimeException(getStringMy(R.string.err_server_internal))
class ParsingError : RuntimeException(getStringMy(R.string.err_parsing))
class ServerError(override val message: String) : RuntimeException()

