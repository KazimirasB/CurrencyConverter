package lt.akb.currency.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

/*
Creates currency rates database, add default EUR currency item and provide DAO to access data
 */

@Database(entities = [Rate::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getRateDao(): RateDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "currency_rates_database"
                )
                    .addCallback(CreateCallback(scope))
                    .build()

                INSTANCE = instance
                instance
            }
        }

        class CreateCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let {
                    scope.launch {
                        it.getRateDao().insert(
                            Rate("EUR", "EU", "EU euro", BigDecimal.ONE, 1))
                    }
                }
            }
        }
    }
}