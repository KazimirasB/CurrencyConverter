package lt.akb.currency.web

import io.reactivex.Single
import lt.akb.currency.main.bones.RateResource
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
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

    //load currencies rates updates from remote server
    override suspend fun updateRateResource(handleResponse: (RatesResult) -> Unit): RateResource {
        return try {
            // RateResource.Success(webApiRates.getRatesUpdate())
            val result = webApiRates.getRatesUpdate()
            handleResponse(result)
            RateResource.Success(result)
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> RateResource.Error(throwable.message)
                is HttpException -> {
                    RateResource.Error("${throwable.code()}, $throwable.message")
                }
                else -> {
                    RateResource.Error(throwable.message)
                }
            }
        }

    }
}


