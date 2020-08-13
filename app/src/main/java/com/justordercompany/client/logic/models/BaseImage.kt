package com.justordercompany.client.logic.models

import java.io.Serializable

class BaseImage(
        var width: Int? = null,
        var height: Int? = null,
        var src: String? = null,
        var url_s: String? = null,
        var url_m: String? = null,
        var url_l: String? = null)
    : Serializable, ObjWithImageUrl
{
    override var image_url: String? = null
        get() = url_l
}