package lt.akb.currency.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import lt.akb.currency.database.AppDatabase
import lt.akb.currency.database.Rate
import lt.akb.currency.web.ApiClient
import java.math.BigDecimal

class RatesRepository(application: Application) {

    val apiClient = ApiClient(this)
    val repoScope = CoroutineScope(Dispatchers.Main + Job())
    private val rateDao = AppDatabase.getInstance(application, repoScope).getRateDao()

    init {
        RatesSettings.init(application.applicationContext)
    }

    fun addRates(items: List<Rate>) {
        repoScope.launch { rateDao.insertAll(items) }
    }

    fun getRatesLive(): LiveData<List<Rate>> {
        return liveData(Dispatchers.IO) { emitSource(rateDao.getAllLive()) }
    }

    fun getRatesUpdate(currencyRates: List<Rate>) = liveData(Dispatchers.IO) {

        val response = apiClient.updateRates()
        val ratesMap = response.rates
        for (i in currencyRates.indices) {
            val currencyRate = currencyRates[i]
            ratesMap[currencyRate.currency]?.let {
                currencyRate.currencyRate = BigDecimal(ratesMap[currencyRate.currency]!!)
            }
            emit(ratesMap)
        }
    }

    fun getBaseUrl() = RatesSettings.ratesUrl

}
