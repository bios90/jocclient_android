package com.justordercompany.client.ui.custom_views.bubble_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import com.justordercompany.client.R
import com.justordercompany.client.databinding.LaAddableBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelAddableValue
import com.justordercompany.client.ui.custom_views.BaseView
import io.reactivex.disposables.CompositeDisposable

class BubbleView(inflater: LayoutInflater, parent: ViewGroup) : BaseView(inflater, parent)
{
    val vm_bubble: VmBubble
    val bnd_bubble_view: LaAddableBinding
    val composite_disposable = CompositeDisposable()

    init
    {
        vm_bubble = VmBubble()
        bnd_bubble_view = DataBindingUtil.inflate(inflater, R.layout.la_addable, parent, false)
        setEvents()
    }

    fun setEvents()
    {
        vm_bubble.bs_title_text
                .mainThreaded()
                .subscribe(
                    {
                        bnd_bubble_view.tvName.text = it
                    })
                .disposeBy(composite_disposable)

        vm_bubble.bs_price_text
                .mainThreaded()
                .subscribe(
                    {
                        bnd_bubble_view.tvPlusPrice.text = it
                    })
                .disposeBy(composite_disposable)

        vm_bubble.bs_addables
                .mainThreaded()
                .subscribe(
                    {
                        setBubbles(it)
                    })
                .disposeBy(composite_disposable)

        vm_bubble.bs_selected_pos
                .mainThreaded()
                .subscribe(
                    {
                        val views_count = bnd_bubble_view.lalBubbles.childCount
                        for (i in 0 until views_count)
                        {
                            val view = bnd_bubble_view.lalBubbles.get(i) as TextView
                            if (it.contains(i))
                            {
                                view.background = getRippleDrawableMy(R.drawable.ripple_round_empty_orange)
                                view.setTextColor(getColorMy(R.color.orange))
                                view.typeface = getTypeFaceFromResource(R.font.exo_bold)
                            }
                            else
                            {
                                view.background = getRippleDrawableMy(R.drawable.ripple_round_trans)
                                view.setTextColor(getColorMy(R.color.gray4))
                                view.typeface = getTypeFaceFromResource(R.font.exo_reg)
                            }
                        }
                    })
                .disposeBy(composite_disposable)
    }

    fun setBubbles(addables: ArrayList<ModelAddableValue>)
    {
        bnd_bubble_view.lalBubbles.removeAllViews()

        addables.forEachIndexed(
            { pos, addable_view ->

                val tv = inflater.inflate(R.layout.tv_bubble, bnd_bubble_view.lalBubbles, false) as TextView
                tv.text = addable_view.value

                tv.setOnClickListener(
                    {
                        vm_bubble.clickedBubble(pos)
                    })

                bnd_bubble_view.lalBubbles.addView(tv)
            })
    }

    override fun getView(): View
    {
        return bnd_bubble_view.root
    }
}