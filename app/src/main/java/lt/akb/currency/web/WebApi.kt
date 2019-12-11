package lt.akb.currency.web

import retrofit2.Call
import retrofit2.http.*

interface WebApi {

    @GET("/latest?base=EUR")
    fun getRates(): Call<RatesResult>


}