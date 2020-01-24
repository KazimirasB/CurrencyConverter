package lt.akb.currency.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.converter_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lt.akb.currency.database.Rate
import lt.akb.currency.main.RatesRepository
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

//convert BigDecimal object to format decimal string
fun BigDecimal.toDecimalString() = "${setScale(2, BigDecimal.ROUND_CEILING).stripTrailingZeros()}"

//Currency amount calculations by entered amount and base rate
fun Rate.calculateAmount(rateBase: Rate, amount: BigDecimal) : BigDecimal =  currencyRate.multiply(amount)
        .divide(rateBase.currencyRate, 2, BigDecimal.ROUND_CEILING)

class RatesViewModel @Inject constructor(private  val appRepository: RatesRepository): ViewModel() {

    lateinit var rates: List<Rate>
    lateinit var rateBase: Rate
    private var amount = BigDecimal.ONE
    val ratesLive: LiveData<List<Rate>> = appRepository.getRatesLive()

    val ratesLiveSource:  LiveData<RateActionResource> = appRepository.getRatesSourceLive()

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


    ///Actions part
//    val actions = MutableLiveData<RatesAction>()
//    fun loadAction() {
//        appRepository.loadRates().subscribe { action -> actions.postValue(action.action) }
//    }
}
