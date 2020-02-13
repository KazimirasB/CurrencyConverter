package lt.akb.currency

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import lt.akb.currency.main.AppSettings
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun initSettings() {
       // AppSettings.init(context)
    }

    @Test
    @Throws(Exception::class)
    fun ratesUrl() {
        val urlRates = "http://wwww.test_rates.com"
//        AppSettings.updateRatesUrl(urlRates)
//        assertEquals(AppSettings.ratesUrl, urlRates)
    }
    @Test
    @Throws(Exception::class)
    fun imagesUrl() {
//        val urlImages = "http://wwww.test_images.com"
//        AppSettings.updateImagesUrl(urlImages)
//        val imagesUrl = AppSettings.getImageUrl("TST")
//        assertEquals("http://wwww.test_images.com/tst/flat/64.png", imagesUrl)
    }

}
