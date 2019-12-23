package lt.akb.currency.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.math.BigDecimal

@Dao
interface RateDao {

    //Insert single Rate item into database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Rate)

    //Insert list of Rate items into database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Rate>)

    //Get live data of all currency rates item in database in descending order
    @Query("SELECT * FROM currency_rate ORDER BY orderKey DESC")
    fun getAllLive(): LiveData<List<Rate>>

    //Update single currency rate by currency key
    @Query("UPDATE currency_rate SET currencyRate = :value WHERE currency= :currency")
    suspend fun updateValue(currency:String, value:BigDecimal)

}