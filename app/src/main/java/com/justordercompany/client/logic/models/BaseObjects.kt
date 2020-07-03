package com.justordercompany.client.logic.models

import java.util.*

interface ObjectWithId
{
    var id:Int?
}

interface ObjectWithDates
{
    var created: Date?
    var updated: Date?
    var deleted: Date?
}

interface ObjWithImageUrl
{
    var image_url:String?
}