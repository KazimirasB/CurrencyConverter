package lt.akb.currency.web

import io.reactivex.Single
import retrofit2.http.*

interface WebApi {

    @GET("/latest?base=EUR")
    suspend fun getRatesUpdate(): RatesResult

    @GET("/latest?base=EUR")
    fun observeRates(): Single<RatesResult>

}