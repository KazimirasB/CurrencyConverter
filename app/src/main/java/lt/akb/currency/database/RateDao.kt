package lt.akb.currency.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.math.BigDecimal

@Dao
interface RateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Rate>)

    @Query("SELECT * FROM currency_rate ORDER BY isBase DESC")
    fun getAllLive(): LiveData<List<Rate>>

    @Query("UPDATE currency_rate SET value = :value WHERE currency= :currency")
    suspend fun updateValue(currency:String, value:BigDecimal)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll(items: List<Rate>)

}