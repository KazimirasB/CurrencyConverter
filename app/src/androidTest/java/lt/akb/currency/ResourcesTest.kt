package lt.akb.currency

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Test
import org.junit.runner.RunWith
import java.util.HashMap

@RunWith(AndroidJUnit4::class)
class ResourcesTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    @Throws(Exception::class)
    fun currencyMap() {
       val map: HashMap<String, String> =  Gson().fromJson(context.getString(R.string.currencies_json),
           object : TypeToken<HashMap<String, String>>() {}.type
       )
        assert(map.isNotEmpty())
    }

}
