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
import java.util.*

class RatesRepository(application: Application) {

    private val apiClient = ApiClient(this)
    val repoScope = CoroutineScope(Dispatchers.Main + Job())
    private val rateDao = AppDatabase.getInstance(application, repoScope).getRatesDao()

    init {
        RatesSettings.init(application.applicationContext)
    }

    fun refreshRates(items: List<Rate>) {
        repoScope.launch { rateDao.insertAll(items) }
    }

    fun getRatesLive(): LiveData<List<Rate>> {
        return liveData(Dispatchers.IO) { emitSource(rateDao.getAllLive()) }
    }

    fun getRatesUpdate(currencyRates: List<Rate>) = liveData(Dispatchers.IO) {

        val response = apiClient.updateRates()
        val ratesMap = response.rates
        for (i in currencyRates.indices) {
            val currencyRate = currencyRates[i]
            ratesMap[currencyRate.currency]?.let {
                currencyRate.currencyRate = BigDecimal(ratesMap[currencyRate.currency]!!)
            }
            emit(ratesMap)
        }
    }

    fun getBaseUrl() = RatesSettings.ratesUrl

    fun getRates(currencyMap: HashMap<String, String>) {
        apiClient.getRates(currencyMap)
    }
}

@BindingAdapter("setWebImage")
fun setWebImage(view: ImageView, countryCode: String?) {
    countryCode?.let {

        Picasso.get()
            .load("${RatesSettings.imagesUrl}/${countryCode.toLowerCase(Locale.getDefault())}/flat/64.png")
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
