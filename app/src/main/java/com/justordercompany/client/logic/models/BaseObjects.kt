package com.justordercompany.client.logic.models

import java.util.*
import kotlin.collections.ArrayList

interface ObjectWithId
{
    var id: Int?
}

fun <T : ObjectWithId> ArrayList<out ObjectWithId>.findById(id: Int): T?
{
    this.forEach(
        { obj ->
            if (obj.id == id)
            {
                return@findById obj as? T
            }
        })

    return null
}


interface ObjectWithDates
{
    var created: Date?
    var updated: Date?
    var deleted: Date?
}

interface ObjWithImageUrl
{
    var image_url: String?
}