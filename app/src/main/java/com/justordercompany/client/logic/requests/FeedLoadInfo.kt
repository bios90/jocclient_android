package com.justordercompany.client.logic.requests

import com.justordercompany.client.logic.models.ModelOrder

open class FeedLoadInfo<T>(val offset: Int, val limit: Int, val action_success: (ArrayList<T>) -> Unit)
open class FeedLoadInfoPaged<T>(val page: Int, val limit: Int, val action_success: (ArrayList<T>) -> Unit)

