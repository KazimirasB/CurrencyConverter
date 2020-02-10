package lt.akb.currency.main.bones

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lt.akb.currency.web.RatesResult

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
abstract class NetworkBoundResourceRxJava<ResultType, RequestType> {


//      private val asObservable: Observable<Resource<ResultType>>

//      init {
//         val source: Observable<Resource<ResultType>>
//         if (shouldFetch()) {
//
//            source = createCall()
//               .subscribeOn(Schedulers.io())
//               .doOnNext {
//                  saveCallResult(processResponse(it)!!) }
//
//               .flatMap {
//                  loadFromDb().toObservable()
//                     .map { Resource.success(it) } }
//
//               .doOnError { onFetchFailed() }
//
//               .onErrorResumeNext { t : Throwable ->
//                  loadFromDb().toObservable().map {
//                     Resource.error(t.message!!, it) }
//               }
//
//               .observeOn(AndroidSchedulers.mainThread())
//
//         } else {
//            source = loadFromDb()
//               .toObservable()
//               .map { Resource.success(it) }
//         }
//
//         asObservable = Observable.concat(
//            loadFromDb()
//               .toObservable()
//               .map { Resource.loading(it) }
//               .take(1),
//            source
//         )
//      }






      // Called to save the result of the API response into the database
   @WorkerThread
   protected abstract fun saveCallResult(item: RequestType)

   // Called with the data in the database to decide whether to fetch
   // potentially updated data from the network.
   @MainThread
   protected abstract fun shouldFetch(): Boolean

   // Called to get the cached data from the database.
   @MainThread
   protected abstract fun loadFromDb(): LiveData<ResultType>

   // Called to create the API call.
//   @MainThread
//   protected abstract fun createCall(): LiveData<RateResource<RequestType>>

   // Called when the fetch fails. The child class may want to reset components
   // like rate limiter.
   protected open fun onFetchFailed() {}

   // Returns a LiveData object that represents the resource that's implemented
   // in the base class.
   fun asLiveData(): LiveData<ResultType> = TODO()
}
