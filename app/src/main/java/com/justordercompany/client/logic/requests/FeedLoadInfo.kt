package com.justordercompany.client.logic.requests

import com.justordercompany.client.logic.models.ModelOrder

open class FeedLoadInfo<T>(val offset: Int, val limit: Int, var action_success: (ArrayList<T>) -> Unit)


