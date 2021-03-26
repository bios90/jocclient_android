package com.justordercompany.client.logic.models

import android.util.Log
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

fun <T : ObjectWithId> ArrayList<T>.replaceWith(new_obj: T): Boolean
{
    val new_id = new_obj.id ?: return false

    this.forEachIndexed(
        { index, obj_in_list ->

            if (obj_in_list.id == new_id)
            {
                this.removeAt(index)
                this.add(index,new_obj)
                return true
            }
        })

    return false
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