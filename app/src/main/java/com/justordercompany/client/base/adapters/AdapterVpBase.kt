package com.justordercompany.client.base.adapters

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.justordercompany.client.R
import com.justordercompany.client.extensions.getStringMy

class AdapterVpBase : PagerAdapter()
{
    private var views: ArrayList<View> = arrayListOf()

    override fun instantiateItem(container: ViewGroup, position: Int): Any
    {
        val view = views.get(position).getRootView()
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any)
    {
        container.removeView(obj as View)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean
    {
        return view == obj
    }

    override fun getCount(): Int
    {
        return views.size
    }

    fun setViews(views: ArrayList<View>)
    {
        this.views = views
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence?
    {
        if (position == 0)
        {
            return getStringMy(R.string.by_distance)
        }
        else if (position == 1)
        {
            return getStringMy(R.string.by_rating)
        }

        return null
    }
}
