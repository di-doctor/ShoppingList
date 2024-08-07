package com.example.shoppinglist.domain

interface ShopListRepository {
    fun addShopItem(shopItem:ShopItem){
        TODO()
    }
    fun deleteShopItem(shopItem: ShopItem){
        TODO()
    }
    fun editShopItem(shopItem:ShopItem){
        TODO()
    }
    fun getShopItem(shopItemId:Int):ShopItem{
        TODO()
    }
    fun getShopList():List<ShopItem>{
        TODO()
    }

}