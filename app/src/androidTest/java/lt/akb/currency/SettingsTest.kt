package lt.akb.currency

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import lt.akb.currency.main.RatesSettings
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun initSettings() {
        RatesSettings.init(context)
    }

    @Test
    @Throws(Exception::class)
    fun ratesUrl() {
        val urlRates = "http://wwww.test_rates.com"
        RatesSettings.updateRatesUrl(urlRates)
        assertEquals(RatesSettings.ratesUrl, urlRates)
    }
    @Test
    @Throws(Exception::class)
    fun imagesUrl() {
        val urlImages = "http://wwww.test_images.com"
        RatesSettings.updateImagesUrl(urlImages)
        val imagesUrl = RatesSettings.getImageUrl("TST")
        assertEquals("http://wwww.test_images.com/tst/flat/64.png", imagesUrl)
    }

}
