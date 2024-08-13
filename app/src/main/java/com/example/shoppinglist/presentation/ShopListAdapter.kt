package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

class ShopListAdapter :
    androidx.recyclerview.widget.ListAdapter<ShopItem, ShopListAdapter.ShopItemViewHolder>(
        ShopItemDiffCallback()
    ) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            TYPE_ENABLED -> R.layout.item_shop_enabled
            TYPE_DISABLE -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown View type $viewType")
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.updateViewHolder(name = shopItem.name, count = shopItem.count)
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        holder.updateViewHolder(name = shopItem.name, count = shopItem.count)
    }

    override fun getItemViewType(position: Int): Int {
        val shopItemEnabled = getItem(position).enabled
        return when (shopItemEnabled) {
            true -> TYPE_ENABLED
            false -> TYPE_DISABLE
        }
    }

    class ShopItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvCount: TextView = view.findViewById(R.id.tv_count)

        fun updateViewHolder(name: String, count: Int) {
            tvName.text = name
            tvCount.text = count.toString()
        }
    }

    companion object {
        const val TYPE_ENABLED = 1
        const val TYPE_DISABLE = 0
        const val MAX_POOL_SIZE = 5
    }
}
