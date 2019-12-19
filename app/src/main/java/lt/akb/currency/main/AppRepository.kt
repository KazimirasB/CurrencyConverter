package lt.akb.currency.main

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import lt.akb.currency.R
import lt.akb.currency.database.AppDatabase
import lt.akb.currency.database.Rate
import lt.akb.currency.web.ApiClient
import java.math.BigDecimal

const val RATES_URL = "RATES_URL"
const val IMAGES_URL = "IMAGES_URL"

@BindingAdapter("setWebImage")
fun setWebImage(view: ImageView, countryCode: String?) {
    countryCode?.let {
        val pref = view.context.getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)
        val imagesUrl = pref.getString(IMAGES_URL, "https://www.countryflags.io")
        Picasso.get().load("$imagesUrl/${countryCode.toLowerCase()}/flat/64.png")
            .error(R.drawable.ic_error_icon)
            .into(object : Target {

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    view.setImageDrawable(errorDrawable)
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

                    bitmap?.let {
                        val squareBitmap =
                            Bitmap.createBitmap(bitmap, 12, 12, 40, 40)
                        val imageDrawable: RoundedBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(view.resources, squareBitmap)
                        imageDrawable.isCircular = true
                        view.setImageDrawable(imageDrawable)
                    }
                }
            })
    }
}


class AppRepository(application: Application) {

    private val apiClient = ApiClient(this)
    val repoScope = CoroutineScope(Dispatchers.Main + Job())
    private val rateDao = AppDatabase.getInstance(application, repoScope).getRatesDao()
    val pref = application.applicationContext.getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)

    fun refreshRates(items: List<Rate>) {
        repoScope.launch { rateDao.insertAll(items) }
    }

    fun getRatesLive(): LiveData<List<Rate>> {
        return liveData(Dispatchers.IO) { emitSource(rateDao.getAllLive()) }
    }

    fun getRatesUpdate() = liveData(Dispatchers.IO) {
        emit(apiClient.updateRates())
    }

    fun getRatesUpdate(currencyRates: List<Rate>) = liveData(Dispatchers.IO) {

        val response = apiClient.updateRates()

        response?.let {
            val ratesMap = response.rates
            for (i in currencyRates.indices) {
                val currencyRate = currencyRates[i]
                ratesMap[currencyRate.currency]?.let {
                    currencyRate.currencyRate = BigDecimal(ratesMap[currencyRate.currency]!!)
                }
            }
            emit(ratesMap)
        }
    }

    fun getBaseUrl() =  pref.getString(RATES_URL, "https://revolut.duckdns.org")

    fun getRates(currencyMap: HashMap<String, String>) {
        apiClient.getRates(currencyMap)
    }


}