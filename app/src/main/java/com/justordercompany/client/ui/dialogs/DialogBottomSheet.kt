package com.justordercompany.client.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.justordercompany.client.R
import com.justordercompany.client.base.data_binding.BuilderBg
import com.justordercompany.client.databinding.DialogBottomSheetBinding
import com.justordercompany.client.extensions.applyTransparency
import com.justordercompany.client.extensions.getColorMy
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.logic.utils.BtnAction
import kotlinx.android.synthetic.main.act_auth.*

class DialogBottomSheet : BottomSheetDialogFragment()
{
    lateinit var bnd_dialog: DialogBottomSheetBinding
    private var btns: ArrayList<BtnAction> = arrayListOf()
    private var show_cancel = true

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        bnd_dialog = DataBindingUtil.inflate(inflater, R.layout.dialog_bottom_sheet, container, false)
        return bnd_dialog.root
    }

    fun setBtns(btns: ArrayList<BtnAction>)
    {
        this.btns = btns
    }

    override fun onStart()
    {
        super.onStart()

        btns.forEachIndexed(
            { index, btn_action ->

                val tv = layoutInflater.inflate(R.layout.tv_bottom_sheet_btn, bnd_dialog.laBtns, false) as TextView

                tv.text = btn_action.text

                val bg = BuilderBg()
                        .setBgColor(getColorMy(R.color.white).applyTransparency(80))
                        .setRippleColor(getColorMy(R.color.orange).applyTransparency(15))
                        .isRipple(true)
                        .isDpMode(true)

                if (btns.size == 0)
                {
                    bg.setCorners(4f)
                }
                else if (index == 0)
                {
                    bg.setCornerRadiuses(4f, 4f, 0f, 0f)
                }
                else if (index == btns.lastIndex)
                {
                    bg.setCornerRadiuses(0f, 0f, 4f, 4f)
                }

                tv.background = bg.get()
                tv.setOnClickListener(
                    {
                        dialog?.dismiss()
                        btn_action.action?.invoke()
                    })

                bnd_dialog.laBtns.addView(tv)
            })


        if (show_cancel)
        {
            val tv_cancel = layoutInflater.inflate(R.layout.tv_bottom_sheet_btn, bnd_dialog.laBtns, false) as TextView
            tv_cancel.text = getStringMy(R.string.cancel)

            val bg = BuilderBg()
                    .setBgColor(getColorMy(R.color.white).applyTransparency(80))
                    .setRippleColor(getColorMy(R.color.orange).applyTransparency(15))
                    .isRipple(true)
                    .setCorners(4f)
                    .isDpMode(true)
                    .get()
            tv_cancel.background = bg

            tv_cancel.setOnClickListener(
                {
                    dialog?.dismiss()
                })

            bnd_dialog.laCancel.addView(tv_cancel)
            bnd_dialog.laCancel.visibility = View.VISIBLE
        }
        else
        {
            bnd_dialog.laCancel.visibility = View.GONE
        }
    }
}