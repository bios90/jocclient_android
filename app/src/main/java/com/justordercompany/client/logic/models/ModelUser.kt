package com.justordercompany.client.logic.models

import android.util.Log
import com.justordercompany.client.extensions.toJsonMy
import java.util.*

class ModelUser : ObjectWithId, ObjectWithDates
{
    override var id: Int? = null
    override var created: Date? = null
    override var updated: Date? = null
    override var deleted: Date? = null

    var name: String? = null
    var email: String? = null
    var api_token: String? = null
    var phone: String? = null
    var image: BaseImage? = null

    fun isEmpty(): Boolean
    {
        Log.e("ModelUser", "isEmpty: ${this.toJsonMy()}")

        return name.isNullOrEmpty() && email.isNullOrEmpty() && image == null
    }
}
