package com.justordercompany.client.base.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.justordercompany.client.R
import com.justordercompany.client.base.FeedDisplayInfo
import com.justordercompany.client.base.LoadBehavior
import com.justordercompany.client.base.diff.DiffBasketItem
import com.justordercompany.client.base.diff.DiffProducts
import com.justordercompany.client.databinding.ItemBasketBinding
import com.justordercompany.client.logic.models.ModelBasketItem
import com.justordercompany.client.logic.models.ModelProduct
import com.justordercompany.client.logic.utils.images.GlideManager
import androidx.cardview.widget.CardView
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout
import com.justordercompany.client.extensions.isVisibleOnScreen
import com.justordercompany.client.extensions.runActionWithDelay
import com.justordercompany.client.extensions.simulateSwipeLeft
import com.justordercompany.client.extensions.simulateSwipeRight
import com.justordercompany.client.logic.utils.formatAsMoney


class AdapterRvBasket : RecyclerView.Adapter<AdapterRvBasket.CardBasketItem>()
{
    var action_clicked_delete:((ModelBasketItem)->Unit)? = null
    var action_clicked_edit:((ModelBasketItem)->Unit)? = null

    var items: ArrayList<ModelBasketItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardBasketItem
    {
        val inflater = LayoutInflater.from(parent.context)
        val bnd: ItemBasketBinding = DataBindingUtil.inflate(inflater, R.layout.item_basket, parent, false)
        return CardBasketItem(bnd.root)
    }

    override fun getItemCount(): Int
    {
        return items.size
    }

    override fun onBindViewHolder(holder: CardBasketItem, position: Int)
    {
        val item = items.get(position)
        holder.bindItem(item)

        holder.cv_content.setOnClickListener(
            {
                holder.toggleSwipeLa()
            })

        holder.tv_edit.setOnClickListener(
            {
                holder.toggleSwipeLa()
                action_clicked_edit?.invoke(item)
            })

        holder.tv_delete.setOnClickListener(
            {
                holder.toggleSwipeLa()
                action_clicked_delete?.invoke(item)
            })
    }

    fun setItems(rec_info: FeedDisplayInfo<ModelBasketItem>)
    {
        if (rec_info.load_behavior == LoadBehavior.FULL_RELOAD)
        {
            this.items = ArrayList(rec_info.items)
            notifyDataSetChanged()
            return
        }

        val diff_callback = DiffBasketItem(items, rec_info.items)
        val diff_result = DiffUtil.calculateDiff(diff_callback)
        diff_result.dispatchUpdatesTo(this)
        this.items = ArrayList(rec_info.items)
    }

    class CardBasketItem(view: View) : RecyclerView.ViewHolder(view)
    {
        val la_swipe: EasySwipeMenuLayout
        val cv_content: CardView
        val img_logo: ImageView
        val tv_name: TextView
        val tv_price: TextView
        val tv_weight: TextView
        val tv_sugar: TextView
        val tv_milk: TextView
        val tv_delete: TextView
        val tv_edit: TextView

        init
        {
            la_swipe = view.findViewById(R.id.la_swipe)
            cv_content = view.findViewById(R.id.cv_content)
            img_logo = view.findViewById(R.id.img_logo)
            tv_name = view.findViewById(R.id.tv_name)
            tv_price = view.findViewById(R.id.tv_price)
            tv_weight = view.findViewById(R.id.tv_weight)
            tv_sugar = view.findViewById(R.id.tv_sugar)
            tv_milk = view.findViewById(R.id.tv_milk)
            tv_delete = view.findViewById(R.id.tv_delete)
            tv_edit = view.findViewById(R.id.tv_edit)
        }

        fun bindItem(item: ModelBasketItem)
        {
            item.product?.image?.url_m?.let(
                {
                    GlideManager.loadImageSimpleCircle(it, img_logo)
                    img_logo.visibility = View.VISIBLE
                }) ?: run(
                {
                    img_logo.visibility = View.GONE
                })

            tv_name.text = item.product?.name
            tv_price.text = "${item.getSum().formatAsMoney()} р."
            tv_weight.text = item.weight?.value
            tv_sugar.text = item.sugar?.toString()
            tv_milk.text = item.milk?.value
        }

        fun toggleSwipeLa()
        {
            if(tv_edit.isVisibleOnScreen())
            {
                la_swipe.simulateSwipeRight()
            }
            else
            {
                la_swipe.simulateSwipeLeft()
            }
        }
    }
}