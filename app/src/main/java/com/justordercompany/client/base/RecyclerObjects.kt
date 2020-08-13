package com.justordercompany.client.base

import com.justordercompany.client.logic.requests.FeedLoadInfo

enum class LoadBehavior
{
    FULL_RELOAD,
    UPDATE,
    SCROLL_TO_ID,
    NONE;

    open var id_to_scroll: Int? = null

    companion object
    {
        fun getScrollToId(id: Int): LoadBehavior
        {
            val behavior = SCROLL_TO_ID
            behavior.id_to_scroll = id
            return behavior
        }
    }
}

class FeedDisplayInfo<T>
    (
        val items: List<T> = arrayListOf(),
        var load_behavior: LoadBehavior = LoadBehavior.NONE
)

fun <T> FeedDisplayInfo<T>?.getNextLoadInfo(): FeedLoadInfo<T>
{
    if (this == null)
    {
        return FeedLoadInfo(0, Constants.COUNT_ADD_ON_LOAD, {})
    }

    return FeedLoadInfo(this.items.size, Constants.COUNT_ADD_ON_LOAD, {})
}

fun <T> FeedDisplayInfo<T>?.addElements(items:List<T>): FeedDisplayInfo<T>
{
    if (this == null)
    {
        return FeedDisplayInfo(items, LoadBehavior.FULL_RELOAD)
    }

    val new_items : ArrayList<T> = arrayListOf()
    new_items.addAll(this.items)
    new_items.addAll(items)
    return FeedDisplayInfo(new_items,LoadBehavior.UPDATE)
}
