package lt.akb.currency.main

import android.content.Context
import android.content.SharedPreferences
import java.util.*

const val SETTINGS_TAG = "SETTINGS_TAG"
const val RATES_URL = "RATES_URL"
const val IMAGES_URL = "IMAGES_URL"

class RatesSettings {
    companion object {
        lateinit var ratesUrl: String
        private lateinit var imagesUrl: String
        private lateinit var pref: SharedPreferences

        fun init(context: Context) {
            pref = context.getSharedPreferences(SETTINGS_TAG, Context.MODE_PRIVATE)
            ratesUrl = pref.getString(RATES_URL, "https://revolut.duckdns.org")!!
            imagesUrl = pref.getString(IMAGES_URL, "https://www.countryflags.io")!!
        }

        //Format image url to download with Picasso
        fun getImageUrl(countryCode: String): String {
            return "${imagesUrl}/${countryCode.toLowerCase(Locale.getDefault())}/flat/64.png"
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
}
