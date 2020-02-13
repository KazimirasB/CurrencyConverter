package lt.akb.currency.main

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lt.akb.currency.R
import lt.akb.currency.database.Rate
import lt.akb.currency.database.RateDao
import lt.akb.currency.main.bones.NetworkBoundResource
import lt.akb.currency.main.bones.RateResource
import lt.akb.currency.web.IWebRates
import lt.akb.currency.web.RatesResult
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

    var stopPeriodic: Boolean = false

    fun boundResourcesLive(): LiveData<RateResource> {
        return object : NetworkBoundResource<RateResource>() {

            override suspend fun saveCallResult(webResult: RateResource): RateResource {
                return when (webResult) {
                    is RateResource.Success -> {
                        saveResponse(webResult.result)
                        loadFromDb()
                    }
                    is RateResource.Error -> webResult
                    is RateResource.ErrorRes -> webResult
                    else -> RateResource.ErrorRes(R.string.error_message)
                }
            }

            override fun shouldFetch(): Boolean = true

            override suspend fun loadFromDb(): RateResource =
                RateResource.Load(rateDao.getAll())

            override suspend fun webRequest(): RateResource = iWebRates.updateRateResource()

            override fun showProgress(): RateResource = RateResource.Progress(true)

            override suspend fun periodicJob(): RateResource {
                return when (val webResult = webRequest()) {
                    is RateResource.Success -> RateResource.Refresh(webResult.result.rates)
                    else -> saveCallResult(webResult)
                }
            }

            override fun runPeriodic(): Boolean = !stopPeriodic
        }.asLiveData()
    }

    //Save currencies from web server into database, update display name and country code
    private fun saveResponse(result: RatesResult) {
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



//    lateinit var disposableRate: Disposable

    //load currencies from database
//    fun getRatesLive(): LiveData<List<Rate>> {
//        return liveData(Dispatchers.IO) {
//            emitSource(rateDao.getAllLive())
//        }
//    }
//
//    //load currencies from database
//    fun getRatesSourceLive(): LiveData<RateActionResource> {
//        return liveData(Dispatchers.IO) {
//            emitSource(Transformations.map(rateDao.getAllLive()) {
//                RateActionResource(RatesAction.LOAD, it)
//            }
//            )
//        }
//    }

    //    suspend fun getSomething(): LiveData<Resource<Something>> = livedata {
//        emit(Resource.Loading())
//        try {
//            val req = apiService.getSomething()
//            if (req.isSuccessful()) emit(Resource.Success(req.body))
//        } catch (e: Exception) {
//            emit(Resource.Error(e.message))
//        }
//    }
//

//    fun getRatesResourceLive(isRefresh: Boolean): LiveData<RateResource> =
//        liveData(Dispatchers.IO) {
//
//            if (isFetch()) {
//
//                if (!isRefresh) emit(RateResource.Progress(true))
//
//                when (val resource = iWebRates.updateRateResource()) {
//                    is RateResource.Success -> {
//                        saveResponse(resource.result)
//                        emitSource(Transformations.map(rateDao.getAllLive()) {
//                            //                            if (isRefresh) RateResource.Refresh(it.)
////                            else
//                            RateResource.Load(it)
//                        })
//                    }
//                    else -> emit(resource)
//                }
//            } else {
//                val rates = rateDao.getAll()
//                emit(RateResource.Load(rates))
//            }
//        }
//

//    private fun isFetch() = true
//
//
//    //updates rates of currencies from remote server
//    fun getRatesUpdate(currencyRates: List<Rate>) = liveData(Dispatchers.IO) {
//        val response = iWebRates.updateRates()
//        val ratesMap = response.rates
//        for (i in currencyRates.indices) {
//            val currencyRate = currencyRates[i]
//            ratesMap[currencyRate.currency]?.let {
//                currencyRate.currencyRate = BigDecimal(ratesMap[currencyRate.currency]!!)
//            }
//            emit(ratesMap)
//        }
//    }


//    fun observeRatesLive(): LiveData<RateActionResource> {
//        return liveData(Dispatchers.IO) {
//
//            saveResponse(iWebRates.updateRates())
//
//            emitSource(Transformations.map(rateDao.getAllLive()) {
//                RateActionResource(RatesAction.LOAD, it)
//            })
//        }
//
//    }

//    fun observeRates() {
//        disposableRate =
//            iWebRates.observeRates().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe {
//                    //                    progressBar.visibility = View.VISIBLE
////                    reloadImageButton.visibility = View.GONE
//                }
//                .doOnSuccess {
//                    //progressBar.visibility = View.GONE
//                }
//                .doOnError {
//                    //                    progressBar.visibility = View.GONE
////                    reloadImageButton.visibility = View.VISIBLE
//                }
//                .subscribe(this::saveResponse, this::handelError)
//
//    }
//
//    private fun handelError(t: Throwable?) {
//        //     Toast.makeText(context, R.string.error_message, Toast.LENGTH_LONG).show()
//        t?.let { throw it }
//    }



    //actions part
//
//    fun loadRates(): Observable<RateActionResource> {
//
//       return Observable.just(RateActionResource(RatesAction.LOAD))
//    }
}
