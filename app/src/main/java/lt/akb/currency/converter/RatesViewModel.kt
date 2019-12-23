package lt.akb.currency.converter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.cancel
import lt.akb.currency.R
import lt.akb.currency.database.Rate
import lt.akb.currency.main.RatesRepository
import lt.akb.currency.web.RatesResult
import java.math.BigDecimal
import java.util.*
//convert BigDecimal object to format decimal string
fun BigDecimal.toDecimalString() = "${setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros()}"

//Currency amount calculations by entered amount and base rate
fun Rate.calculateAmount(rateBase: Rate, amount: BigDecimal) : BigDecimal =  currencyRate.multiply(amount)
        .divide(rateBase.currencyRate, 2, BigDecimal.ROUND_CEILING)

class RatesViewModel(
    application: Application
) : AndroidViewModel(application) {

    lateinit var rateBase: Rate
    private var amount = BigDecimal.ONE
    private var currencyMap: HashMap<String, String> //default map of currencies and country codes
    var ratesLive: LiveData<List<Rate>>
    val appRepository: RatesRepository = RatesRepository(application)

    init {
        ratesLive = appRepository.getRatesLive()
        currencyMap = Gson().fromJson(
            application.getString(R.string.currencies_json),
            object : TypeToken<HashMap<String, String>>() {}.type
        )
    }

    //Save currencies from web server into database, update display name and country code
    fun handleResponse(result: RatesResult) {
        val rates = ArrayList<Rate>()
        for (key in result.rates.keys) {
            val currency = Currency.getInstance(key)
            val countryCode = currencyMap[key]
            val rate =
                Rate(key, countryCode, currency.displayName, result.rates[key]!!.toBigDecimal(), 0)
            rates.add(rate)
        }
        if (rates.isNotEmpty()) appRepository.addRates(rates)
    }

    //Update currencies rates from web server
    fun updateRates(currencyRates: List<Rate>) = appRepository.getRatesUpdate(currencyRates)

    override fun onCleared() {
        super.onCleared()
        appRepository.repoScope.cancel()
    }

    //calculate amount on currency item binding
    fun calculateValue(item: Rate): String {
        item.value = item.calculateAmount(rateBase, amount)
        return item.value.toDecimalString()
    }

    //set base rate on currency item click
    fun setBaseRate(rate: Rate) {
        this.rateBase = rate
        amount = rate.value
    }

    //set base amount on base currency item amount edit
    fun setBaseAmount(amount: BigDecimal) {
        this.amount = amount
        rateBase.value = amount
    }
}
