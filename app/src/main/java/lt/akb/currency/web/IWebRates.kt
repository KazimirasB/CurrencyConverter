package lt.akb.currency.web

import androidx.lifecycle.LiveData
import io.reactivex.Single
import lt.akb.currency.main.bones.RateResource

interface IWebRates {
    //load currencies from remote server
    fun observeRates(): Single<RatesResult>
    //load currencies rates updates from remote server
    suspend fun updateRates(): RatesResult
    //
    suspend fun updateRateResource(result: (RatesResult) -> Unit): RateResource
}