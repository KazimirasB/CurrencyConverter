package lt.akb.currency.web

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import lt.akb.currency.main.RatesSettings
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiClient @Inject constructor(retrofit: Retrofit) : IWebRates {

    private val webApiRates: WebApi by lazy {
        retrofit.create(WebApi::class.java)
    }

    override fun observeRates(): Single<RatesResult> {
        return webApiRates.observeRates()
    }

    //load currencies rates updates from remote server
    override suspend fun updateRates(): RatesResult {
        return webApiRates.getRatesUpdate()
    }
}


