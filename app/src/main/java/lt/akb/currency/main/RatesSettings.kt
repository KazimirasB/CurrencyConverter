package lt.akb.currency.main

import android.content.Context
import android.content.SharedPreferences

const val SETTINGS_TAG = "SETTINGS_TAG"
const val RATES_URL = "RATES_URL"
const val IMAGES_URL = "IMAGES_URL"

class RatesSettings {
    companion object {
        var ratesUrl : String = ""
        var imagesUrl: String = ""
        private lateinit var pref : SharedPreferences

        fun init(context: Context) {
             pref = context.getSharedPreferences(SETTINGS_TAG, Context.MODE_PRIVATE)
             ratesUrl = pref.getString(RATES_URL, "https://revolut.duckdns.org")!!
             imagesUrl = pref.getString(IMAGES_URL, "https://www.countryflags.io")!!
        }

        fun updateRatesUrl(url:String){
            ratesUrl=url
            pref.edit().putString(RATES_URL, url).apply()
        }

        fun updateImagesUrl(url:String){
            imagesUrl=url
            pref.edit().putString(IMAGES_URL, url).apply()
        }
    }
}