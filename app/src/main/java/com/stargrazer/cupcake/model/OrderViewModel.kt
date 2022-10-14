package com.stargrazer.cupcake.model
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.Transformations
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel : ViewModel() {
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity
    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor
    fun setFlavor(desiredFlavor: String) { _flavor.value = desiredFlavor }

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date
    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    val dateOptions = getPickupOptions()

    init{
        resetOrder()
    }

    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE,1)
        }
        return options
    }

    private fun updatePrice() {
        var tempPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        if(dateOptions[0]==_date.value) {
            tempPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = tempPrice
    }

}