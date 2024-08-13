package com.example.shoppinglist.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.AddShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.ShopItem

class ShopItemViewModel : ViewModel() {
    private val shopListRepository = ShopListRepositoryImpl

    private val addShopItemUseCase = AddShopItemUseCase(shopListRepository)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepository)
    private val getShopItemUseCase = GetShopItemUseCase(shopListRepository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(shopItemId: Int) {
        val shopItem = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = shopItem
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseString(inputName)
        val count = parseCount(inputCount)
        val validateValid = validateInput(name, count)
        if (validateValid) {
            Log.d("ShopViewModelLog","$name  $count")
                val shopItem = ShopItem(name = name,count = count,enabled = true)
                addShopItemUseCase.addShopItem(shopItem)
                finishWork()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseString(inputName)
        val count = parseCount(inputCount)
        val validateValid = validateInput(name, count)
        if (validateValid) {
            Log.d("ShopViewModelLog","$name  $count")
            _shopItem.value?.let {
                val shopItem = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(shopItem)
                finishWork()
            }

        }
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }


    private fun parseString(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return inputCount?.trim()?.toIntOrNull() ?: 0
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            result = false
            _errorInputName.value = true
        }
        if (count <= 0) {
            result = false
            _errorInputCount.value = true
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

}
