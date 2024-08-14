package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemFragment : Fragment() {
    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    private lateinit var  onEditingFinishingListener:OnEditingFinishingListener
    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEditingFinishingListener)   {
            onEditingFinishingListener = context
        }else{
            throw RuntimeException("Activity must implements interface OnEditingFinishingListener")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        initViews(view)
        addTextChangeListener()
        launchRightMode()
        observeViewModel()
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        buttonSave = view.findViewById(R.id.button)
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun observeViewModel() {
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) getString(R.string.error_input_count)
            else null
            tilCount.error = message
        }
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) getString(R.string.error_input_name)
            else null
            tilName.error = message
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun addTextChangeListener() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }


    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)

        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun parseParams() {
        val args  = requireArguments()
        if (!args.containsKey(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param Screen Mode Is Absent")
        }
        val mode = args.getString(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shopItem id Is Absent")
            }
            shopItemId = args.getInt(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }
    interface OnEditingFinishingListener{
        fun onEditingFinish()
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra mode"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_ADD"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_UNKNOWN = ""


        fun newInstanceAddItem(): ShopItemFragment {
            val args = Bundle().apply {
                putString(EXTRA_SCREEN_MODE, MODE_ADD)
            }
            return ShopItemFragment().apply {
                arguments = args
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            val args = Bundle().apply {
                putString(EXTRA_SCREEN_MODE, MODE_EDIT)
                putInt(EXTRA_SHOP_ITEM_ID,shopItemId)
            }
            return ShopItemFragment().apply {
                arguments = args
            }
        }
    }
}