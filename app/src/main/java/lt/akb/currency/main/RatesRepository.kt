package lt.akb.currency.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lt.akb.currency.converter.RateActionResource
import lt.akb.currency.converter.RatesAction
import lt.akb.currency.database.Rate
import lt.akb.currency.database.RateDao
import lt.akb.currency.web.IWebRates
import lt.akb.currency.web.RatesResult
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class RatesRepository @Inject constructor(
    private val rateDao: RateDao,
    private val iWebRates: IWebRates,
    val repoScope: CoroutineScope,
    private val settings: AppSettings
) {

    lateinit var disposableRate: Disposable

    //load currencies from database
    fun getRatesLive(): LiveData<List<Rate>> {
        return liveData(Dispatchers.IO) {
            emitSource(rateDao.getAllLive())
        }
    }

    //load currencies from database
    fun getRatesSourceLive(): LiveData<RateActionResource> {
        return liveData(Dispatchers.IO) {
          emitSource(Transformations.map(rateDao.getAllLive()) {
                RateActionResource(RatesAction.LOAD, it)
            }
            )
        }
    }

    //updates rates of currencies from remote server
    fun getRatesUpdate(currencyRates: List<Rate>) = liveData(Dispatchers.IO) {
        val response = iWebRates.updateRates()
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
            iWebRates.observeRates().subscribeOn(Schedulers.io())
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
            val flagUrl = settings.getImageUrl(key)
            val rate =
                Rate(key, flagUrl, currency.displayName, result.rates[key]!!.toBigDecimal(), 0)
            rates.add(rate)
        }
        if (rates.isNotEmpty()) addRates(rates)
    }


    // Add list of currencies into database
    private fun addRates(items: List<Rate>) {
        repoScope.launch { rateDao.insertAll(items) }
    }

    //actionspart
//
//    fun loadRates(): Observable<RateActionResource> {
//
//       return Observable.just(RateActionResource(RatesAction.LOAD))
//    }
}
