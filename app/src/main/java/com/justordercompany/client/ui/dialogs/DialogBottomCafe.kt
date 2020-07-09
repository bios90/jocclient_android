package com.justordercompany.client.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.justordercompany.client.R
import com.justordercompany.client.databinding.ActCafePopupBinding
import com.justordercompany.client.databinding.DialogBottomSheetBinding
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.utils.BtnAction
import com.justordercompany.client.logic.utils.images.GlideManager

class DialogBottomCafe : BottomSheetDialogFragment()
{
    lateinit var bnd_dialog: ActCafePopupBinding
    lateinit var cafe:ModelCafe

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        bnd_dialog = DataBindingUtil.inflate(inflater, R.layout.act_cafe_popup, container, false)
        return bnd_dialog.root
    }


    override fun onStart()
    {
        super.onStart()
        bindCafe(cafe)
    }

    private fun bindCafe(cafe: ModelCafe)
    {
        cafe.logo?.url_l?.let(
            {
                GlideManager.loadImageSimpleCircle(it, bnd_dialog.imgLogo)
            })
        bnd_dialog.tvName.text = cafe.name
        bnd_dialog.tvAdress.text = cafe.address

        cafe.rating?.let(
            {
                bnd_dialog.ratingBar.rating = it
            })

        bnd_dialog.tvDistance.text = "2429384"
        bnd_dialog.tvAbout.text = "aksfbjkafhb jkdfhbgjhdfbla lsadbflsa bhsbdf jahksbjhblhabfgk"

        bnd_dialog.tvTime.text = "25:00 - 26:00"
    }
}