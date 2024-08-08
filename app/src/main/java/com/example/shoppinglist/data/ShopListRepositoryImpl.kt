package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import kotlin.random.Random

class ShopListRepositoryImpl : ShopListRepository {
    private val shopList = sortedSetOf(object :Comparator<ShopItem>{
        override fun compare(p0: ShopItem, p1: ShopItem): Int {
            return p0.id.compareTo(p1.id)
        }
    })
    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private var autoIncrementId = 0

    init {
        repeat(50) {
            val shopItem = ShopItem(name = "product $it", count = it, Random.nextBoolean())
            addShopItem(shopItem)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { shopItem -> shopItem.id == shopItemId }
            ?: throw Exception("Not found object")
    }

    override fun getShopList(): LiveData<List<ShopItem>> = shopListLD

    private fun updateList() {
        shopListLD.value = shopList.toList()
    }
}