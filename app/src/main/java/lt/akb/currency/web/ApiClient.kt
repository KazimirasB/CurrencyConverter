package lt.akb.currency.web

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import lt.akb.currency.main.RatesRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient(private val ratesRepository: RatesRepository) {

    private val webApiUpdate: WebApi by lazy {
        Retrofit.Builder()
            .baseUrl(ratesRepository.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WebApi::class.java)
    }

    private val webApiRates: WebApi by lazy {
        Retrofit.Builder()
            .baseUrl(ratesRepository.getBaseUrl())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WebApi::class.java)
    }

    fun observeRates(): Single<RatesResult> {
        return webApiRates.observeRates()
    }

    suspend fun updateRates(): RatesResult {
        return webApiUpdate.getRatesUpdate()
    }
}


