package lt.akb.currency.main.bones

import lt.akb.currency.database.Rate

sealed class RateResource {
    data class Success(val data: List<Rate>) : RateResource()
    data class Loading(val isProceed: Boolean) : RateResource()
    data class Error(val message: String) : RateResource()
}


