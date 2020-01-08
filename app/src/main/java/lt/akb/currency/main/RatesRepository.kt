package lt.akb.currency.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import lt.akb.currency.R
import lt.akb.currency.database.AppDatabase
import lt.akb.currency.database.Rate
import lt.akb.currency.web.ApiClient
import lt.akb.currency.web.RatesResult
import java.math.BigDecimal
import java.util.*

class RatesRepository(application: Application) {

    val apiClient = ApiClient()
    val repoScope = CoroutineScope(Dispatchers.Main + Job())
    private val rateDao = AppDatabase.getInstance(application, repoScope).getRateDao()
    private var currencyMap: HashMap<String, String> //default map of currencies and country codes
    lateinit var disposableRate: Disposable

    init {
        RatesSettings.init(application.applicationContext)
        currencyMap = Gson().fromJson(application.getString(R.string.currencies_json),
            object : TypeToken<HashMap<String, String>>() {}.type
        )
    }

    // Add list of currencies into database
    private fun addRates(items: List<Rate>) {
        repoScope.launch { rateDao.insertAll(items) }
    }

    //load currencies from database
    fun getRatesLive(): LiveData<List<Rate>> {
        return liveData(Dispatchers.IO) { emitSource(rateDao.getAllLive()) }
    }

    //updates rates of currencies from remote server
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

    fun observeRates() {
        disposableRate =
            apiClient.observeRates().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
//                    progressBar.visibility = View.VISIBLE
//                    reloadImageButton.visibility = View.GONE
                }
                .doOnSuccess {
                    //progressBar.visibility = View.GONE
                    }
                .doOnError {
//                    progressBar.visibility = View.GONE
//                    reloadImageButton.visibility = View.VISIBLE
                }
                .subscribe(this::handleResponse, this::handelError)

    }
    private fun handelError(t: Throwable?) {
   //     Toast.makeText(context, R.string.error_message, Toast.LENGTH_LONG).show()
        t?.let { throw it }
    }

    //Save currencies from web server into database, update display name and country code
    private fun handleResponse(result: RatesResult) {
        val rates = ArrayList<Rate>()
        for (key in result.rates.keys) {
            val currency = Currency.getInstance(key)
            val countryCode = currencyMap[key]
            val rate =
                Rate(key, countryCode, currency.displayName, result.rates[key]!!.toBigDecimal(), 0)
            rates.add(rate)
        }
        if (rates.isNotEmpty()) addRates(rates)
    }
}
