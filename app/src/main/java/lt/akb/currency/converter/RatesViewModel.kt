package lt.akb.currency.converter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.cancel
import lt.akb.currency.R
import lt.akb.currency.database.Rate
import lt.akb.currency.main.AppRepository
import java.math.BigDecimal

fun BigDecimal.toDecimalString() = "${setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros()}"

class RatesViewModel(
    application: Application
) : AndroidViewModel(application) {

    var rate: Rate = Rate("EUR", "ES", "European Union euro", BigDecimal.ONE, 1)
    var amount = BigDecimal.ONE
    private var currencyMap: HashMap<String, String>
    var ratesLive: LiveData<List<Rate>>
    private val appRepository: AppRepository = AppRepository(application)

    init {
        ratesLive = appRepository.getRatesLive()
        currencyMap = Gson().fromJson(
            application.getString(R.string.currencies_json),
            object : TypeToken<HashMap<String, String>>() {}.type
        )
    }

    fun getRates() {
        appRepository.getRates(currencyMap)
    }

    fun updateRates() = appRepository.getRatesUpdate()

    fun updateRates(currencyRates:List<Rate>) = appRepository.getRatesUpdate(currencyRates)

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
        amount = rate.value
        this.rate = rate
    }
}
