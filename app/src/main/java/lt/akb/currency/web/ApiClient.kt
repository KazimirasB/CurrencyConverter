package lt.akb.currency.web

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import lt.akb.currency.database.Rate
import lt.akb.currency.main.RatesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

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

    fun getRates(currencyMap: HashMap<String, String>) {

        webApiRates.getRates().enqueue(
            object : Callback<RatesResult> {
                override fun onResponse(call: Call<RatesResult>, response: Response<RatesResult>) {

                    if (response.isSuccessful) {
                        val body = response.body()!!
                        val rates = ArrayList<Rate>()

                        for (key in body.rates.keys) {
                            val currency = Currency.getInstance(key)
                            val countryCode = currencyMap[key]

                            rates.add(
                                Rate(
                                    key,
                                    countryCode,
                                    currency.displayName,
                                    body.rates[key]!!.toBigDecimal()
                                    , 0
                                )
                            )
                        }
                        ratesRepository.refreshRates(rates)
                    } else
                        //throw Throwable("${response.code()} ${response.message()}")
                    throw Throwable("Network error")
                }

                override fun onFailure(call: Call<RatesResult>?, t: Throwable?) {
                    t?.let { Throwable("Network error") }
                }
            }
        )
    }

    suspend fun updateRates(): RatesResult {
        return webApiUpdate.getRatesUpdate()
    }
}


