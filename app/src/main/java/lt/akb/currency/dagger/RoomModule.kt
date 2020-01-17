package lt.akb.currency.dagger

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lt.akb.currency.database.Rate
import lt.akb.currency.database.RateDao
import lt.akb.currency.database.RatesDatabase
import lt.akb.currency.main.RatesSettings
import java.math.BigDecimal
import javax.inject.Named
import javax.inject.Singleton

@Module
class RoomModule {

    lateinit var appDatabase: RatesDatabase

    @Provides
    @Singleton
    fun provideDatabase(
        context: Context,
        scope: CoroutineScope, @Named("Euro currency") rate: Rate
    ): RatesDatabase {
        appDatabase =
            Room.databaseBuilder(context, RatesDatabase::class.java, "currency_rates_database")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        appDatabase?.let {
                            scope.launch {
                                it.getRateDao().insert(rate)
                            }
                        }
                    }
                })
                .build()
        return appDatabase
    }

    @Provides
    @Singleton
    fun provideRateDao(appDatabase: RatesDatabase): RateDao {
        return appDatabase.getRateDao()
    }

    @Provides
    @Singleton
    @Named("Euro currency")
    fun provideEuroCurrency(settings: RatesSettings): Rate {
        return Rate("EUR", settings.getImageUrl("EUR"), "EU euro", BigDecimal.ONE, 1)
    }

}
