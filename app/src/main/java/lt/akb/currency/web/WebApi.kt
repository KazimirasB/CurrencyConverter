package lt.akb.currency.web

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.http.*

interface WebApi {

    @GET("/latest?base=EUR")
    fun getRates(): Call<RatesResult>

    @GET("/latest?base=EUR")
    suspend fun getRatesUpdate(): RatesResult

}