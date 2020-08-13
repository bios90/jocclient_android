package com.justordercompany.client.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.justordercompany.client.R
import com.justordercompany.client.base.FeedDisplayInfo
import com.justordercompany.client.base.LoadBehavior
import com.justordercompany.client.base.diff.DiffProducts
import com.justordercompany.client.databinding.ItemReviewBinding
import com.justordercompany.client.logic.models.ModelProduct
import com.justordercompany.client.logic.models.ModelReview
import com.justordercompany.client.logic.utils.formatToString
import com.justordercompany.client.logic.utils.images.GlideManager

class AdapterRvReviews : RecyclerView.Adapter<CardReview>()
{
    private var reviews: ArrayList<ModelReview> = arrayListOf()
    var action_card_clicked: ((ModelReview) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardReview
    {
        val inflater = LayoutInflater.from(parent.context)
        val bnd_item_review: ItemReviewBinding = DataBindingUtil.inflate(inflater, R.layout.item_review, parent, false)
        return CardReview(bnd_item_review)
    }

    override fun getItemCount(): Int
    {
        return reviews.size
    }

    override fun onBindViewHolder(holder: CardReview, position: Int)
    {
        val review = reviews.get(position)
        holder.bindReview(review)
    }

    fun setItems(items: ArrayList<ModelReview>)
    {
        this.reviews = ArrayList(items)
        notifyDataSetChanged()
        return
    }
}

class CardReview(val bnd_item_review: ItemReviewBinding) : RecyclerView.ViewHolder(bnd_item_review.root)
{
    fun bindReview(review: ModelReview)
    {
        review.user?.image?.url_s?.let(
            {
                GlideManager.loadImageSimpleCircle(it, bnd_item_review.imgLogo)
            })

        review.rating?.let(
            {
                bnd_item_review.ratingBar.rating = it.toFloat()
            })

        bnd_item_review.tvAuthorName.text = review.user?.name
        bnd_item_review.tvDate.text = review.date?.formatToString()
        bnd_item_review.tvText.text = review.text
    }
}