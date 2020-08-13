package com.justordercompany.client.base.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.justordercompany.client.R
import com.justordercompany.client.base.FeedDisplayInfo
import com.justordercompany.client.base.LoadBehavior
import com.justordercompany.client.base.data_binding.BuilderBg
import com.justordercompany.client.base.diff.DiffBasketItem
import com.justordercompany.client.base.diff.DiffOrders
import com.justordercompany.client.base.enums.TypeOrderStatus
import com.justordercompany.client.databinding.ItemOrderHistoryBinding
import com.justordercompany.client.extensions.toVisibility
import com.justordercompany.client.logic.models.ModelBasketItem
import com.justordercompany.client.logic.models.ModelOrder
import com.justordercompany.client.logic.utils.DateManager
import com.justordercompany.client.logic.utils.areAtSameDay
import com.justordercompany.client.logic.utils.formatAsMoney
import com.justordercompany.client.logic.utils.formatToString
import com.justordercompany.client.logic.utils.images.GlideManager
import com.justordercompany.client.logic.utils.strings.getRandomString

class AdapterRvOrders : RecyclerView.Adapter<CardOrder>()
{
    private var items: ArrayList<ModelOrder> = arrayListOf()
    var listener: ((ModelOrder) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardOrder
    {
        val inflater = LayoutInflater.from(parent.context)
        val bnd_order: ItemOrderHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.item_order_history, parent, false)
        return CardOrder(bnd_order)
    }

    override fun getItemCount(): Int
    {
        return items.size
    }

    override fun onBindViewHolder(holder: CardOrder, position: Int)
    {
        val order = items.get(position)

        var need_to_show_date = true
        val previous_date = items.getOrNull(position - 1)?.date
        if (previous_date != null && order.date != null)
        {
            need_to_show_date = !areAtSameDay(previous_date, order.date!!)
        }

        holder.bindOrder(order, need_to_show_date)

        holder.bnd_order.cvRoot.setOnClickListener(
            {
                listener?.invoke(order)
            })
    }

    fun setItems(rec_info: FeedDisplayInfo<ModelOrder>)
    {
        if (rec_info.load_behavior == LoadBehavior.FULL_RELOAD)
        {
            this.items = ArrayList(rec_info.items)
            notifyDataSetChanged()
            return
        }

        val diff_callback = DiffOrders(items, rec_info.items)
        val diff_result = DiffUtil.calculateDiff(diff_callback)
        diff_result.dispatchUpdatesTo(this)
        this.items = ArrayList(rec_info.items)
    }
}

class CardOrder(val bnd_order: ItemOrderHistoryBinding) : RecyclerView.ViewHolder(bnd_order.root)
{
    fun bindOrder(order: ModelOrder, show_date: Boolean)
    {
        order.cafe?.logo?.url_m?.let(
            {
                GlideManager.loadImageSimpleCircle(it, bnd_order.imgLogo)
            })

        bnd_order.tvName.text = order.cafe?.name
        bnd_order.tvAdress.text = order.cafe?.address
        order.sum?.let(
            {
                bnd_order.tvSum.text = "${it.formatAsMoney()} р"
            })

        bnd_order.tvDate.text = order.date?.formatToString()
        bnd_order.tvDate.visibility = show_date.toVisibility()

        bnd_order.tvMenu.text = order.getProductNamesList()
        bnd_order.tvTime.text = order.date?.formatToString(DateManager.FORMAT_TIME)

        val drawable: Drawable
        if (order.status == TypeOrderStatus.DONE)
        {
            drawable = BuilderBg.getGradGreen(0f)
        }
        else if (order.status == TypeOrderStatus.CANCELED)
        {
            drawable = BuilderBg.getSimpleGradient(0f, R.color.gray_grad_dark, R.color.gray_grad_light)
        }
        else
        {
            drawable = BuilderBg.getSimpleGradient(0f, R.color.yellow_grad_dark, R.color.yellow_grad_light)
        }

        bnd_order.lalRight.background = drawable
    }
}