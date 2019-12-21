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

fun BigDecimal.toDecimalString() = "${setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros()}"

class RatesViewModel(
    application: Application
) : AndroidViewModel(application) {

    lateinit var rate: Rate
    private var amount = BigDecimal.ONE
    private var currencyMap: HashMap<String, String>
    var ratesLive: LiveData<List<Rate>>
    val appRepository: RatesRepository = RatesRepository(application)

    init {
        ratesLive = appRepository.getRatesLive()
        currencyMap = Gson().fromJson(
            application.getString(R.string.currencies_json),
            object : TypeToken<HashMap<String, String>>() {}.type
        )
    }

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

    fun updateRates(currencyRates: List<Rate>) = appRepository.getRatesUpdate(currencyRates)

    override fun onCleared() {
        super.onCleared()
        appRepository.repoScope.cancel()
    }

    fun calculateValue(item: Rate): String {
        item.value = calculateAmount(item)
        return item.value.toDecimalString()
    }

    private fun calculateAmount(item: Rate): BigDecimal {
        return item.currencyRate.multiply(amount)
            .divide(rate.currencyRate, 2, BigDecimal.ROUND_CEILING)
    }

    fun setBaseRate(rate: Rate) {
        this.rate = rate
        amount = rate.value
    }

    fun setBaseAmount(amount: BigDecimal) {
        this.amount = amount
        rate.value = amount
    }
}
