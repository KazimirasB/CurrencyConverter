package lt.akb.currency.main

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import lt.akb.currency.R
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

const val SETTINGS_TAG = "SETTINGS_TAG"
const val RATES_URL = "RATES_URL"
const val IMAGES_URL = "IMAGES_URL"

@Singleton
class RatesSettings @Inject constructor(context: Context, gson: Gson) {

    private val pref = context.getSharedPreferences(SETTINGS_TAG, Context.MODE_PRIVATE)

    var ratesUrl: String? = pref.getString(RATES_URL, "https://revolut.duckdns.org")
    var imagesUrl: String? = pref.getString(IMAGES_URL, "https://www.countryflags.io")
    val currencyMap: HashMap<String, String> =
        gson.fromJson(context.getString(R.string.currencies_json),
            object : TypeToken<HashMap<String, String>>() {}.type
        )

    //Format image url to download with Picasso
    fun getImageUrl(key: String): String? {
      return if(currencyMap.containsKey(key))
         "${imagesUrl}/${currencyMap[key]!!.toLowerCase(Locale.getDefault())}/flat/64.png"
        else null
    }

    // Update web url to get rates
    fun updateRatesUrl(url: String) {
        ratesUrl = url
        pref.edit().putString(RATES_URL, url).apply()
    }

    // Update web url to get images
    fun updateImagesUrl(url: String) {
        imagesUrl = url
        pref.edit().putString(IMAGES_URL, url).apply()
    }

}
