package com.justordercompany.client.base.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.justordercompany.client.R
import com.justordercompany.client.base.LoadBehavior
import com.justordercompany.client.base.FeedDisplayInfo
import com.justordercompany.client.base.diff.DiffCafes
import com.justordercompany.client.databinding.ItemCafeBinding
import com.justordercompany.client.extensions.toVisibility
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.utils.LocationManager
import com.justordercompany.client.logic.utils.images.GlideManager
import com.willy.ratingbar.ScaleRatingBar

class AdapterRvCafe : RecyclerView.Adapter<AdapterRvCafe.CardCafe>()
{
    private var cafes: ArrayList<ModelCafe> = arrayListOf()
    var listener: ((ModelCafe) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCafe
    {
        val inflater = LayoutInflater.from(parent.context)
        val bnd_cafe: ItemCafeBinding = DataBindingUtil.inflate(inflater, R.layout.item_cafe, parent, false)
        return CardCafe(bnd_cafe.root)
    }

    override fun getItemCount(): Int
    {
        return cafes.size
    }

    override fun onBindViewHolder(holder: CardCafe, position: Int)
    {
        val cafe = cafes.get(position)
        holder.bindCafe(cafe)

        holder.cv_root.setOnClickListener(
            {
                listener?.invoke(cafe)
            })

        holder.rating_bar.isClickable = false
        holder.rating_bar.isFocusable = false
    }

    fun setItems(rec_info: FeedDisplayInfo<ModelCafe>)
    {
        if (rec_info.load_behavior == LoadBehavior.FULL_RELOAD)
        {
            this.cafes = ArrayList(rec_info.items)
            notifyDataSetChanged()
            return
        }

        val diff_callback = DiffCafes(cafes, rec_info.items)
        val diff_result = DiffUtil.calculateDiff(diff_callback)
        diff_result.dispatchUpdatesTo(this)
        this.cafes = ArrayList(rec_info.items)
    }

    class CardCafe(view: View) : RecyclerView.ViewHolder(view)
    {
        val cv_root: CardView
        val img_logo: ImageView
        val tv_name: TextView
        val tv_adress: TextView
        val rating_bar: ScaleRatingBar
        val tv_time: TextView
        val tv_distance: TextView
        val tv_is_favorite: TextView

        init
        {
            cv_root = view.findViewById(R.id.cv_root)
            img_logo = view.findViewById(R.id.img_logo)
            tv_name = view.findViewById(R.id.tv_name)
            tv_adress = view.findViewById(R.id.tv_adress)
            rating_bar = view.findViewById(R.id.rating_bar)
            tv_time = view.findViewById(R.id.tv_time)
            tv_distance = view.findViewById(R.id.tv_distance)
            tv_is_favorite = view.findViewById(R.id.tv_is_favorite)
        }

        fun bindCafe(cafe: ModelCafe)
        {
            cafe.logo?.url_l?.let(
                {
                    GlideManager.loadImageSimpleCircle(it, img_logo)
                })
            tv_name.text = cafe.name
            tv_adress.text = cafe.address

            tv_is_favorite.visibility = cafe.isFavorite().toVisibility()

            cafe.rating?.let(
                {
                    rating_bar.rating = it
                })

            cafe.distance?.let(
                {
                    tv_distance.text = LocationManager.getDistanceText(it)
                })
            tv_time.text = cafe.working_hours_str
        }
    }
}