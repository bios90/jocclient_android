package com.justordercompany.client.logic.utils.builders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.github.florent37.inlineactivityresult.kotlin.startForResult
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.enums.TypeActivityAnim
import com.justordercompany.client.extensions.myPutExtra
import java.lang.RuntimeException

class BuilderIntent()
{
    enum class TypeSlider
    {
        BOTTOM_UP
    }

    private var class_to_start: Class<out AppCompatActivity>? = null
    private var ok_lambda: ((Intent?) -> Unit)? = null
    private var cancel_lambda: ((Intent?) -> Unit)? = null
    private var params: ArrayList<Pair<String, Any?>> = ArrayList()
    private var flags: ArrayList<Int> = ArrayList()
    private var on_start_action: (() -> Unit)? = null
    private var type_anim: TypeActivityAnim? = null
    private var slider: TypeSlider? = null

    fun setActivityToStart(act_class: Class<out AppCompatActivity>): BuilderIntent
    {
        class_to_start = act_class
        return this
    }

    fun setOkAction(action: (Intent?) -> Unit): BuilderIntent
    {
        ok_lambda = action
        return this
    }

    fun setCancelAction(action: (Intent?) -> Unit): BuilderIntent
    {
        cancel_lambda = action
        return this
    }

    fun addParam(param: Pair<String, Any?>): BuilderIntent
    {
        params.add(param)
        return this
    }


    fun addParam(name: String, obj: Any?): BuilderIntent
    {
        params.add(Pair(name, obj))
        return this
    }

    fun addFlag(flag: Int): BuilderIntent
    {
        flags.add(flag)
        return this
    }

    fun addOnStartAction(action: () -> Unit): BuilderIntent
    {
        this.on_start_action = action
        return this
    }

    fun addActivityAnim(anim: TypeActivityAnim?): BuilderIntent
    {
        this.type_anim = anim
        return this
    }

    fun setSlider(slider: TypeSlider?): BuilderIntent
    {
        this.slider = slider
        return this
    }

    fun sendInVm(base_vm: BaseViewModel)
    {
        base_vm.ps_intent_builded.onNext(this)
    }

    fun startActivity(activity_from: AppCompatActivity)
    {
        if (class_to_start == null)
        {
            throw RuntimeException("***** Error no class to Start!!!! ****")
        }

        val intent = Intent(activity_from, class_to_start)

        params.forEach(
            { pair ->

                pair.second?.let(
                    { value ->
                        intent.myPutExtra(pair.first, value)
                    })
            })

        flags.forEach(
            { flag ->

                intent.addFlags(flag)
            })

        if (ok_lambda != null || cancel_lambda != null)
        {
            activity_from.startForResult(intent)
            { result ->

                ok_lambda?.invoke(result.data)
            }.onFailed(
                { result ->

                    cancel_lambda?.invoke(result.data)
                })
        }
        else
        {
            activity_from.startActivity(intent)
        }

        slider?.let(
            {
                when (it)
                {
                    TypeSlider.BOTTOM_UP -> Animatoo.animateSlideUp(activity_from)
                }
            })

        on_start_action?.let({ it.invoke() })
        type_anim?.let(
            {
                it.animateWithActivity(activity_from)
            })
    }
}