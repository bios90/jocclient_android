package com.justordercompany.client.ui.custom_views.bubble_view

import androidx.lifecycle.ViewModel
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.logic.models.ModelAddableValue
import io.reactivex.subjects.BehaviorSubject

class VmBubble : ViewModel()
{
    val bs_title_text: BehaviorSubject<String> = BehaviorSubject.create()
    val bs_addables: BehaviorSubject<ArrayList<ModelAddableValue>> = BehaviorSubject.create()
    val bs_selected_pos: BehaviorSubject<ArrayList<Int>> = BehaviorSubject.createDefault(ArrayList())
    val bs_multi_selectable: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    val bs_price_text: BehaviorSubject<String> = BehaviorSubject.create()

    fun clickedBubble(pos: Int)
    {
        val positions = bs_selected_pos.value!!

        if (bs_multi_selectable.value == true)
        {
            if (positions.contains(pos))
            {
                positions.remove(pos)
            }
            else
            {
                positions.add(pos)
            }
        }
        else
        {
            if(positions.contains(pos))
            {
                return
            }

            positions.clear()
            positions.add(pos)
        }

        bs_selected_pos.onNext(positions)
    }
}