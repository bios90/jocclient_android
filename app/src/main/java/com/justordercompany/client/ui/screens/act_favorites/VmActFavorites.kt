package com.justordercompany.client.ui.screens.act_favorites

import android.content.Intent
import com.justordercompany.client.R
import com.justordercompany.client.base.*
import com.justordercompany.client.base.enums.TypeCafe
import com.justordercompany.client.extensions.Optional
import com.justordercompany.client.extensions.asOptional
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.logic.models.FilterData
import com.justordercompany.client.logic.models.ModelCafe
import io.reactivex.subjects.BehaviorSubject

class VmActFavorites : BaseViewModel()
{
    val bs_cafes: BehaviorSubject<FeedDisplayInfo<ModelCafe>> = BehaviorSubject.create()
    val bs_info_text: BehaviorSubject<Optional<String>> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
    }

    override fun viewAttached()
    {
        base_networker.loadFavorites(
            {
                bs_cafes.onNext(FeedDisplayInfo(it, LoadBehavior.UPDATE))
                if (it.isNullOrEmpty())
                {
                    val text = getStringMy(R.string.you_have_no_favorites)
                    bs_info_text.onNext(text.asOptional())
                }
                else
                {
                    bs_info_text.onNext(Optional(null))
                }
            })
    }

    fun clickedCafe(cafe: ModelCafe)
    {
        val cafe_id = cafe.id ?: return
        val return_intent = Intent()
        return_intent.putExtra(Constants.Extras.EXTRA_CAFE_ID, cafe_id)
        ps_to_finish.onNext(return_intent.asOptional())
    }
}