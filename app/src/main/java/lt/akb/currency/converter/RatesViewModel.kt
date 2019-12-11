package lt.akb.currency.converter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.cancel
import lt.akb.currency.R
import lt.akb.currency.database.AppDatabase
import lt.akb.currency.database.Rate
import lt.akb.currency.main.AppRepository
import kotlin.concurrent.fixedRateTimer
import java.math.BigDecimal

class RatesViewModel (application: Application) : AndroidViewModel(application) {

    var rate: Rate = Rate("EUR", "ES","" , BigDecimal.valueOf(1), true)
    var amount = BigDecimal(100)
    private var currencyMap: HashMap<String, String>
    var isNew: Boolean = true
    var ratesLive: LiveData<List<Rate>>
    private val appRepository: AppRepository
    var isStop: Boolean = false

    init {
        val rateDao = AppDatabase.getInstance(application).getRatesDao()
        appRepository = AppRepository(rateDao)
        ratesLive = appRepository.getRatesLive()
        currencyMap = Gson().fromJson(application.getString(R.string.currencies_json), object : TypeToken<HashMap<String, String>>() {}.type
        )
    }

    fun getRates() {

        appRepository.getRates(currencyMap)

     //   fixedRateTimer("timer", false, 0L,  1000) {
   //         if (isStop) cancel()
            appRepository.refreshRates()
    //    }

    }

    override fun onCleared() {
        super.onCleared()
        appRepository.repoScope.cancel()
    }

    fun calculateValue(item: Rate): String {
        return "${item.value * amount / rate.value}"
    }

    fun setSelectedRate(rate: Rate) {
        this.rate.isBase = false
        rate.isBase = true
        appRepository.updateRates(arrayListOf(this.rate, rate))
    }
}
