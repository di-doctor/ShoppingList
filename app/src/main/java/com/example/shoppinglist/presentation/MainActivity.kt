package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private val shopItem = ShopItem(name = "product 2", count = 2, enabled = true, id = 2)
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(owner = this)[MainViewModel::class.java]


        viewModel.shopList.observe( this){

            Log.d("Shop_List", it.toString())
            if (count == 0){
                count++
                val itemOld = it[0]
                val itemNew = itemOld.copy(enabled = !itemOld.enabled)
                viewModel.editShopItem(itemNew)
            }
        }


    }
}