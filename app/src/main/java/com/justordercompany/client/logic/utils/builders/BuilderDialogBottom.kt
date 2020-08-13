package com.justordercompany.client.logic.utils.builders

import com.justordercompany.client.logic.utils.BtnAction

class BuilderDialogBottom
{
    val btns: ArrayList<BtnAction> = ArrayList()
    var show_cancel: Boolean = true
    var cancel_on_touch_outside: Boolean = true
    var dim_background: Boolean = true

    fun addBtn(btn: BtnAction) = apply(
        {
            this.btns.add(btn)
        })

    fun setShowCancel(show_cancel: Boolean) = apply(
        {
            this.show_cancel = show_cancel
        })

    fun setCancelOnTouchOutside(cancel_on_touch_outside: Boolean) = apply(
        {
            this.cancel_on_touch_outside = cancel_on_touch_outside
        })

    fun setDimBackground(dim_background: Boolean) = apply(
        {
            this.dim_background = dim_background
        })
}