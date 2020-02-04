package lt.akb.currency.main.bones

import lt.akb.currency.database.Rate
import lt.akb.currency.web.RatesResult

sealed class RateResource {
    data class Load(val data: List<Rate>) : RateResource()
    data class Refresh(val data: List<Rate>) : RateResource()
    data class Progress(val isProceed: Boolean) : RateResource()
    data class Error(val message: String?) : RateResource()
    data class Success(val result: RatesResult) : RateResource()
}


