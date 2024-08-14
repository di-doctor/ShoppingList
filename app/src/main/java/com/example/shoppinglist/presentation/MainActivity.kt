package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ShopListAdapter
    private lateinit var viewModel: MainViewModel

    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemContainer = findViewById(R.id.shop_item_container)

        setupRecyclerView()
        viewModel = ViewModelProvider(owner = this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            adapter.submitList(it)
        }
        val buttonAddItem = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        buttonAddItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                val fragment = ShopItemFragment.newInstanceAddItem()
                launchFragment(fragment)
            }
        }
    }

    private fun isOnePaneMode(): Boolean {
        return shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        with(supportFragmentManager){
            popBackStack()
            beginTransaction()
                .replace(R.id.shop_item_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        adapter = ShopListAdapter()
        rvShopList.adapter = adapter
        rvShopList.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.TYPE_ENABLED,
            ShopListAdapter.MAX_POOL_SIZE
        )
        rvShopList.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.TYPE_DISABLE,
            ShopListAdapter.MAX_POOL_SIZE
        )
        setupLongClickListener()
        setupClickListener()
        setupSwipeItemForDelete(rvShopList)
    }

    private fun setupSwipeItemForDelete(rvShopList: RecyclerView?) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        adapter.onShopItemClickListener = {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            }else{
                val fragment = ShopItemFragment.newInstanceEditItem(it.id)
                launchFragment(fragment)
            }
        }

    }

    private fun setupLongClickListener() {
        adapter.onShopItemLongClickListener = {
            viewModel.changeEnabledState(it)
            Log.d("ShoppingList", "click work")
        }
    }

}