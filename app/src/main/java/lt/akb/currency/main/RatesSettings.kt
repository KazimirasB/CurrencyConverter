package lt.akb.currency.main

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import lt.akb.currency.R
import java.util.*

const val SETTINGS_TAG = "SETTINGS_TAG"
const val RATES_URL = "RATES_URL"
const val IMAGES_URL = "IMAGES_URL"

class RatesSettings {
    companion object {
        var ratesUrl: String = ""
        private var imagesUrl: String = ""
        private lateinit var pref: SharedPreferences

        fun init(context: Context) {
            pref = context.getSharedPreferences(SETTINGS_TAG, Context.MODE_PRIVATE)
            ratesUrl = pref.getString(RATES_URL, "https://revolut.duckdns.org")!!
            imagesUrl = pref.getString(IMAGES_URL, "https://www.countryflags.io")!!
        }

        fun getImageUrl(countryCode: String): String {
            return "${imagesUrl}/${countryCode.toLowerCase(Locale.getDefault())}/flat/64.png"
        }

        //  Created for settings edit in future
        @Suppress("UNUSED")
        fun updateRatesUrl(url: String) {
            ratesUrl = url
            pref.edit().putString(RATES_URL, url).apply()
        }

        @Suppress("UNUSED")
        fun updateImagesUrl(url: String) {
            imagesUrl = url
            pref.edit().putString(IMAGES_URL, url).apply()
        }
    }
}

@BindingAdapter("setWebImage")
fun setWebImage(view: ImageView, countryCode: String?) {
    countryCode?.let {

        Picasso.get()
            .load(RatesSettings.getImageUrl(countryCode))
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