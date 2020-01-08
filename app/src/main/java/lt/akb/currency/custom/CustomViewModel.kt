package lt.akb.currency.custom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

open class CustomViewModel (
    application: Application
) : AndroidViewModel(application) {
    val liveAction: MutableLiveData<ILiveAction> = MutableLiveData()
    fun proceedLiveAction(fragmentAction: ILiveAction) {
             liveAction.postValue(fragmentAction)
    }
}