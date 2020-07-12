package com.justordercompany.client.base

import android.util.Log
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.requests.ReqCafes
import com.justordercompany.client.logic.responses.RespCafeSingle
import com.justordercompany.client.logic.responses.RespCafes

class BaseNetworker(private val base_vm: BaseViewModel)
{
    fun loadCafeSingle(id: Int, action_success: (ModelCafe) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        base_vm.api_cafe.getCafeSingle(id)
                .mainThreaded()
                .addMyParser<RespCafeSingle>(RespCafeSingle::class.java)
                .addProgress(base_vm)
                .addScreenDisabling(base_vm)
                .addErrorCatcher(base_vm)
                .addParseChecker({ it.cafe != null })
                .subscribeMy(
                    {
                        action_success(it.cafe!!)
                    },
                    {
                        action_error?.invoke(it)
                        Log.e("BaseNetworker", "**** Got error on loading cafe single ****")
                    })
                .disposeBy(base_vm.composite_disposable)
    }

    fun loadCafes(req: ReqCafes, action_success: (ArrayList<ModelCafe>) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        req.getRequest(base_vm.api_cafe)
                .mainThreaded()
                .addMyParser<RespCafes>(RespCafes::class.java)
                .addProgress(base_vm)
                .addScreenDisabling(base_vm)
                .addErrorCatcher(base_vm)
                .addParseChecker({ it.data?.cafes != null })
                .subscribeMy(
                    {
                        val cafes = it.data?.cafes!!
                        action_success(cafes)
                    },
                    {
                        action_error?.invoke(it)
                        Log.e("BaseNetworker", "**** Got error on loading cafes list ****")
                    })
                .disposeBy(base_vm.composite_disposable)
    }
}