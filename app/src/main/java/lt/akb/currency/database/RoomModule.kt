package lt.akb.currency.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lt.akb.currency.main.RatesSettings
import java.math.BigDecimal
import javax.inject.Singleton

@Module
class RoomModule {

    lateinit var appDatabase: AppDatabase

    @Provides
    @Singleton
    fun provideDatabase(context: Context, scope: CoroutineScope, settings: RatesSettings): AppDatabase {
        appDatabase =  Room.databaseBuilder(context, AppDatabase::class.java, "currency_rates_database")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    appDatabase?.let {
                        scope.launch {
                            it.getRateDao().insert(
                                Rate("EUR", settings.getImageUrl("EUR"), "EU euro", BigDecimal.ONE, 1)
                            )
                        }
                    }
                }
            })
            .build()
        return appDatabase
    }

    @Provides
    @Singleton
    fun provideRateDao(appDatabase: AppDatabase): RateDao {
        return appDatabase.getRateDao()
    }

}
