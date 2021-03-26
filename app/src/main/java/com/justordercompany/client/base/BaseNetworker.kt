package com.justordercompany.client.base

import android.util.Log
import com.justordercompany.client.base.AppClass.Companion.composite_disposable
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelOrder
import com.justordercompany.client.logic.models.ModelUser
import com.justordercompany.client.logic.requests.FeedLoadInfo
import com.justordercompany.client.logic.requests.ReqCafes
import com.justordercompany.client.logic.responses.*

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

    fun makeOrder(date: String, comment: String?, items: String, action_success: (Int) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        base_vm.api_orders.createOrder(date, comment, items)
                .mainThreaded()
                .addMyParser<RespBaseWithData>(RespBaseWithData::class.java)
                .addProgress(base_vm)
                .addScreenDisabling(base_vm)
                .addErrorCatcher(base_vm)
                .addParseChecker(
                    {

                        return@addParseChecker it.getInt("id") != null
                    })
                .subscribeMy(
                    {
                        action_success(it.getInt("id")!!)
                    },
                    {
                        action_error?.invoke(it)
                    })
                .disposeBy(base_vm.composite_disposable)
    }

    fun payOrder(token: String, order_id: Int, action_success: (RespBaseWithData) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        base_vm.api_orders.makePay(order_id, token)
                .mainThreaded()
                .addMyParser<RespBaseWithData>(RespBaseWithData::class.java)
                .addProgress(base_vm)
                .addScreenDisabling(base_vm)
                .addErrorCatcher(base_vm)
                .subscribeMy(
                    {
                        action_success(it)
                    },
                    {
                        action_error?.invoke(it)
                    })
                .disposeBy(base_vm.composite_disposable)
    }

    fun getOrderInfo(order_id: Int, action_success: (ModelOrder) -> Unit, action_error: ((Throwable) -> Unit)? = null, default_progress: Boolean = true)
    {
        val builder = base_vm.api_orders.getOrderInfo(order_id)
                .mainThreaded()
                .addMyParser<RespOrderSingle>(RespOrderSingle::class.java)

        if (default_progress)
        {
            builder.addProgress(base_vm)
        }

        builder.addScreenDisabling(base_vm)
                .addErrorCatcher(base_vm)
                .addParseChecker({ it.order != null })
                .subscribeMy(
                    {
                        action_success(it.order!!)
                    },
                    {
                        it.printStackTrace()
                        action_error?.invoke(it)
                    })
                .disposeBy(base_vm.composite_disposable)
    }

    fun loadOrders(info: FeedLoadInfo<ModelOrder>)
    {
        base_vm.api_orders.getUserOrders(info.offset, info.limit)
                .mainThreaded()
                .addMyParser<RespOrders>(RespOrders::class.java)
                .addProgress(base_vm)
                .addScreenDisabling(base_vm)
                .addErrorCatcher(base_vm)
                .subscribeMy(
                    {
                        if (it.orders == null)
                        {
                            it.orders = arrayListOf()
                        }
                        info.action_success(it.orders!!)
                    },
                    {
                        it.printStackTrace()
//                        action_error?.invoke(it)
                    })
                .disposeBy(base_vm.composite_disposable)
    }

    fun makeOrderReview(cafe_id: Int, order_id: Int, text: String?, rating: Int, action_success: () -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        base_vm.api_orders.makeReview(cafe_id, order_id, text, rating)
                .mainThreaded()
                .addMyParser<RespBaseWithData>(RespBaseWithData::class.java)
                .addProgress(base_vm)
                .addScreenDisabling(base_vm)
                .addErrorCatcher(base_vm)
                .subscribeMy(
                    {
                        action_success()
                    },
                    {
                        action_error?.invoke(it)
                    })
                .disposeBy(base_vm.composite_disposable)
    }

    fun makeAuth(phone: String, fb_token: String?, action_success: () -> Unit)
    {
        base_vm.api_auth.makeAuth(phone, fb_token)
                .mainThreaded()
                .addMyParser<BaseResponse>(BaseResponse::class.java)
                .addProgress(base_vm)
                .addScreenDisabling(base_vm)
                .addErrorCatcher(base_vm)
                .subscribeMy(
                    {
                        action_success()
                    })
                .disposeBy(composite_disposable)
    }

    fun makeCodeConfirm(phone: String, code: String, action_success: (ModelUser) -> Unit)
    {
        base_vm.api_auth.confirmPhone(phone, code)
                .mainThreaded()
                .addMyParser<RespUserSingle>(RespUserSingle::class.java)
                .addProgress(base_vm)
                .addScreenDisabling(base_vm)
                .addErrorCatcher(base_vm)
                .addParseChecker(
                    {
                        return@addParseChecker it.user != null
                    })
                .subscribeMy(
                    {
                        action_success(it.user!!)
                    })
                .disposeBy(composite_disposable)
    }

    fun loadUser(action_success: (ModelUser) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        base_vm.api_auth.getUser()
                .mainThreaded()
                .addMyParser<RespUserSingle>(RespUserSingle::class.java)
                .addProgress(base_vm)
                .addScreenDisabling(base_vm)
                .addErrorCatcher(base_vm)
                .addParseChecker(
                    {
                        return@addParseChecker it.user != null
                    })
                .subscribeMy(
                    {
                        Log.e("BaseNetworker", "loadUser: Successs on loading user ${it.user.toJsonMy()}")
                        action_success(it.user!!)
                    },
                    {

                        Log.e("BaseNetworker", "loadUser: Error on loading user")
                        action_error?.invoke(it)
                    })
                .disposeBy(composite_disposable)
    }

    fun cancelOrder(order_id: Int, action_success: () -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        base_vm.api_orders.cancelOrder(order_id)
                .mainThreaded()
                .addMyParser<BaseResponse>(BaseResponse::class.java)
                .addProgress(base_vm)
                .addScreenDisabling(base_vm)
                .addErrorCatcher(base_vm)
                .subscribeMy(
                    {
                        action_success()
                    },
                    {
                        action_error?.invoke(it)
                    })
                .disposeBy(composite_disposable)
    }
}