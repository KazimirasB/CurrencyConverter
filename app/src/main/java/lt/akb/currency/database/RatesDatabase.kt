package lt.akb.currency.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/*
Creates currency rates database, add default EUR currency item and provide DAO to access data
 */

@Database(entities = [Rate::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseConverters::class)
abstract class RatesDatabase : RoomDatabase() {

    abstract fun getRateDao(): RateDao

}