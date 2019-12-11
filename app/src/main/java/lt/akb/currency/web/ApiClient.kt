package lt.akb.currency.web

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import lt.akb.currency.database.Rate
import lt.akb.currency.main.AppRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ApiClient(private val appRepository: AppRepository) {

    private fun getWebApi(): WebApi {

        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(appRepository.getBaseUrl())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(WebApi::class.java)

    }

    fun getRates(currencyMap: HashMap<String, String>) {

        val call = getWebApi().getRates()

        call.enqueue(
            object : Callback<RatesResult> {
                override fun onResponse(call: Call<RatesResult>, response: Response<RatesResult>) {

                    if (response.isSuccessful) {
                        val body = response.body()!!
                        val rates = emptyList<Rate>().toMutableList()

                        for (key in body.rates.keys) {
                            val currency = Currency.getInstance(key)
                            val countryCode = currencyMap[key]

                            rates.add(
                                Rate(
                                    key,
                                    countryCode,
                                    currency.displayName,
                                    body.rates[key]!!.toBigDecimal(),
                                    false
                                )
                            )
                        }
                        appRepository.refreshRates(rates)
                    } else error("${response.code()} ${response.message()}")
                }

                override fun onFailure(call: Call<RatesResult>?, t: Throwable?) {
                    //    error("${t?.message}")
                }
            }
        )
    }

    fun refreshRates() {

        val call = getWebApi().getRates()

        call.enqueue(
            object : Callback<RatesResult> {
                override fun onResponse(call: Call<RatesResult>, response: Response<RatesResult>) {

                    if (response.isSuccessful) {
                        val body = response.body()!!
                        for (currency in body.rates.keys) {
                            appRepository.updateValue(
                                currency,
                                body.rates[currency]!!.toBigDecimal()
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<RatesResult>?, t: Throwable?) {

                }
            }
        )
    }


}

