package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var ll :LinearLayout
    private val shopItem = ShopItem(name = "product 2", count = 2, enabled = true, id = 2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(owner = this)[MainViewModel::class.java]
        ll = findViewById(R.id.ll_shop_list)


        viewModel.shopList.observe(this) {
            showList(it)
        }
    }

    private fun showList(list: List<ShopItem>) {
        ll.removeAllViews()
        for (shopItem in list) {
            val idLayout = if (shopItem.enabled) {
                R.layout.item_shop_enabled
            } else {
                R.layout.item_shop_disabled
            }
            val view = LayoutInflater.from(this).inflate(idLayout,ll,false)
            view.findViewById<TextView>(R.id.tv_name).text = shopItem.name
            view.findViewById<TextView>(R.id.tv_count).text = shopItem.count.toString()
            view.setOnLongClickListener(View.OnLongClickListener {
                viewModel.changeEnabledState(shopItem)
                true
            })
            ll.addView(view)
        }
    }
}