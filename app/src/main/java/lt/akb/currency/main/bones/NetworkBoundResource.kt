package lt.akb.currency.main.bones

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
abstract class NetworkBoundResource<ResourceType> {

    //@MainThread
    protected abstract suspend fun webRequest(): ResourceType

    // Called to save the result of the API response into the database
    // @WorkerThread
    protected abstract suspend fun saveCallResult(webResult: ResourceType): ResourceType

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    // @MainThread
    protected abstract fun shouldFetch(): Boolean

    protected abstract fun showProgress(): ResourceType

    protected abstract suspend fun periodicJob(): ResourceType

    protected abstract fun runPeriodic(): Boolean

    // Called to get the cached data from the database.
    //  @MainThread
    protected abstract suspend fun loadFromDb(): ResourceType

    fun asLiveData(): LiveData<ResourceType> = liveData(Dispatchers.IO) {

        showProgress()?.let { emit(showProgress()) }

        if (shouldFetch())
            emit(saveCallResult(webRequest()))
        else
            emit(loadFromDb())

        while (runPeriodic()) {
            delay(1000)
            emit(periodicJob())
        }


    }
}
