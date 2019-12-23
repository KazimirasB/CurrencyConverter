package lt.akb.currency

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import lt.akb.currency.database.AppDatabase
import lt.akb.currency.database.Rate
import lt.akb.currency.database.RateDao
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException
import java.math.BigDecimal

@RunWith(AndroidJUnit4::class)
class RateDatabaseTest {

    private lateinit var dao: RateDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        dao = db.getRateDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun checkCurrencyRateAddAndRead() {
        val rate = Rate("EUR", "EU", "EU euro", BigDecimal.ONE, 1000)

        val scope = CoroutineScope(Dispatchers.Main + Job())
        scope.launch {
            dao.insert(rate)
        }

        val items = dao.getAllLive()

        items.value?.let {
            assertThat(it[0], equalTo(rate))
        }
    }

}
