package com.justordercompany.client.logic.utils.builders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.justordercompany.client.R
import com.justordercompany.client.extensions.getNullableText
import com.justordercompany.client.extensions.makeTransparentBg
import com.justordercompany.client.extensions.setTextHtml
import com.justordercompany.client.logic.utils.BtnAction
import com.justordercompany.client.logic.utils.BtnActionWithText
import java.lang.RuntimeException

class BuilderDialogMy()
{
    private var view: View? = null
    private var view_id: Int? = null
    private var title: String? = null
    private var text: String? = null
    private var btn_ok: BtnAction? = null
    private var btn_cancel: BtnAction? = null
    private var btn_left: BtnAction? = null
    private var btn_ok_text: BtnActionWithText? = null
    private var btn_cancel_text: BtnActionWithText? = null
    private var btn_left_text: BtnActionWithText? = null
    private var current_text: String? = null
    private var dismiss_on_touch_outside = false
    private var cancelable = true
    private var dismiss_on_btn_touch = true
    private var is_html: Boolean = false
    private var dialog: AlertDialog? = null

    fun setView(view: View) = apply(
        {
            this.view = view
            this.view_id = null
        })

    fun setViewId(view_id: Int) = apply(
        {
            this.view_id = view_id
            this.view = null
        })

    fun setTitle(title: String) = apply(
        {
            this.title = title
        })

    fun setText(text: String) = apply(
        {
            this.text = text
        })

    fun setBtnOk(btn_ok: BtnAction) = apply(
        {
            this.btn_ok_text = null
            this.btn_ok = btn_ok
        })

    fun setBtnCancel(btn_cancel: BtnAction) = apply(
        {
            this.btn_cancel_text = null
            this.btn_cancel = btn_cancel
        })

    fun setBtnLeft(btn_left: BtnAction) = apply(
        {
            this.btn_left_text = null
            this.btn_left = btn_left
        })

    fun setBtnOkWithText(btn_ok: BtnActionWithText) = apply(
        {
            this.btn_ok_text = btn_ok
            this.btn_ok = null
        })

    fun setBtnCancelWithText(btn_cancel: BtnActionWithText) = apply(
        {
            this.btn_cancel_text = btn_cancel
            this.btn_cancel = null
        })

    fun setBtnLeft(btn_left: BtnActionWithText) = apply(
        {
            this.btn_left_text = btn_left
            this.btn_left = null
        })

    fun setCurrentText(text: String?) = apply(
        {
            this.current_text = text
        })

    fun setDismissOnTouchOutside(dismiss_on_touch_outside: Boolean) = apply(
        {
            this.dismiss_on_touch_outside = dismiss_on_touch_outside
        })

    fun setCancalable(cancelable: Boolean) = apply(
        {
            this.cancelable = cancelable
        })

    fun setIsHtml(is_html: Boolean) = apply(
        {
            this.is_html = is_html
        })

    fun build(context: Context)
    {
        if (view == null && view_id == null)
        {
            throw RuntimeException("**** Error view or view_id should be passed ****")
        }

        if (view == null)
        {
            val inflater = LayoutInflater.from(context)
            val bnd = DataBindingUtil.inflate(inflater, view_id!!, null, false) as ViewDataBinding
            view = bnd.root
        }

        dialog = AlertDialog.Builder(context).create()
        dialog?.setView(view)
        dialog?.makeTransparentBg()
        dialog?.setCanceledOnTouchOutside(dismiss_on_touch_outside)

        val tv_title = view!!.findViewById(R.id.tv_title) as? TextView
        val tv_text = view!!.findViewById(R.id.tv_text) as? TextView
        val tv_ok = view!!.findViewById(R.id.tv_ok) as? TextView
        val tv_cancel = view!!.findViewById(R.id.tv_cancel) as? TextView
        val tv_left = view!!.findViewById(R.id.tv_left) as? TextView
        val et_et = view!!.findViewById(R.id.et_et) as? EditText

        tv_title?.text = title
        tv_text?.text = text

        if (is_html)
        {
            if (text != null)
            {
                tv_text?.setTextHtml(text!!)
            }
        }
        else
        {
            tv_text?.text = text
        }


        applyTextAndActions(tv_ok, et_et, btn_ok, btn_ok_text)
        applyTextAndActions(tv_cancel, et_et, btn_cancel, btn_cancel_text)
        applyTextAndActions(tv_left, et_et, btn_left, btn_left_text)

        et_et?.setText(current_text)

        dialog?.show()
    }

    private fun dismissAfterTouchIfNeeded()
    {
        if (dismiss_on_btn_touch)
        {
            dialog?.dismiss()
        }
    }

    private fun applyTextAndActions(tv: TextView?, et_et: EditText?, btn: BtnAction?, btn_with_text: BtnActionWithText?)
    {
        if (btn != null)
        {
            tv?.text = btn.text
            tv?.setOnClickListener(
                {
                    btn.action?.invoke()
                    dismissAfterTouchIfNeeded()
                })
        }
        else if (btn_with_text != null)
        {
            tv?.text = btn_with_text.text
            tv?.setOnClickListener(
                {
                    val text = et_et?.getNullableText()
                    btn_with_text.action?.invoke(text)
                    dismissAfterTouchIfNeeded()
                })
        }
        else
        {
            tv?.visibility = View.GONE
        }
    }
}