package com.justordercompany.client.base.adapters

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
import com.justordercompany.client.base.diff.DiffProducts
import com.justordercompany.client.databinding.ItemProductBinding
import com.justordercompany.client.logic.models.ModelProduct
import com.justordercompany.client.logic.utils.images.GlideManager

class AdapterRvProducts : RecyclerView.Adapter<AdapterRvProducts.CardCafe>()
{
    private var products: ArrayList<ModelProduct> = arrayListOf()
    var action_card_clicked: ((ModelProduct) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCafe
    {
        val inflater = LayoutInflater.from(parent.context)
        val bnd_item_product: ItemProductBinding = DataBindingUtil.inflate(inflater, R.layout.item_product, parent, false)
        return CardCafe(bnd_item_product.root)
    }

    override fun getItemCount(): Int
    {
        return products.size
    }

    override fun onBindViewHolder(holder: CardCafe, position: Int)
    {
        val product = products.get(position)
        holder.bindProduct(product)
        holder.itemView.setOnClickListener(
            {
                action_card_clicked?.invoke(product)
            })
    }

    fun setItems(rec_info: FeedDisplayInfo<ModelProduct>)
    {
        if (rec_info.load_behavior == LoadBehavior.FULL_RELOAD)
        {
            this.products = ArrayList(rec_info.items)
            notifyDataSetChanged()
            return
        }

        val diff_callback = DiffProducts(products, rec_info.items)
        val diff_result = DiffUtil.calculateDiff(diff_callback)
        diff_result.dispatchUpdatesTo(this)
        this.products = ArrayList(rec_info.items)
    }

    class CardCafe(view: View) : RecyclerView.ViewHolder(view)
    {
        val img_logo: ImageView
        val tv_name: TextView
        val tv_description: TextView
        val tv_price: TextView

        init
        {
            img_logo = view.findViewById(R.id.img_logo)
            tv_name = view.findViewById(R.id.tv_name)
            tv_description = view.findViewById(R.id.tv_description)
            tv_price = view.findViewById(R.id.tv_price)
        }

        fun bindProduct(product: ModelProduct)
        {
            product.image?.url_m?.let(
                {
                    GlideManager.loadImageSimpleCircle(it, img_logo)
                })

            tv_name.text = product.name
            tv_description.text = product.description
            product.getPriceText()?.let(
                {
                    tv_price.text = it
                })
        }
    }
}
