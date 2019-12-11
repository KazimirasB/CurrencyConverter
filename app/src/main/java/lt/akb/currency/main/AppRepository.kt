package lt.akb.currency.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import lt.akb.currency.database.Rate
import lt.akb.currency.database.RateDao
import lt.akb.currency.web.ApiClient
import java.math.BigDecimal

class AppRepository(private val dao: RateDao) {

    private val apiClient: ApiClient = ApiClient(this)

    val repoScope = CoroutineScope(Dispatchers.Main + Job())

    fun refreshRates(items: List<Rate>) {
        repoScope.launch { dao.insertAll(items) }
    }

    fun updateValue(currency: String, value: BigDecimal) {
        repoScope.launch { dao.updateValue(currency, value) }
    }

    fun updateRates (items: List<Rate>) {
        repoScope.launch {  dao.updateAll(items) }
    }

    fun getRatesLive(): LiveData<List<Rate>> {
        return liveData { emitSource(dao.getAllLive()) }
    }

    fun getBaseUrl(): String {
        return "https://revolut.duckdns.org"
    }

    fun getRates(currencyMap: HashMap<String, String>) {
        apiClient.getRates(currencyMap)
    }

    fun refreshRates() {
        apiClient.refreshRates()
    }

}