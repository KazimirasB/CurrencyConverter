package lt.akb.currency.web

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import lt.akb.currency.main.RatesSettings
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private val webApiRates: WebApi by lazy {
        Retrofit.Builder()
            .baseUrl(RatesSettings.ratesUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WebApi::class.java)
    }

    //load currencies from remote server
    fun observeRates(): Single<RatesResult> {
        return webApiRates.observeRates()
    }

    //load currencies rates updates from remote server
    suspend fun updateRates(): RatesResult {
        return webApiRates.getRatesUpdate()
    }
}


