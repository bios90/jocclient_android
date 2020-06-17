package com.justordercompany.client.networking

import java.lang.RuntimeException

class NoInternetException : RuntimeException("*** No internett ****")
class UnknownServerError : RuntimeException("*** Errr server ***")
class ParsingError : RuntimeException("*** Err parsing ****")
class ServerError(override val message: String) : RuntimeException()

