package lt.akb.currency.web

import io.reactivex.Single

interface IWebRates {
    //load currencies from remote server
    fun observeRates(): Single<RatesResult>
    //load currencies rates updates from remote server
    suspend fun updateRates(): RatesResult
}