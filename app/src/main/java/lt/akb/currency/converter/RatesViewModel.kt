package lt.akb.currency.converter

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.cancel
import lt.akb.currency.custom.CustomViewModel
import lt.akb.currency.database.Rate
import lt.akb.currency.main.RatesRepository
import java.math.BigDecimal

//convert BigDecimal object to format decimal string
fun BigDecimal.toDecimalString() = "${setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros()}"

//Currency amount calculations by entered amount and base rate
fun Rate.calculateAmount(rateBase: Rate, amount: BigDecimal) : BigDecimal =  currencyRate.multiply(amount)
        .divide(rateBase.currencyRate, 2, BigDecimal.ROUND_CEILING)

class RatesViewModel(
    application: Application
) : CustomViewModel(application) {

    lateinit var rateBase: Rate
    private var amount = BigDecimal.ONE
    var ratesLive: LiveData<List<Rate>>
    private val appRepository: RatesRepository = RatesRepository(application)

    init {
        ratesLive = appRepository.getRatesLive()
      }

    //Update currencies rates from web server
    fun updateRates(currencyRates: List<Rate>) = appRepository.getRatesUpdate(currencyRates)

    override fun onCleared() {
        super.onCleared()
        appRepository.repoScope.cancel()
        appRepository.disposableRate.dispose()
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

    fun observeRates() {
        appRepository.observeRates()
    }

    //proceedFragmentAction(new FragmentBindAction());
}
