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
import lt.akb.currency.database.AppDatabase
import lt.akb.currency.main.AppSettings
import java.math.BigDecimal
import javax.inject.Named
import javax.inject.Singleton

@Module
class RoomModule {

    lateinit var appDatabase: AppDatabase

    @Provides
    @Singleton
    fun provideDatabase(
        context: Context, scope: CoroutineScope, @Named("Euro currency") rate: Rate
    ): AppDatabase {
        appDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "currency_rates_database")
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
    fun provideRateDao(appDatabase: AppDatabase): RateDao {
        return appDatabase.getRateDao()
    }

    @Provides
    @Singleton
    @Named("Euro currency")
    fun provideEuroCurrency(settings: AppSettings): Rate {
        return Rate("EUR", settings.getImageUrl("EUR"), "EU euro", BigDecimal.ONE, 1)
    }

}
