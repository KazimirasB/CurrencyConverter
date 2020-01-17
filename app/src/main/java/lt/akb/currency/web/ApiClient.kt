package lt.akb.currency.web

import io.reactivex.Single
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiClient @Inject constructor(retrofit: Retrofit) : IWebRates {

    private val webApiRates: WebApi by lazy {
        retrofit.create(WebApi::class.java)
    }
    //load rates list from server
    override fun observeRates(): Single<RatesResult> {
        return webApiRates.observeRates()
    }

    //load currencies rates updates from remote server
    override suspend fun updateRates(): RatesResult {
        return webApiRates.getRatesUpdate()
    }
}


